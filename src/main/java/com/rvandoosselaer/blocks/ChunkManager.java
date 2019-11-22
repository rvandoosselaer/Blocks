package com.rvandoosselaer.blocks;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * thread safe, but the lifecycle methods initialize, update and cleanup should be called from the same thread!
 *
 * @author: rvandoosselaer
 */
@Slf4j
public class ChunkManager {

    private final int cacheSize;
    @Getter
    private boolean initialized = false;
    private ChunkCache cache;
    private Queue<Vec3i> loadingQueue = new ConcurrentLinkedQueue<>();
    private Queue<Vec3i> generatorQueue = new ConcurrentLinkedQueue<>();
    private Queue<Chunk> meshQueue = new ConcurrentLinkedQueue<>();
    private List<Future<Chunk>> loadingResults = new ArrayList<>();
    private List<Future<Chunk>> generatorResults = new ArrayList<>();
    private List<Future<Chunk>> meshResults = new ArrayList<>();
    private ChunkRepository repository;
    private ChunkGenerator generator;
    private ChunkMeshGenerator meshGenerator;
    /**
     * Time between cache maintenance operations in milliseconds
     */
    private int cacheMaintenanceInterval = 1000;
    private long lastCacheMaintenanceTimestamp = -1;
    private int repositoryPoolSize = 1;
    private int generatorPoolSize = 1;
    private int meshPoolSize = 1;
    private ExecutorService repositoryExecutor;
    private ExecutorService generatorExecutor;
    private ExecutorService meshExecutor;

    public ChunkManager() {
        this(0);
    }

    public ChunkManager(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Builder
    private ChunkManager(int cacheSize, ChunkRepository repository, int repositoryPoolSize, ChunkGenerator generator, int generatorPoolSize, int meshPoolSize, int cacheMaintenanceInterval) {
        this.cacheSize = cacheSize;
        this.repository = repository;
        this.repositoryPoolSize = repositoryPoolSize;
        this.generator = generator;
        this.generatorPoolSize = generatorPoolSize;
        this.meshPoolSize = meshPoolSize;
        this.cacheMaintenanceInterval = cacheMaintenanceInterval;
    }

    /**
     * Calculate the location of the chunk that contains the given location.
     *
     * @param location
     * @return location of the chunk
     */
    public static Vec3i getLocation(Vector3f location) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        // Math.floor() rounds the decimal part down; 4.13 => 4.0, 4.98 => 4.0, -7.82 => -8.0
        // downcasting double to int removes the decimal part
        return new Vec3i((int) Math.floor(location.x / chunkSize.x), (int) Math.floor(location.y / chunkSize.y), (int) Math.floor(location.z / chunkSize.z));
    }

    public Optional<Chunk> get(Vec3i location) {
        assertInitialized();

        return location == null ? Optional.empty() : cache.get(location);
    }

    public void request(Vec3i location) {
        assertInitialized();

        if (!get(location).isPresent()) {
            addElementToQueue(new Vec3i(location), loadingQueue);
        }
    }

    public void requestUpdate(Vec3i location) {
        assertInitialized();

        get(location).ifPresent(chunk -> addElementToQueue(chunk, meshQueue));
    }

    public void requestUpdate(Chunk chunk) {
        assertInitialized();

        if (chunk != null) {
            addElementToQueue(chunk, meshQueue);
        }
    }

    /**
     * @param location of the chunk
     * @see #remove(Chunk)
     */
    public void remove(Vec3i location) {
        assertInitialized();

        if (location != null) {
            cache.evict(location);
        }
    }

    /**
     * Inform the ChunkManager that you no longer need access to the chunk and it can perform cleanup operations on it.
     *
     * @param chunk
     */
    public void remove(Chunk chunk) {
        assertInitialized();

        if (chunk != null) {
            cache.evict(chunk.getLocation());
        }
    }

    public void initialize() {
        if (log.isTraceEnabled()) {
            log.trace("{} - initialize", getClass().getSimpleName());
        }

        // create cache
        cache = new ChunkCache(cacheSize);

        // create executors
        if (repository != null) {
            repositoryExecutor = createNamedFixedThreadPool(Math.max(1, repositoryPoolSize), "chunk-repository-%d");
        } else {
            log.info("No ChunkRepository set.");
        }

        if (generator != null) {
            generatorExecutor = createNamedFixedThreadPool(Math.max(1, generatorPoolSize), "chunk-generator-%d");
        } else {
            log.info("No ChunkGenerator set.");
        }

        meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        meshExecutor = createNamedFixedThreadPool(Math.max(1, meshPoolSize), "chunk-mesh-%d");

        initialized = true;
    }

    public void update(float tpf) {
        assertInitialized();

        performLoading();
        performGeneration();
        performMeshGeneration();

        handleLoadResults();

        performCacheMaintenance();
    }

    public void cleanup() {
        assertInitialized();

        if (log.isTraceEnabled()) {
            log.trace("{} - cleanup", getClass().getSimpleName());
        }

        // stop executors
        repositoryExecutor.shutdownNow();
        generatorExecutor.shutdownNow();
        meshExecutor.shutdownNow();

        // clear queues and cache
        loadingQueue.clear();
        loadingResults.clear();
        generatorQueue.clear();
        generatorResults.clear();
        meshQueue.clear();
        meshResults.clear();
        cache.evictAll();

        initialized = false;
    }

    private void performLoading() {
        if (loadingQueue.isEmpty()) {
            return;
        }

        Vec3i location = loadingQueue.poll();
        loadChunk(location);
    }

    private void loadChunk(Vec3i location) {
        if (repository == null) {
            addElementToQueue(location, generatorQueue);
            return;
        }

        Future<Chunk> loadingResult = repositoryExecutor.submit(new LoadingCallable(location, repository));
        loadingResults.add(loadingResult);
    }

    private void performGeneration() {
        if (generatorQueue.isEmpty()) {
            return;
        }

        Vec3i location = generatorQueue.poll();
        generateChunk(location);
    }

    private void generateChunk(Vec3i location) {
        if (generator == null) {
            createChunk(location);
            return;
        }

        Future<Chunk> generatorResult = generatorExecutor.submit(new GeneratorCallable(location, generator));
        generatorResults.add(generatorResult);
    }

    private void performMeshGeneration() {
        if (meshQueue.isEmpty()) {
            return;
        }

        Chunk chunk = meshQueue.poll();
        generateMesh(chunk);
    }

    private void generateMesh(Chunk chunk) {
        Future<Chunk> meshResult = meshExecutor.submit(new MeshCallable(chunk, meshGenerator));
        meshResults.add(meshResult);
    }

    private void createChunk(Vec3i location) {
        Chunk chunk = Chunk.createAt(location);

        addToCache(chunk);
    }

    private void addToCache(Chunk chunk) {
        cache.put(chunk);
        //TODO: trigger ChunkManagerListener.chunkAvailable(chunk);
    }

    private void assertInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException(getClass().getSimpleName() + " is not initialized.");
        }
    }

    private void performCacheMaintenance() {
        boolean shouldPerformCacheMaintenance = cacheMaintenanceInterval > 0 &&
                System.currentTimeMillis() >= lastCacheMaintenanceTimestamp + cacheMaintenanceInterval;
        if (shouldPerformCacheMaintenance) {
            cache.maintain();
            lastCacheMaintenanceTimestamp = System.currentTimeMillis();
        }
    }

    /**
     * Helper method to add an element to a queue, making sure we don't add the same element multiple times.
     *
     * @param <T>
     * @param element
     * @param queue
     * @return true if the element was added to the queue, false if the element was already present in the queue.
     */
    private <T> boolean addElementToQueue(T element, Queue<T> queue) {
        if (!queue.contains(element)) {
            queue.add(element);
            return true;
        }

        return false;
    }

    private static ExecutorService createNamedFixedThreadPool(int size, String name) {
        return Executors.newFixedThreadPool(size, new ThreadFactoryBuilder().setNameFormat(name).build());
    }

    @RequiredArgsConstructor
    private static class LoadingCallable implements Callable<Chunk> {

        private final Vec3i location;
        private final ChunkRepository repository;

        @Override
        public Chunk call() {
            return repository.load(location);
        }

    }

    @RequiredArgsConstructor
    private static class GeneratorCallable implements Callable<Chunk> {

        private final Vec3i location;
        private final ChunkGenerator generator;

        @Override
        public Chunk call() {
            return generator.generate(location);
        }

    }

    @RequiredArgsConstructor
    private static class MeshCallable implements Callable<Chunk> {

        private final Chunk chunk;
        private final ChunkMeshGenerator meshGenerator;

        @Override
        public Chunk call() {
            meshGenerator.createAndSetNodeAndCollisionMesh(chunk);
            return chunk;
        }

    }

}
