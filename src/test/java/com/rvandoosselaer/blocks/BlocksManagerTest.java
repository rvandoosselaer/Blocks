package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlocksManagerTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testCacheEviction() {
        BlocksManager blocksManager = new BlocksManager();
        blocksManager.initialize();

        blocksManager.requestChunk(new Vec3i(0, 0, 0));
        blocksManager.update();

        Chunk chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNotNull(chunk);

        blocksManager.invalidateChunk(new Vec3i(0, 0, 0));

        chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNull(chunk);
    }

    @Test
    public void testChunkCleanupAfterCacheEviction() throws InterruptedException {
        BlocksManager blocksManager = new BlocksManager();
        blocksManager.initialize();

        blocksManager.requestChunk(new Vec3i(0, 0, 0));
        blocksManager.update();

        Chunk chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNotNull(chunk);
        assertNotNull(chunk.getBlocks());

        chunk.setNode(new Node("placeholder"));
        chunk.setCollisionMesh(new Mesh());

        assertNotNull(chunk.getNode());
        assertNotNull(chunk.getCollisionMesh());

        blocksManager.invalidateChunk(new Vec3i(0, 0, 0));
        assertFalse(blocksManager.hasChunk(new Vec3i(0, 0, 0)));
        Thread.sleep(500);
        assertNull(chunk.getBlocks());
        assertNull(chunk.getNode());
        assertNull(chunk.getCollisionMesh());
    }

    @Test
    public void testAddBlock() {
        BlocksManager blocksManager = new BlocksManager();
        blocksManager.initialize();

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNull(chunk);

        blocksManager.addBlock(new Vec3i(0, 0, 0), blockRegistry.get("grass"));
        chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNotNull(chunk);

        Block block = chunk.getBlock(new Vec3i(0, 0, 0));
        assertNotNull(block);
    }

    @Test
    public void testRemoveBlock() {
        BlocksManager blocksManager = new BlocksManager();
        blocksManager.initialize();

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        blocksManager.addBlock(new Vec3i(0, 0, 0), blockRegistry.get("grass"));
        assertNotNull(blocksManager.getChunk(new Vec3i(0, 0, 0)).getBlock(new Vec3i(0, 0, 0)));

        Block previousBlock = blocksManager.removeBlock(new Vec3i(0, 0, 0));
        assertEquals(blockRegistry.get("grass"), previousBlock);
        assertNull(blocksManager.getChunk(new Vec3i(0, 0, 0)).getBlock(new Vec3i(0, 0, 0)));

        blocksManager.removeBlock(new Vec3i(0, 0, 0));
    }

    @Test
    public void testRequestChunkWithoutLoaderAndGenerator() {
        BlocksManager blocksManager = new BlocksManager();
        blocksManager.initialize();

        Chunk chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNull(chunk);

        boolean requested = blocksManager.requestChunk(new Vec3i(0, 0, 0));
        assertTrue(requested);

        blocksManager.update(); // load, generation and create step

        chunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNotNull(chunk);
        assertTrue(chunk.isEmpty());
    }

    @Test
    public void testRequestChunkWithLoader() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(1, 2, 3, blockRegistry.get("grass"));
        chunk.update();

        ChunkRepository repository = Mockito.mock(ChunkRepository.class);
        Mockito.when(repository.load(new Vec3i(0, 0, 0))).thenReturn(chunk);

        BlocksManager blocksManager = BlocksManager.builder().chunkRepository(repository).build();
        blocksManager.initialize();

        Chunk loadedChunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNull(loadedChunk);

        boolean requested = blocksManager.requestChunk(new Vec3i(0, 0, 0));
        assertTrue(requested);

        blocksManager.update(); // perform load

        loadedChunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertEquals(chunk, loadedChunk);
    }

    @Test
    public void testRequestChunkWithoutLoaderWithGenerator() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(1, 2, 3, blockRegistry.get("grass"));
        chunk.update();

        ChunkGenerator chunkGenerator = Mockito.mock(ChunkGenerator.class);
        Mockito.when(chunkGenerator.generate(new Vec3i(0, 0, 0))).thenReturn(chunk);

        BlocksManager blocksManager = BlocksManager.builder().chunkGenerator(chunkGenerator).build();
        blocksManager.initialize();

        Chunk generatedChunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertNull(generatedChunk);

        boolean requested = blocksManager.requestChunk(new Vec3i(0, 0, 0));
        assertTrue(requested);

        blocksManager.update(); // perform load and generation

        generatedChunk = blocksManager.getChunk(new Vec3i(0, 0, 0));
        assertEquals(chunk, generatedChunk);
    }

    @Test
    public void testChunkLocationCalculation() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 16, 16));

        Vector3f location = new Vector3f(0, 0, 0);
        Vec3i chunkLocation = BlocksManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(13, 10, 5);
        chunkLocation = BlocksManager.getChunkLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(-5, 3, -9);
        chunkLocation = BlocksManager.getChunkLocation(location);

        assertEquals(new Vec3i(-1, 0, -1), chunkLocation);

        location = new Vector3f(16, 15, 2);
        chunkLocation = BlocksManager.getChunkLocation(location);

        assertEquals(new Vec3i(1, 0, 0), chunkLocation);
    }

    @Test
    public void testPickedBlockLocation() {
        BlocksConfig.getInstance().setBlockScale(1);

        Vector3f block = new Vector3f(1.3f, 0.99999f, -2.84f);
        Vec3i blockLocation = BlocksManager.getPickedBlockLocation(block, Vector3f.UNIT_Y, false);

        assertEquals(new Vec3i(1, 0, -3), blockLocation);

        block = new Vector3f(-13.140036f, 15.920046f, -15.0f);
        blockLocation = BlocksManager.getPickedBlockLocation(block, new Vector3f(0, 0, -1), false);

        assertEquals(new Vec3i(-14, 15, -15), blockLocation);

        block = new Vector3f(-15.554672f, 15.649327f, -13.999999f);
        blockLocation = BlocksManager.getPickedBlockLocation(block, new Vector3f(0, 0, -1), true);

        assertEquals(new Vec3i(-16, 15, -15), blockLocation);

        block = new Vector3f(1.5f, 1.0012f, 0.9997f);
        blockLocation = BlocksManager.getPickedBlockLocation(block, new Vector3f(0, -1, 0), true);

        assertEquals(new Vec3i(1, 0, 0), blockLocation);
    }

}
