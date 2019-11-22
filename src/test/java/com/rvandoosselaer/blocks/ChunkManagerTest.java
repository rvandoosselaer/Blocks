package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: rvandoosselaer
 */
public class ChunkManagerTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testLocationCalculation() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 16, 16));

        Vector3f location = new Vector3f(0, 0, 0);
        Vec3i chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(13, 10, 5);
        chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(-5, 3, -9);
        chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(-1, 0, -1), chunkLocation);

        location = new Vector3f(16, 15, 2);
        chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(1, 0, 0), chunkLocation);

        location = new Vector3f(16, 32, 2);
        chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(1, 2, 0), chunkLocation);
    }

    @Test
    public void testRequestChunkWithRepository() throws InterruptedException {
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        chunk.update();

        ChunkRepository repository = Mockito.mock(ChunkRepository.class);
        Mockito.when(repository.load(new Vec3i(0, 0, 0))).thenReturn(chunk);

        ChunkManager chunkManager = ChunkManager.builder().repository(repository).build();
        chunkManager.initialize();

        Optional<Chunk> loadedChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertFalse(loadedChunk.isPresent());

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update(); // loading done
        Thread.sleep(50);
        chunkManager.update(); // mesh done

        loadedChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(loadedChunk.isPresent());
        assertEquals(loadedChunk.get(), chunk);
    }

    @Test
    public void testRequestChunkWithGenerator() throws InterruptedException {
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        chunk.update();

        ChunkGenerator generator = Mockito.mock(ChunkGenerator.class);
        Mockito.when(generator.generate(new Vec3i(0, 0, 0))).thenReturn(chunk);

        ChunkManager chunkManager = ChunkManager.builder().generator(generator).build();
        chunkManager.initialize();

        Optional<Chunk> generatedChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertFalse(generatedChunk.isPresent());

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update(); // generation done
        Thread.sleep(50);
        chunkManager.update(); // mesh done

        generatedChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(generatedChunk.isPresent());
        assertEquals(generatedChunk.get(), chunk);
    }

    @Test
    public void testRequestChunk() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        Optional<Chunk> chunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertFalse(chunk.isPresent());

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update(); // creation is instant
        Thread.sleep(50);
        chunkManager.update(); // mesh done

        chunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(chunk.isPresent());
        assertTrue(chunk.get().isEmpty());
    }

    @Test
    public void testListenerCalledWhenChunkAvailable() throws InterruptedException {
        ChunkManagerListener listener = Mockito.mock(ChunkManagerListener.class);

        ChunkManager chunkManager = new ChunkManager();
        chunkManager.addListener(listener);
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(1, 2, 3));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        Optional<Chunk> chunk = chunkManager.getChunk(new Vec3i(1, 2, 3));
        assertTrue(chunk.isPresent());

        // check that the listener is called with the chunk
        Mockito.verify(listener).onChunkAvailable(chunk.get());
    }

    @Test
    public void testListenerCalledWhenChunkUpdated() throws InterruptedException {
        ChunkManagerListener listener = Mockito.mock(ChunkManagerListener.class);

        ChunkManager chunkManager = new ChunkManager();
        chunkManager.addListener(listener);
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(3, 2, 1));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        Optional<Chunk> chunk = chunkManager.getChunk(new Vec3i(3, 2, 1));
        assertTrue(chunk.isPresent());

        chunkManager.requestChunkMeshUpdate(new Vec3i(3, 2, 1));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        // check that the listener is called with the chunk
        Mockito.verify(listener).onChunkUpdated(chunk.get());
    }

    @Test
    public void testRequestUpdateChunkIsAdded() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        Chunk chunk = Chunk.createAt(new Vec3i(3, 3, 3));

        Optional<Chunk> optionalChunk = chunkManager.getChunk(new Vec3i(3, 3, 3));
        assertFalse(optionalChunk.isPresent());

        chunkManager.requestChunkMeshUpdate(chunk);
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        optionalChunk = chunkManager.getChunk(new Vec3i(3, 3, 3));
        assertTrue(optionalChunk.isPresent());
        assertEquals(optionalChunk.get(), chunk);
    }

    @Test
    public void testRemoveChunk() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        Optional<Chunk> optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(optionalChunk.isPresent());

        chunkManager.removeChunk(optionalChunk.get());

        optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertFalse(optionalChunk.isPresent());
    }

    @Test
    public void testRemoveChunkByLocationByLocation() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        Optional<Chunk> optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(optionalChunk.isPresent());

        chunkManager.removeChunk(new Vec3i(0, 0, 0));

        optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertFalse(optionalChunk.isPresent());
    }

}
