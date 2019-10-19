package com.rvandoosselaer.blocks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * A class for managing the loading, generating and mesh constructing of chunk objects in a thread safe way.
 * The chunk manager uses an internal cache to store and retrieve chunks from memory. The size of the cache should at
 * least be the total number of available chunks based on the grid size when the {@link BlocksPager} is used.
 * With a grid size of (9,5,9) the size of the cache should be 9 x 5 x 9 = 405.
 * The chunk manager can be setup to delegate tasks to worker threads when a pool size of &gt; 0 is specified.
 * <p>
 * The chunk manager should be initialized before it can be used and should always be cleaned up to properly shutdown
 * the thread pool. It is good practice to handle the lifecycle of the chunkmanager in an {@link com.jme3.app.state.AppState}
 * to properly initialize, update and cleanup the class.
 * <p>
 * A chunk can be requested from the chunk manager by using the {@link #getChunk(Vector3i)} method. When the chunk
 * isn't available it can be requested with the {@link #requestChunk(Vector3i)} method. The chunk manager will try to
 * load a requested chunk using the supplied {@link ChunkLoader}. The loaded chunk is placed in the cache and can now
 * be retrieved using the {@link #getChunk(Vector3i)} method.
 * When the chunk manager was unable to load the requested chunk using the {@link ChunkLoader} it will generate the
 * chunk using the {@link ChunkGenerator}. The generated chunk is placed in the cache and can be retrieved using the
 * {@link #getChunk(Vector3i)} method.
 * Each chunk that is placed in the cache, through loading or generation, will have it's mesh generated. When the node
 * of a chunk in the cache is not set, the generation of the mesh is still in progress. The node should be fetched at
 * a later time.
 * <p>
 * Applications can register a {@link MeshGenerationListener} to the chunk manager to be notified when the mesh of a
 * chunk changes.
 *
 * @author remy
 */
@Slf4j
public class BlocksManager {

    private final int poolSize;
    private final int cacheSize;
    // queue of chunks to load
    private final Queue<Vec3i> loadQueue = new ConcurrentLinkedQueue<>();
    // list of chunk load tasks
    private final List<Future<ChunkLoadResult>> loadResultList = new ArrayList<>();
    // queue of chunks that need to be populated
    private final Queue<Vec3i> generationQueue = new ConcurrentLinkedQueue<>();
    // list of chunk generation tasks
    private final List<Future<Chunk>> generationResultList = new ArrayList<>();
    // queue of chunks to need an updated mesh
    private final Queue<Vec3i> meshesGenerationQueue = new ConcurrentLinkedQueue<>();
    // list of chunks mesh generation tasks
    private final List<Future<Vec3i>> meshesGenerationResultList = new ArrayList<>();
    // a list of listeners to notify when the mesh of a chunk is updated
    private final List<MeshGenerationListener> meshListeners = new CopyOnWriteArrayList<>();

    @Getter
    @Setter
    private ChunkLoader chunkLoader;
    @Getter
    @Setter
    private ChunkGenerator chunkGenerator;
    @Getter
    @Setter
    private MeshGenerationStrategy meshGenerationStrategy;
    //private ThreadPoolExecutor executor;
    private ExecutorService executor;
    private Cache<Vec3i, Chunk> chunkCache;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean initialized = false;

//    public BlocksManager() {
//        this(0, 0, null, null, null);
//    }

//    public BlocksManager(int poolSize) {
//        this(poolSize, 0, null, null, null);
//    }

//    public BlocksManager(int poolSize, int cacheSize) {
//        this(poolSize, cacheSize, null, null, null);
//    }

    private BlocksManager(int poolSize, int cacheSize, ChunkLoader chunkLoader, ChunkGenerator chunkGenerator, MeshGenerationStrategy meshGenerationStrategy) {
        this.poolSize = poolSize;
        this.cacheSize = cacheSize;
        this.chunkLoader = chunkLoader;
        this.chunkGenerator = chunkGenerator;
        this.meshGenerationStrategy = meshGenerationStrategy;
    }

    public void initialize() {
        // create the threadpool executor
        if (isMultiThreaded()) {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            if (poolSize > availableProcessors) {
                log.warn("The requested pool size of {} is larger then the number of processors: {}.", poolSize, availableProcessors);
            }
            //executor = new ScheduledThreadPoolExecutor(poolSize);
            executor = Executors.newFixedThreadPool(poolSize);
            log.debug("Constructed ThreadPoolExecutor with pool size: {}", poolSize);
        }

        // create the cache
        Vec3i gridSize = BlocksConfig.getInstance().getGridSize();
        int minimumSize = gridSize.x * gridSize.y * gridSize.z;
        if (cacheSize > 0 && cacheSize < minimumSize) {
            log.warn("The cache size of {} is lower then the recommended minimum size of {}.", cacheSize, minimumSize);
        }
        chunkCache = cacheSize > 0 ? Caffeine.newBuilder().maximumSize(cacheSize).build() : Caffeine.newBuilder().maximumSize(minimumSize).build();
        log.debug("Constructed cache of size: {}", cacheSize > 0 ? cacheSize : minimumSize);
        initialized = true;
    }

    public void update(float tpf) {
        if (!isInitialized()) {
            throw new IllegalStateException("BlocksManager was not properly initialized before update() was called!");
        }

        // 1. generate chunk meshes
        handleChunkMeshGeneration();

        // 2. load chunks
        handleChunkLoading();

        // 3. generate chunks
        handleChunkGeneration();
    }

    public void cleanup() {
        if (!isInitialized()) {
            return;
        }

        if (isMultiThreaded()) {
            log.debug("Shutting down ThreadPoolExecutor...");
            executor.shutdownNow();
            log.debug("ThreadPoolExecutor shut down.");
        }
        chunkCache.cleanUp();
        log.debug("Cache cleared.");
    }

    /**
     * Returns the chunk at the given location from the cache.
     *
     * @param chunkLocation of the chunk
     * @return chunk or null
     */
    public Chunk getChunk(Vec3i chunkLocation) {
        Chunk chunk = chunkCache.getIfPresent(chunkLocation);
        if (log.isTraceEnabled()) {
            log.trace("Retrieving chunk {} from cache: {}", chunkLocation, chunk);
        }
        return chunk;
    }

    /**
     * Checks if the chunk at the given location is available.
     *
     * @param chunkLocation of the chunk
     * @return true when the chunk is available
     */
    public boolean hasChunk(Vec3i chunkLocation) {
        return getChunk(chunkLocation) != null;
    }

    /**
     * Request the BlocksManager to fetch the chunk at the given location.
     *
     * @param chunkLocation of the chunk
     * @return true if the chunk is successfully requested
     */
    public boolean requestChunk(Vec3i chunkLocation) {
        if (chunkLocation == null || hasChunk(chunkLocation)) {
            return false;
        }

        if (log.isTraceEnabled()) {
            log.trace("Requesting chunk {}", chunkLocation);
        }

        return addToQueue(loadQueue, chunkLocation);
    }

    /**
     * Request the BlocksManager to update the meshes of the chunk at the given location.
     * @param chunkLocation of the chunk
     * @return true if successfully requested
     */
    public boolean requestChunkMeshUpdate(Vec3i chunkLocation) {
        if (chunkLocation == null || !hasChunk(chunkLocation)) {
            return false;
        }

        return addToQueue(meshesGenerationQueue, chunkLocation);
    }

    /**
     * Add a listener to the list of listeners that are notified when the mesh generation of a chunk is finished.
     *
     * @param listener to add
     */
    public void addListener(MeshGenerationListener listener) {
        meshListeners.add(listener);
    }

    /**
     * Remove a listener from the list of listeners that are notified when the mesh generation of a chunk is finished.
     *
     * @param listener to remove
     */
    public void removeListener(MeshGenerationListener listener) {
        meshListeners.remove(listener);
    }

    /**
     * Calculate the picked block location based on the contact point and contact normals of a contact collision. By
     * setting the pickNeighbour flag, the neighbour of the picked block is returned.
     *
     * @param contactPoint  collision contact point
     * @param contactNormal collision contact normal
     * @param pickNeighbour if the neighbour block should be returned
     * @return the picked block or the neighbour of the picked block
     */
    public Vec3i getPickedBlockLocation(Vector3f contactPoint, Vector3f contactNormal, boolean pickNeighbour) {
        if (contactPoint == null || contactNormal == null) {
            return null;
        }

        // add a small offset to the contact point, so we point a bit more 'inward' into the block
        contactPoint = contactPoint.add(contactNormal.negate().mult(0.05f));

        if (pickNeighbour) {
            contactPoint.addLocal(contactNormal.mult(0.75f));
        }

        Vec3i blockWorldLocation = new Vec3i((int) Math.floor(contactPoint.x), (int) Math.floor(contactPoint.y), (int) Math.floor(contactPoint.z));
        if (log.isTraceEnabled()) {
            log.trace("Calculated block location from contact point {} and contact normal {} : {}", contactPoint, contactNormal, blockWorldLocation);
        }
        return blockWorldLocation;
    }

    /**
     * Add the block to the given block world location. When the chunk to add the block to could not be found, a new
     * one is created.
     *
     * @param blockWorldLocation location in the world
     * @param block              to add
     * @return true if the block is successfully added
     */
    public boolean addBlock(Vec3i blockWorldLocation, Block block) {
        if (blockWorldLocation == null || block == null) {
            return false;
        }

        Vec3i chunkLocation = BlocksManager.getChunkLocation(blockWorldLocation.toVector3f());
        Chunk chunk = getChunk(chunkLocation);
        if (chunk == null) {
            chunk = Chunk.create(chunkLocation);
            if (log.isTraceEnabled()) {
                log.trace("Chunk not found in the cache, created new chunk {}", chunk);
            }
            addToCache(chunk, false);
        }

        Vec3i localBlockCoord = chunk.toLocalCoordinate(blockWorldLocation);
        chunk.addBlock(localBlockCoord.x, localBlockCoord.y, localBlockCoord.z, block);
        addToQueue(meshesGenerationQueue, chunkLocation);
        log.debug("Added block {} - {} to chunk {}", block, blockWorldLocation, chunk);
        return true;
    }

    /**
     * Removes and returns the block at the given world location.
     *
     * @param blockWorldLocation location in the world
     * @return the block at the given location
     */
    public Block removeBlock(Vec3i blockWorldLocation) {
        if (blockWorldLocation == null) {
            return null;
        }

        Vec3i chunkLocation = BlocksManager.getChunkLocation(blockWorldLocation.toVector3f());
        Chunk chunk = getChunk(chunkLocation);
        if (chunk == null) {
            log.warn("Trying to remove block {} from chunk {} that isn't found in the cache.", blockWorldLocation, chunkLocation);
            return null;
        }

        Vec3i blockLocal = chunk.toLocalCoordinate(blockWorldLocation);
        Block block = chunk.removeBlock(blockLocal.x, blockLocal.y, blockLocal.z);
        if (block != null) {
            addToQueue(meshesGenerationQueue, chunkLocation);
        }
        log.debug("Removed block {} at {} from chunk {}", block, blockWorldLocation, chunk);
        return block;
    }

    /**
     * Calculate the chunk location that contains the given world location.
     *
     * @param worldLocation location in the world
     * @return the chunk location
     */
    public static Vec3i getChunkLocation(Vector3f worldLocation) {
        if (worldLocation == null) {
            return null;
        }

        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        // Math.floor() rounds the decimal part down; 4.13 => 4.0, 4.98 => 4.0, -7.82 => -8.0
        // downcasting double to int removes the decimal part
        Vec3i chunkLocation = new Vec3i((int) Math.floor(worldLocation.x / chunkSize.x), (int) Math.floor(worldLocation.y / chunkSize.y), (int) Math.floor(worldLocation.z / chunkSize.z));
        if (log.isTraceEnabled()) {
            log.trace("Calculated chunk location {} from world location {}", chunkLocation, worldLocation);
        }
        return chunkLocation;
    }

    /**
     * Notify the list of listeners that the mesh of the chunk is generated.
     *
     * @param chunkLocation of the chunk
     */
    private void notifyMeshGenerationListeners(Vec3i chunkLocation) {
        for (MeshGenerationListener listener : meshListeners) {
            listener.generationFinished(chunkLocation);
        }
    }

    /**
     * Handles the generation of chunk meshes. All chunks that have new meshes are added to the updatedChunkQueue.
     */
    private void handleChunkMeshGeneration() {
        // check for completed tasks
        for (Iterator<Future<Vec3i>> i = meshesGenerationResultList.iterator(); i.hasNext(); ) {
            Future<Vec3i> future = i.next();
            if (future.isDone()) {
                try {
                    notifyMeshGenerationListeners(future.get());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    i.remove();
                }
            } else if (future.isCancelled()) {
                log.warn("Mesh generation task was cancelled.");
                i.remove();
            }
        }

        // check for new tasks
        Vec3i chunkLocation = meshesGenerationQueue.poll();
        if (chunkLocation != null) {
            Chunk chunk = getChunk(chunkLocation);
            if (chunk == null) {
                log.warn("Requested mesh generation of chunk {} that is not found in the cache, skipping mesh generation!", chunkLocation);
            } else {
                if (isMultiThreaded()) {
                    // check if should notify when the mesh generation is done
                    meshesGenerationResultList.add(executor.submit(new MeshGenerationCallable(chunk, meshGenerationStrategy)));
                } else {
                    // immediately create a new mesh and add it to the updated chunk queue
                    meshGenerationStrategy.generateNodeAndCollisionMesh(chunk);
                    notifyMeshGenerationListeners(chunkLocation);
                }
            }
        }
    }

    /**
     * Handles the loading of chunks. When a chunk failed to load, it is added to the generationQueue. Otherwise it is
     * added to the cache.
     */
    private void handleChunkLoading() {
        // check for completed tasks
        for (Iterator<Future<ChunkLoadResult>> i = loadResultList.iterator(); i.hasNext(); ) {
            Future<ChunkLoadResult> future = i.next();
            if (future.isDone()) {
                try {
                    ChunkLoadResult result = future.get();
                    if (result.hasResult()) {
                        addToCache(result.getChunk(), true);
                    } else {
                        addToQueue(generationQueue, result.chunkLocation);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    i.remove();
                }
            } else if (future.isCancelled()) {
                log.warn("Chunk load task was cancelled.");
                i.remove();
            }
        }

        // check for new tasks
        Vec3i chunkLocation = loadQueue.poll();
        if (chunkLocation != null) {
            // check if the chunk isn't in the cache
            Chunk chunk = getChunk(chunkLocation);
            if (chunk == null) {
                if (isMultiThreaded()) {
                    loadResultList.add(executor.submit(new ChunkLoadCallable(chunkLocation, chunkLoader)));
                } else {
                    chunk = chunkLoader.load(chunkLocation);
                    if (chunk != null) {
                        addToCache(chunk, true);
                    } else {
                        addToQueue(generationQueue, chunkLocation);
                    }
                }
            }
        }
    }

    /**
     * Handles the generation of chunks. When a chunk is generated with block data, call the {@link Chunk#update()}
     * method and add it to the cache.
     */
    private void handleChunkGeneration() {
        // check for completed tasks
        for (Iterator<Future<Chunk>> i = generationResultList.iterator(); i.hasNext(); ) {
            Future<Chunk> future = i.next();
            if (future.isDone()) {
                try {
                    Chunk chunk = future.get();
                    chunk.update();
                    addToCache(chunk, true);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    i.remove();
                }
            } else if (future.isCancelled()) {
                log.warn("Chunk generation task was cancelled.");
                i.remove();
            }
        }

        // check for new tasks
        Vec3i chunkLocation = generationQueue.poll();
        if (chunkLocation != null) {
            // check if the chunk isn't in the cache
            Chunk chunk = getChunk(chunkLocation);
            if (chunk == null) {
                if (isMultiThreaded()) {
                    generationResultList.add(executor.submit(new ChunkGenerationCallable(chunkLocation, chunkGenerator)));
                } else {
                    chunk = chunkGenerator.generate(chunkLocation);
                    chunk.update();
                    addToCache(chunk, true);
                }
            }
        }
    }

    /**
     * Adds the given chunk to cache. When the needMeshGeneration flag is set, the chunk will be added to the mesh
     * generation queue but it will not notify the listeners when the mesh generation is finished.
     *
     * @param chunk              to add
     * @param needMeshGeneration true if the meshes of the chunk need to be generated
     * @return true if the chunk is successfully added to the cache
     */
    private boolean addToCache(Chunk chunk, boolean needMeshGeneration) {
        if (chunk == null) {
            return false;
        }

        if (needMeshGeneration) {
            addToQueue(meshesGenerationQueue, chunk.getLocation());
        }

        if (log.isTraceEnabled()) {
            log.trace("Adding chunk {} to cache", chunk.getLocation());
        }

        chunkCache.put(chunk.getLocation(), chunk);
        return true;
    }

    private boolean isMultiThreaded() {
        return poolSize > 0;
    }

    /**
     * Helper method to add an object to a queue, making sure we don't add the same object multiple times.
     *
     * @param queue to add the object to
     * @param object object to add
     * @param <T> type of the queue
     * @return true if the object was already in the queue or if the object was successfully added to the queue, false
     * otherwise
     */
    private <T> boolean addToQueue(Queue<T> queue, T object) {
        if (!queue.contains(object)) {
            return queue.offer(object);
        }
        return true;
    }

//    public static class Builder {
//
//        private int cacheSize = 0;
//        private ChunkLoader loader;
//        private ChunkGenerator generator;
//        private MeshGenerationStrategy meshGenerationStrategy;
//
//        public Builder cacheSize(int cacheSize) {
//            this.cacheSize = cacheSize;
//            return this;
//        }
//
//        public Builder chunkLoader(ChunkLoader loader) {
//            this.loader = loader;
//            return this;
//        }
//
//        public Builder generator(ChunkGenerator generator) {
//            this.generator = generator;
//            return this;
//        }
//
//        public Builder meshGenerationStrategy(MeshGenerationStrategy meshGenerationStrategy) {
//            this.meshGenerationStrategy = meshGenerationStrategy;
//            return this;
//        }
//
//        public BlocksManager build() {
//            return new BlocksManager(0, cacheSize, loader, generator, meshGenerationStrategy);
//        }
//    }

    @RequiredArgsConstructor
    private class MeshGenerationCallable implements Callable<Vec3i> {

        private final Chunk chunk;
        private final MeshGenerationStrategy meshGenerationStrategy;

        @Override
        public Vec3i call() {
            meshGenerationStrategy.generateNodeAndCollisionMesh(chunk);
            return chunk.getLocation();
        }

    }

    @RequiredArgsConstructor
    private class ChunkLoadCallable implements Callable<ChunkLoadResult> {

        private final Vec3i chunkLocation;
        private final ChunkLoader chunkLoader;

        @Override
        public ChunkLoadResult call() {
            return new ChunkLoadResult(chunkLocation, chunkLoader.load(chunkLocation));
        }

    }

    @RequiredArgsConstructor
    private class ChunkGenerationCallable implements Callable<Chunk> {

        private final Vec3i chunkLocation;
        private final ChunkGenerator chunkGenerator;

        @Override
        public Chunk call() {
            return chunkGenerator.generate(chunkLocation);
        }

    }

    @Getter
    @RequiredArgsConstructor
    private class ChunkLoadResult {

        private final Vec3i chunkLocation;
        private final Chunk chunk;

        public boolean hasResult() {
            return chunk != null;
        }

    }

}