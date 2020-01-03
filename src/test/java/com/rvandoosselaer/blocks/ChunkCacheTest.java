package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: rvandoosselaer
 */
public class ChunkCacheTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testCacheMaximumSize() {
        int size = 6;
        ChunkCache cache = new ChunkCache(size);

        IntStream.range(0, size * 2).forEach(i -> cache.put(Chunk.createAt(new Vec3i(i, 0, 0))));
        cache.maintain();

        assertEquals(size, cache.getSize());
    }

    @Test
    public void testCacheDefaultMaximumSize() {
        Vec3i gridSize = BlocksConfig.getInstance().getGrid();
        int size = gridSize.x * gridSize.y * gridSize.z;
        ChunkCache cache = new ChunkCache();

        IntStream.range(0, size * 2).forEach(i -> cache.put(Chunk.createAt(new Vec3i(i, 0, 0))));
        cache.maintain();

        assertEquals(size, cache.getSize());
    }

    @Test
    public void testPutChunkInCache() {
        ChunkCache cache = new ChunkCache();

        assertEquals(0, cache.getSize());

        cache.put(Chunk.createAt(new Vec3i(0, 0, 0)));

        assertEquals(1, cache.getSize());
    }

    @Test
    public void testGetChunkFromCache() {
        ChunkCache cache = new ChunkCache();
        Vec3i location = new Vec3i(0, 0, 0);
        Chunk chunk = Chunk.createAt(location);
        cache.put(chunk);

        Optional<Chunk> chunkFromCache = cache.get(location);

        assertTrue(chunkFromCache.isPresent());
        assertEquals(chunk, chunkFromCache.get());

    }

    @Test
    public void testGetChunkFromCacheThatIsNotInCache() {
        ChunkCache cache = new ChunkCache();
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        cache.put(chunk);

        Optional<Chunk> chunkFromCache = cache.get(new Vec3i(1, 2, 3));

        assertFalse(chunkFromCache.isPresent());
    }

    @Test
    public void testEvictFromCache() {
        ChunkCache cache = new ChunkCache();
        Vec3i location = new Vec3i(0, 0, 0);
        Chunk chunk = Chunk.createAt(location);
        cache.put(chunk);

        assertEquals(1, cache.getSize());

        cache.evict(location);

        assertEquals(0, cache.getSize());
    }

    @Test
    public void testEvictAllFromCache() {
        ChunkCache cache = new ChunkCache();
        IntStream.range(0, 20)
                .forEach(i -> cache.put(Chunk.createAt(new Vec3i(i, 0, 0))));

        assertEquals(20, cache.getSize());

        cache.evictAll();

        assertEquals(0, cache.getSize());
    }

    @Test
    public void testChunkCleanupIsTriggeredWhenEvictingFromCache() throws InterruptedException {
        ChunkCache cache = new ChunkCache();
        Chunk chunk = Chunk.createAt(new Vec3i());
        chunk.setNode(new Node());
        chunk.setCollisionMesh(new Mesh());

        cache.put(chunk);

        assertNotNull(chunk.getBlocks());
        assertNotNull(chunk.getNode());
        assertNotNull(chunk.getCollisionMesh());

        cache.evict(chunk.getLocation());

        Thread.sleep(500);

        assertNull(chunk.getBlocks());
        assertNull(chunk.getNode());
        assertNull(chunk.getCollisionMesh());
    }

}
