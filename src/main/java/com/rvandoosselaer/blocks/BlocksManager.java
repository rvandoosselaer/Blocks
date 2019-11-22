package com.rvandoosselaer.blocks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Manages the loading, generating and mesh construction of chunk objects in a thread safe way. An internal cache is
 * used to store and retrieve chunks from memory.
 * The BlocksManager can be setup to delegate the loading, generation and mesh creation to worker threads when
 * a pool size of &gt; 0 is specified for the task.
 * <p>
 * The BlocksManager needs to be initialized before it can be used and should always be cleaned up to properly
 * shutdown the worker thread pools. It is a good practice to handle the lifecycle of the BlocksManager in an
 * {@link com.jme3.app.state.AppState} to properly initialize, update and cleanup the class.
 * <p>
 * A chunk can be retrieved by using the {@link #getChunk(Vec3i)} method. When the chunk isn't available it can be
 * requested with the {@link #requestChunk(Vec3i)} method.
 * The BlocksManager will first try to load the requested chunk using the {@link ChunkRepository}. When this is not
 * successful it will try to generate the chunk using the {@link ChunkGenerator}. When this also fails, an empty
 * chunk will be created. The requested chunk is placed in the cache and can be retrieved with the {@link #getChunk(Vec3i)}
 * method.
 * <p>
 * When the node of a chunk in the cache is not set, the generation of the mesh is still in progress. Try to retrieve
 * the node at a later time.
 * Applications can register a {@link BlocksManagerListener} to be notified when the mesh of a chunk is updated.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class BlocksManager implements ChunkResolver {

    /**
     * The size of the cache. Set to increase or decrease the number of chunks to keep in memory.
     */
    private final int cacheSize;

    private Cache<Vec3i, Chunk> cache;
    @Getter
    private boolean initialized = false;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int meshGenerationPoolSize = 0;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int chunkPersistencePoolSize = 0;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int chunkGenerationPoolSize = 0;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private ChunkRepository chunkRepository;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private ChunkGenerator chunkGenerator;
    private final Queue<Chunk> meshGenerationQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vec3i> chunkLoadingQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Vec3i> chunkGenerationQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Chunk> chunkStoringQueue = new ConcurrentLinkedQueue<>();
    private final List<BlocksManagerListener> blocksManagerListeners = new CopyOnWriteArrayList<>();
    private ExecutorService meshGenerationExecutor;
    private ExecutorService chunkPersistenceExecutor;
    private ExecutorService chunkGenerationExecutor;
    private List<Future<Chunk>> meshGenerationResults = new ArrayList<>();
    private List<Future<ChunkLoadResult>> chunkLoadingResults = new ArrayList<>();
    private List<Future<Chunk>> chunkGenerationResults = new ArrayList<>();
    private int storeInterval = -1;
    private long lastStoredTimestamp = -1;

    public BlocksManager() {
        this(0);
    }

    public BlocksManager(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Builder
    private BlocksManager(int cacheSize, int meshGenerationPoolSize, int chunkPersistencePoolSize, int chunkGenerationPoolSize, ChunkRepository chunkRepository, ChunkGenerator chunkGenerator, int storeInterval) {
        this.cacheSize = cacheSize;
        this.meshGenerationPoolSize = meshGenerationPoolSize;
        this.chunkPersistencePoolSize = chunkPersistencePoolSize;
        this.chunkGenerationPoolSize = chunkGenerationPoolSize;
        this.chunkRepository = chunkRepository;
        this.chunkGenerator = chunkGenerator;
        this.storeInterval = storeInterval;
    }

    public void initialize() {
        if (log.isTraceEnabled()) {
            log.trace("{} - initialize", getClass().getSimpleName());
        }

        cache = createCache(cacheSize);

        // start executors
        if (isMeshGenerationMultiThreaded()) {
            meshGenerationExecutor = createNamedFixedThreadPool(meshGenerationPoolSize, "chunk-mesh-generator-%d");
            log.debug("Created mesh generation ThreadPoolExecutor with pool size: {}", meshGenerationPoolSize);
        }
        if (isChunkPersistenceMultiThreaded()) {
            chunkPersistenceExecutor = createNamedFixedThreadPool(chunkPersistencePoolSize, "chunk-repository-%d");
            log.debug("Created chunk persistence ThreadPoolExecutor with pool size: {}", chunkPersistencePoolSize);
        }
        if (isChunkGenerationMultiThreaded()) {
            chunkGenerationExecutor = createNamedFixedThreadPool(chunkGenerationPoolSize, "chunk-generator-%d");
            log.debug("Created chunk generation ThreadPoolExecutor with pool size: {}", chunkGenerationPoolSize);
        }

        if (chunkRepository == null) {
            log.warn("No ChunkRepository set on BlocksManager.");
        }
        if (chunkGenerator == null) {
            log.warn("No ChunkGenerator is set on BlocksManager.");
        }

        this.initialized = true;
    }

    public void update() {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not properly initialized when update() is called!");
        }

        // mesh generation
        handleNextMeshUpdate();
        handleNextMeshUpdateResult();

        // chunk loading
        handleNextChunkLoad();
        handleNextChunkLoadResult();

        // chunk generation
        handleNextChunkGeneration();
        handleNextChunkGenerationResult();

        // chunk storing
        boolean timeToStoreChunks = storeInterval > 0 && System.currentTimeMillis() >= lastStoredTimestamp + storeInterval;
        if (timeToStoreChunks) {
            storeChunks();
            lastStoredTimestamp = System.currentTimeMillis();
        }
    }

    public void cleanup() {
        if (!isInitialized()) {
            return;
        }

        if (log.isTraceEnabled()) {
            log.trace("{} - cleanup", getClass().getSimpleName());
        }

        storeChunks();

        if (isMeshGenerationMultiThreaded()) {
            log.debug("Shutting down mesh generation ThreadPoolExecutor...");
            meshGenerationExecutor.shutdownNow();
            log.debug("Mesh generation ThreadPoolExecutor shut down.");
        }

        if (isChunkGenerationMultiThreaded()) {
            log.debug("Shutting down chunk generation ThreadPoolExecutor...");
            chunkGenerationExecutor.shutdownNow();
            log.debug("Chunk generation ThreadPoolExecutor shut down.");
        }

        if (isChunkPersistenceMultiThreaded()) {
            log.debug("Shutting down chunk persistence ThreadPoolExecutor...");
            chunkPersistenceExecutor.shutdown();
            log.debug("Chunk persistence ThreadPoolExecutor shut down.");
        }


        // clear the queues
        meshGenerationQueue.clear();
        chunkLoadingQueue.clear();
        chunkGenerationQueue.clear();
        // clear the listeners
        blocksManagerListeners.clear();
        // clear the future's
        meshGenerationResults.clear();
        chunkLoadingResults.clear();
        chunkGenerationResults.clear();
        // clear the cache
        cache.invalidateAll();
        cache.cleanUp();

        this.initialized = false;
    }

    @Override
    public Optional<Chunk> get(@NonNull Vec3i location) {
        return Optional.ofNullable(getChunk(location));
    }

    /**
     * Retrieves the chunk at the location from the cache.
     *
     * @param location of the chunk
     * @return chunk or null when not found
     */
    @Override
    public Chunk getChunk(@NonNull Vec3i location) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        Chunk chunk = cache.getIfPresent(location);
        if (log.isTraceEnabled()) {
            log.trace("Fetching chunk from cache: {} -> {}", location, chunk);
        }
        return chunk;
    }

    /**
     * @param location of the chunk
     * @return true when the chunk is in the cache, false otherwise
     */
    public boolean hasChunk(@NonNull Vec3i location) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }
        return getChunk(location) != null;
    }

    /**
     * Request the chunk to be loaded into the cache.
     * When the chunk cannot be loaded, it will be added to the generation queue.
     *
     * @param location of the chunk
     * @return true when the chunk is successfully requested, false otherwise
     */
    public boolean requestChunk(@NonNull Vec3i location) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        return !hasChunk(location) && addToQueue(chunkLoadingQueue, location);
    }

    /**
     * Discards the chunk associated with the location from the cache.
     *
     * @param location of the chunk
     */
    public void invalidateChunk(@NonNull Vec3i location) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        cache.invalidate(location);
    }

    /**
     * Store all chunks that have changed data using the {@link ChunkRepository}
     */
    public void saveChunks() {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        storeChunks();
    }

    /**
     * Add a listener to the list of listeners that gets notified.
     *
     * @param listener
     */
    public void registerListener(@NonNull BlocksManagerListener listener) {
        blocksManagerListeners.add(listener);
    }

    /**
     * Remove a listener from the list of listeners that gets notified.
     *
     * @param listener
     */
    public void removeListener(@NonNull BlocksManagerListener listener) {
        blocksManagerListeners.remove(listener);
    }

    /**
     * Place a block at the given world location. When the chunk to add the block to doesn't exist, a new one
     * is created.
     * If there was already a block on the location it will be replace by the new block.
     *
     * @param blockWorldLocation location in the world
     * @param block              to add
     * @return the previous block at the location or null
     */
    public Block addBlock(@NonNull Vec3i blockWorldLocation, Block block) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        Vec3i chunkLocation = BlocksManager.getChunkLocation(blockWorldLocation.toVector3f());
        Chunk chunk = getChunk(chunkLocation);
        if (chunk == null) {
            chunk = createChunk(chunkLocation);
        }

        Vec3i blockLocalLocation = chunk.toLocalLocation(blockWorldLocation);
        Block previousBlock = chunk.addBlock(blockLocalLocation, block);
        if (!Objects.equals(block, previousBlock)) {
            chunk.update();
            addToQueue(meshGenerationQueue, chunk);
            addToQueue(chunkStoringQueue, chunk);
        }
        return previousBlock;
    }

    /**
     * Removes and returns the block at the given world location.
     *
     * @param blockWorldLocation location in the world
     * @return the block at the given location or null
     */
    public Block removeBlock(@NonNull Vec3i blockWorldLocation) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager is not initialized!");
        }

        Vec3i chunkLocation = BlocksManager.getChunkLocation(blockWorldLocation.toVector3f());
        Chunk chunk = getChunk(chunkLocation);
        if (chunk == null) {
            log.warn("Trying to remove block at {} from chunk at {} that isn't in the cache.", blockWorldLocation, chunkLocation);
            return null;
        }

        Vec3i blockLocalLocation = chunk.toLocalLocation(blockWorldLocation);
        Block block = chunk.removeBlock(blockLocalLocation);
        if (block != null) {
            chunk.update();
            addToQueue(meshGenerationQueue, chunk);
            addToQueue(chunkStoringQueue, chunk);
        }
        return block;
    }

    /**
     * Calculate the picked block location based on the contact point and contact normals of a contact collision. By
     * setting the pickNeighbour flag, the neighbour of the picked block will be returned.
     *
     * @param contactPoint  collision contact point
     * @param contactNormal collision contact normal
     * @param pickNeighbour if the neighbour block should be returned
     * @return the picked block or the neighbour of the picked block
     */
    public static Vec3i getPickedBlockLocation(@NonNull Vector3f contactPoint, @NonNull Vector3f contactNormal, boolean pickNeighbour) {
        float blockScale = BlocksConfig.getInstance().getBlockScale();

        // add a small offset to the contact point, so we point a bit more 'inward' into the block
        Vector3f adjustedContactPoint = contactPoint.add(contactNormal.negate().mult(0.05f * blockScale));

        if (pickNeighbour) {
            Vector3f neighbourDirection = contactNormal.mult(0.75f * blockScale);
            adjustedContactPoint.addLocal(neighbourDirection);
        }

        Vec3i blockWorldLocation = new Vec3i((int) Math.floor(adjustedContactPoint.x), (int) Math.floor(adjustedContactPoint.y), (int) Math.floor(adjustedContactPoint.z));
        if (log.isTraceEnabled()) {
            log.trace("Calculated block location from contact point {} and contact normal {} : {}", contactPoint, contactNormal, blockWorldLocation);
        }
        return blockWorldLocation;
    }

    /**
     * Calculate the location of the chunk that contains the passed world location.
     *
     * @param worldLocation location in the world
     * @return the chunk location
     */
    public static Vec3i getChunkLocation(@NonNull Vector3f worldLocation) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        // Math.floor() rounds the decimal part down; 4.13 => 4.0, 4.98 => 4.0, -7.82 => -8.0
        // downcasting double to int removes the decimal part
        return new Vec3i((int) Math.floor(worldLocation.x / chunkSize.x), (int) Math.floor(worldLocation.y / chunkSize.y), (int) Math.floor(worldLocation.z / chunkSize.z));
    }

    /**
     * Perform a mesh update on the next chunk in the meshes generation queue.
     */
    private void handleNextMeshUpdate() {
        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        Chunk chunk = meshGenerationQueue.poll();

        if (meshGenerator == null || chunk == null) {
            return;
        }

        if (isMeshGenerationMultiThreaded()) {
            meshGenerationResults.add(meshGenerationExecutor.submit(new MeshGenerationCallable(chunk, meshGenerator)));
        } else {
            meshGenerator.createAndSetNodeAndCollisionMesh(chunk);
            notifyListenerOnChunkAvailable(chunk);
        }
    }

    /**
     * Perform the load operation of the next chunk in the chunk loading queue.
     */
    private void handleNextChunkLoad() {
        if (chunkLoadingQueue.isEmpty()) {
            return;
        }

        Vec3i chunkLocation = chunkLoadingQueue.poll();

        if (chunkRepository == null) {
            addToQueue(chunkGenerationQueue, chunkLocation);
        } else {
            if (isChunkPersistenceMultiThreaded()) {
                chunkLoadingResults.add(chunkPersistenceExecutor.submit(new ChunkLoadCallable(chunkLocation, chunkRepository)));
            } else {
                Chunk chunk = chunkRepository.load(chunkLocation);
                if (chunk != null) {
                    addToCache(chunk);
                    addToQueue(meshGenerationQueue, chunk);
                } else {
                    addToQueue(chunkGenerationQueue, chunkLocation);
                }
            }
        }
    }

    /**
     * Perform the generation of the next chunk in the chunk generation queue.
     */
    private void handleNextChunkGeneration() {
        if (chunkGenerationQueue.isEmpty()) {
            return;
        }

        Vec3i chunkLocation = chunkGenerationQueue.poll();

        if (chunkGenerator == null) {
            // unable to generate the chunk, just create one
            createChunk(chunkLocation);
        } else {
            if (isChunkGenerationMultiThreaded()) {
                chunkGenerationResults.add(chunkGenerationExecutor.submit(new ChunkGenerationCallable(chunkLocation, chunkGenerator)));
            } else {
                Chunk chunk = chunkGenerator.generate(chunkLocation);
                chunk.update();
                addToCache(chunk);
                addToQueue(meshGenerationQueue, chunk);
                addToQueue(chunkStoringQueue, chunk);
            }
        }
    }

    /**
     * Perform the processing of the next finished chunk mesh generation task.
     */
    private void handleNextMeshUpdateResult() {
        if (!isMeshGenerationMultiThreaded() || meshGenerationResults.isEmpty()) {
            return;
        }

        // get the first completed future, process and remove it
        Optional<Future<Chunk>> optionalFuture = meshGenerationResults.stream().filter(Future::isDone).findFirst();
        optionalFuture.ifPresent(future -> {
            try {
                notifyListenerOnChunkAvailable(future.get());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            meshGenerationResults.remove(future);
        });

        // remove cancelled futures
        meshGenerationResults.removeIf(Future::isCancelled);
    }

    /**
     * Perform the processing of the next finished loaded chunk task.
     */
    private void handleNextChunkLoadResult() {
        if (!isChunkPersistenceMultiThreaded() || chunkLoadingResults.isEmpty()) {
            return;
        }

        Optional<Future<ChunkLoadResult>> optionalFuture = chunkLoadingResults.stream().filter(Future::isDone).findFirst();
        optionalFuture.ifPresent(future -> {
            try {
                ChunkLoadResult result = future.get();
                if (result.hasChunk()) {
                    addToCache(result.getChunk());
                    addToQueue(meshGenerationQueue, result.getChunk());
                } else {
                    addToQueue(chunkGenerationQueue, result.getLocation());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            chunkLoadingResults.remove(future);
        });

        // remove cancelled futures
        chunkLoadingResults.removeIf(Future::isCancelled);
    }

    /**
     * Perform the processing of the next finished chunk generation task.
     */
    private void handleNextChunkGenerationResult() {
        if (!isChunkGenerationMultiThreaded() || chunkGenerationResults.isEmpty()) {
            return;
        }

        Optional<Future<Chunk>> optionalFuture = chunkGenerationResults.stream().filter(Future::isDone).findFirst();
        optionalFuture.ifPresent(future -> {
            try {
                Chunk chunk = future.get();
                chunk.update();
                addToCache(chunk);
                addToQueue(meshGenerationQueue, chunk);
                addToQueue(chunkStoringQueue, chunk);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            chunkGenerationResults.remove(future);
        });

        // remove cancelled futures
        chunkGenerationResults.removeIf(Future::isCancelled);
    }

    private void storeChunks() {
        if (chunkRepository == null || chunkStoringQueue.isEmpty()) {
            return;
        }

        Set<Chunk> chunks = new HashSet<>(chunkStoringQueue);

        if (isChunkPersistenceMultiThreaded()) {
            chunkPersistenceExecutor.submit(new ChunkStoringRunnable(chunks, chunkRepository));
        } else {
            chunks.forEach(chunkRepository::save);
            chunks.clear();
        }
    }

    /**
     * Create a chunk and add it to the cache.
     *
     * @param chunkLocation
     * @return created chunk
     */
    private Chunk createChunk(@NonNull Vec3i chunkLocation) {
        Chunk chunk = Chunk.createAt(chunkLocation);
        if (log.isTraceEnabled()) {
            log.trace("Created {}", chunk);
        }
        addToCache(chunk);
        return chunk;
    }

    private boolean isMeshGenerationMultiThreaded() {
        return meshGenerationPoolSize > 0;
    }

    private boolean isChunkPersistenceMultiThreaded() {
        return chunkPersistencePoolSize > 0;
    }

    private boolean isChunkGenerationMultiThreaded() {
        return chunkGenerationPoolSize > 0;
    }

    /**
     * Notify the list of listeners when a chunk or updated version of a chunk is available.
     *
     * @param chunk
     */
    private void notifyListenerOnChunkAvailable(Chunk chunk) {
        blocksManagerListeners.forEach(listener -> listener.onChunkAvailable(chunk));
    }

    /**
     * Add the element to the queue, checking so the same element isn't added multiple times to the queue.
     *
     * @param queue
     * @param element
     * @param <T>
     * @return true when the element is added to the queue or when the element was already in the queue, false otherwise
     */
    private <T> boolean addToQueue(@NonNull Queue<T> queue, @NonNull T element) {
        if (!queue.contains(element)) {
            return queue.offer(element);
        }
        return true;
    }

    private void addToCache(@NonNull Chunk chunk) {
        if (chunk.getChunkResolver() == null) {
            chunk.setChunkResolver(this);
            if (log.isTraceEnabled()) {
                log.trace("Setting {} as chunk resolver on {}.", this, chunk);
            }
        }
        cache.put(chunk.getLocation(), chunk);
        if (log.isTraceEnabled()) {
            log.trace("Adding {} to cache. Estimated new cache size: {}", chunk, cache.estimatedSize());
        }
    }

    /**
     * Construct the BlocksManager cache. When a size &lte; 0 of is specified, a minimum value is calculated.
     *
     * @param cacheSize
     * @return cache
     */
    private static Cache<Vec3i, Chunk> createCache(int cacheSize) {
        Vec3i gridSize = BlocksConfig.getInstance().getGridSize();
        int minimumSize = (gridSize.x + 1) * (gridSize.y + 1) * (gridSize.z + 1);
        if (cacheSize > 0 && cacheSize < minimumSize) {
            log.warn("The cache size of {} is lower then the recommended minimum size of {}.", cacheSize, minimumSize);
        }

        return Caffeine.newBuilder()
                .maximumSize(cacheSize > 0 ? cacheSize : minimumSize)
                .removalListener(new CacheRemovalListener())
                .build();
    }

    private static ExecutorService createNamedFixedThreadPool(int size, String name) {
        return Executors.newFixedThreadPool(size, new ThreadFactoryBuilder().setNameFormat(name).build());
    }

    private static class CacheRemovalListener implements RemovalListener<Vec3i, Chunk> {

        @Override
        public void onRemoval(@Nullable Vec3i key, @Nullable Chunk value, @org.checkerframework.checker.nullness.qual.NonNull RemovalCause cause) {
            if (value != null && value.getNode() != null && value.getNode().getParent() != null) {
                log.warn("{} is evicted from the cache, but its node is still attached!", value);
            }

            Optional.ofNullable(value).ifPresent(Chunk::clean);
        }

    }

    @RequiredArgsConstructor
    private static class MeshGenerationCallable implements Callable<Chunk> {

        @NonNull
        private final Chunk chunk;
        @NonNull
        private final ChunkMeshGenerator meshGenerationStrategy;

        @Override
        public Chunk call() throws Exception {
            meshGenerationStrategy.createAndSetNodeAndCollisionMesh(chunk);
            return chunk;
        }

    }

    @RequiredArgsConstructor
    private static class ChunkLoadCallable implements Callable<ChunkLoadResult> {

        @NonNull
        private final Vec3i chunkLocation;
        @NonNull
        private final ChunkRepository chunkRepository;

        @Override
        public ChunkLoadResult call() throws Exception {
            return new ChunkLoadResult(chunkLocation, chunkRepository.load(chunkLocation));
        }

    }

    @RequiredArgsConstructor
    private static class ChunkGenerationCallable implements Callable<Chunk> {

        @NonNull
        private final Vec3i chunkLocation;
        @NonNull
        private final ChunkGenerator chunkGenerator;

        @Override
        public Chunk call() throws Exception {
            return chunkGenerator.generate(chunkLocation);
        }

    }

    @RequiredArgsConstructor
    private static class ChunkStoringRunnable implements Runnable {

        private final Collection<Chunk> chunks;
        private final ChunkRepository repository;

        @Override
        public void run() {
            chunks.forEach(repository::save);
            chunks.clear();
        }

    }

    @Getter
    @RequiredArgsConstructor
    private static class ChunkLoadResult {

        @NonNull
        private final Vec3i location;
        private final Chunk chunk;

        public boolean hasChunk() {
            return chunk != null;
        }

    }

}
