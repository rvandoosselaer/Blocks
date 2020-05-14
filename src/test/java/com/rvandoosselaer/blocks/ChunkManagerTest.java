package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.AtMost;
import org.mockito.internal.verification.Times;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        Vec3i blockLocation = new Vec3i(0, 0, 0);
        assertEquals(new Vector3f(0.5f, 0.5f, 0.5f), ChunkManager.getBlockCenterLocation(blockLocation));

        blockLocation = new Vec3i(-5, 3, 2);
        assertEquals(new Vector3f(-4.5f, 3.5f, 2.5f), ChunkManager.getBlockCenterLocation(blockLocation));
    }

    @Test
    public void testLocationCalculationWithBlockScale() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 16, 16));
        BlocksConfig.getInstance().setBlockScale(2f);

        Vector3f location = new Vector3f(0, 0, 0);
        Vec3i chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(31, 31, 31);
        chunkLocation = ChunkManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        Vec3i blockLocation = new Vec3i(0, 0, 0);
        assertEquals(new Vector3f(1, 1, 1), ChunkManager.getBlockCenterLocation(blockLocation));

        blockLocation = new Vec3i(-4, 3, 5);
        assertEquals(new Vector3f(-7, 7, 11), ChunkManager.getBlockCenterLocation(blockLocation));

        BlocksConfig.getInstance().setBlockScale(1f);
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
    public void testSetChunk() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, Block.create("my-block", "my-shape"));
        chunkManager.setChunk(chunk);

        assertTrue(chunkManager.getChunk(new Vec3i(0, 0, 0)).isPresent());
        assertEquals(chunkManager.getChunk(new Vec3i(0, 0, 0)).get(), chunk);
        assertNotNull(chunkManager.getChunk(new Vec3i(0, 0, 0)).get().getBlock(new Vec3i(0, 0, 0)));

        Chunk otherChunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunkManager.setChunk(otherChunk);

        assertEquals(chunkManager.getChunk(new Vec3i(0, 0, 0)).get(), otherChunk);
        assertNull(chunkManager.getChunk(new Vec3i(0, 0, 0)).get().getBlock(new Vec3i(0, 0, 0)));

        Thread.sleep(100); // wait a bit for clean up operations
        assertNull(chunk.getBlocks()); // old chunk is cleaned up
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

    @Test
    public void testAddBlock() throws InterruptedException {
        BlocksConfig.getInstance().setBlockScale(2f);
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        Optional<Chunk> optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));

        assertTrue(optionalChunk.isPresent());
        assertEquals(optionalChunk.get().getBlock(new Vec3i(0, 0, 0)), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));

        chunkManager.addBlock(new Vector3f(1.8f, 1.8f, 1.8f), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        assertEquals(optionalChunk.get().getBlock(new Vec3i(0, 0, 0)), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));

        BlocksConfig.getInstance().setBlockScale(1f);
    }

    @Test
    public void testRemoveBlock() throws InterruptedException {
        BlocksConfig.getInstance().setBlockScale(2f);
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        Optional<Chunk> optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));

        assertTrue(optionalChunk.isPresent());
        assertEquals(optionalChunk.get().getBlock(new Vec3i(0, 0, 0)), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));

        chunkManager.removeBlock(new Vector3f(1.2f, 1.3f, 1.8f));

        optionalChunk = chunkManager.getChunk(new Vec3i(0, 0, 0));
        assertTrue(optionalChunk.isPresent());
        assertNull(optionalChunk.get().getBlock(new Vec3i(0, 0, 0)));

        BlocksConfig.getInstance().setBlockScale(1f);
    }

    @Test
    public void testGetBlock() throws InterruptedException {
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        Optional<Block> optionalBlock = chunkManager.getBlock(new Vector3f(0.5f, 0.2f, 0.8f));
        assertTrue(optionalBlock.isPresent());
        assertEquals(optionalBlock.get(), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
    }

    @Test
    public void testGetBlockFromCollisionResult() throws InterruptedException {
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        CollisionResult collisionResult = new CollisionResult();
        collisionResult.setContactPoint(new Vector3f(0.8f, 1.0121f, 0.8f));
        collisionResult.setContactNormal(Vector3f.UNIT_Y);

        Optional<Block> optionalBlock = chunkManager.getBlock(collisionResult);
        assertTrue(optionalBlock.isPresent());
        assertEquals(optionalBlock.get(), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
    }

    @Test
    public void testGetNeighbourBlock() throws InterruptedException {
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        Optional<Block> optionalBlock = chunkManager.getNeighbourBlock(new Vector3f(0.5f, 0.9f, 0.1f), Direction.EAST);
        assertFalse(optionalBlock.isPresent());

        optionalBlock = chunkManager.getNeighbourBlock(new Vector3f(1.4f, 0.5f, 0.3f), Direction.WEST);
        assertTrue(optionalBlock.isPresent());
        assertEquals(optionalBlock.get(), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
    }

    @Test
    public void testGetNeighbourBlockFromCollisionResult() throws InterruptedException {
        ChunkManager chunkManager = getChunkManagerWithBlockAtZero();

        CollisionResult collisionResult = new CollisionResult();
        collisionResult.setContactPoint(new Vector3f(0.2f, 1.001f, 0.3f));
        collisionResult.setContactNormal(Vector3f.UNIT_Y.negate());

        Optional<Block> optionalBlock = chunkManager.getNeighbourBlock(collisionResult);
        assertTrue(optionalBlock.isPresent());
        assertEquals(optionalBlock.get(), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));

        collisionResult.setContactPoint(new Vector3f(0.993f, 0.5f, 0.3f));
        collisionResult.setContactNormal(new Vector3f(Vector3f.UNIT_X));

        optionalBlock = chunkManager.getNeighbourBlock(collisionResult);
        assertFalse(optionalBlock.isPresent());
    }

    @Test
    public void testChunkTriggersMeshUpdateOnAdjacentChunks() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.setTriggerAdjacentChunkUpdates(true);
        ChunkManagerListener listener = Mockito.mock(ChunkManagerListener.class);
        chunkManager.addListener(listener);
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.requestChunk(new Vec3i(1, 0, 0));
        chunkManager.requestChunk(new Vec3i(2, 0, 0));
        chunkManager.requestChunk(new Vec3i(0, 0, 1));
        chunkManager.requestChunk(new Vec3i(1, 0, 1));
        chunkManager.requestChunk(new Vec3i(2, 0, 1));
        chunkManager.requestChunk(new Vec3i(0, 0, 2));
        chunkManager.requestChunk(new Vec3i(1, 0, 2));
        chunkManager.requestChunk(new Vec3i(2, 0, 2));

        // make sure we update enough before polling the results
        updateChunkManager(60, chunkManager);

        // a chunk should have been updated at least 2 times, and at most 4 times
        // edge chunks have 2 neighbours, the center chunk has 4 neighbours. We check however with at least 1 invocation
        // as it is possible that the chunk was requested 2 times, but was already in the queue, in this case the listener
        // will only be called once.
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 0)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 0)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 0)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 0)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 0)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 0)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 1)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 1)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 1)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 1)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 1)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 1)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 2)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 2)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 2)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 2)).get());
        Mockito.verify(listener, new AtLeast(1)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 2)).get());
        Mockito.verify(listener, new AtMost(4)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 2)).get());

        Mockito.reset(listener);

        chunkManager.addBlock(new Vector3f(0, 0, 0), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));

        updateChunkManager(3, chunkManager);

        Mockito.verify(listener).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 0)).get());
        Mockito.verify(listener).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 0)).get());
        Mockito.verify(listener).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 1)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 0)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 1)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 1)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(0, 0, 2)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(1, 0, 2)).get());
        Mockito.verify(listener, new Times(0)).onChunkUpdated(chunkManager.getChunk(new Vec3i(2, 0, 2)).get());
    }

    private void updateChunkManager(int times, ChunkManager chunkManager) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            chunkManager.update();
            Thread.sleep(50);
            chunkManager.update();
        }
    }

    private ChunkManager getChunkManagerWithBlockAtZero() throws InterruptedException {
        ChunkManager chunkManager = new ChunkManager();
        chunkManager.initialize();

        chunkManager.requestChunk(new Vec3i(0, 0, 0));
        chunkManager.update();
        Thread.sleep(50);
        chunkManager.update();

        chunkManager.addBlock(new Vector3f(0.7f, 0.9f, 0.3f), BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        return chunkManager;
    }

}
