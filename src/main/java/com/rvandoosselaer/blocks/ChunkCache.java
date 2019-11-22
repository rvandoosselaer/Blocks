package com.rvandoosselaer.blocks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.simsilica.mathd.Vec3i;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

/**
 * An in memory threadsafe chunk cache implementation.
 *
 * @author: rvandoosselaer
 */
@Slf4j
public class ChunkCache implements ChunkResolver {

    private final Cache<Vec3i, Chunk> cache;

    public ChunkCache() {
        this(0);
    }

    public ChunkCache(int cacheSize) {
        this.cache = createCache(cacheSize);
    }

    @Override
    public Optional<Chunk> get(@NonNull Vec3i location) {
        return Optional.ofNullable(cache.getIfPresent(location));
    }

    @Override
    public Chunk getChunk(@NonNull Vec3i location) {
        return get(location).orElse(null);
    }

    public void evict(@NonNull Vec3i location) {
        cache.invalidate(location);
    }

    public void evictAll() {
        cache.invalidateAll();
    }

    public void put(@NonNull Chunk chunk) {
        cache.put(chunk.getLocation(), chunk);
    }

    public long getSize() {
        return cache.estimatedSize();
    }

    /**
     * By default, Caffeine does not perform cleanup and evict values "automatically" or instantly after a value
     * expires. Instead, it performs small amounts of maintenance work after write operations or occasionally after read
     * operations if writes are rare. If your cache is high-throughput then you don't have to worry about performing
     * cache maintenance to clean up expired entries and the like. If your cache is read and written to rarely, you may
     * wish to leverage an external thread, that calls Cache.cleanUp() when appropriate.
     */
    public void maintain() {
        cache.cleanUp();
    }

    private static Cache<Vec3i, Chunk> createCache(int cacheSize) {
        Vec3i gridSize = BlocksConfig.getInstance().getGridSize();
        int minimumSize = gridSize.x * gridSize.y * gridSize.z;

        if (cacheSize > 0 && cacheSize < minimumSize) {
            log.warn("The cache size of {} is lower then the recommended minimum size of {}.", cacheSize, minimumSize);
        }

        return Caffeine.newBuilder()
                .maximumSize(cacheSize > 0 ? cacheSize : minimumSize)
                .removalListener(new ChunkCacheRemovalListener())
                .build();
    }

    private static class ChunkCacheRemovalListener implements RemovalListener<Vec3i, Chunk> {

        @Override
        public void onRemoval(@Nullable Vec3i location, @Nullable Chunk chunk, @org.checkerframework.checker.nullness.qual.NonNull RemovalCause cause) {
            if (chunk != null && chunk.getNode() != null && chunk.getNode().getParent() != null) {
                log.warn("{} is evicted from the cache, but it's node is still attached to parent {}.", chunk, chunk.getNode().getParent());
            }

            if (chunk != null) {
                chunk.cleanup();
            }
        }

    }

}
