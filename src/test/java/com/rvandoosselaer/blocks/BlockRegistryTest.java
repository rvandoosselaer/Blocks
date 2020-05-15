package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: rvandoosselaer
 */
public class BlockRegistryTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @AfterEach
    public void reset() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testRegisterBlock() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        int size = blockRegistry.getAll().size();

        Block block = blockRegistry.register(Block.create("test", "custom-type"));
        assertEquals(size + 1, blockRegistry.getAll().size());
        assertEquals(block, blockRegistry.get("test"));
    }

    @Test
    public void testRegisterBlockWithCustomName() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        int size = blockRegistry.getAll().size();

        Block block = blockRegistry.register("custom-block", Block.create("test", "custom-type"));
        assertEquals(size + 1, blockRegistry.getAll().size());
        assertEquals(block, blockRegistry.get("custom-block"));
    }

    @Test
    public void testGetDefaultBlock() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        assertNotNull(blockRegistry.get(BlockIds.WATER));
    }

    @Test
    public void testRemoveBlockByName() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        int size = blockRegistry.getAll().size();

        blockRegistry.remove(BlockIds.BIRCH_LOG);
        assertEquals(size - 1, blockRegistry.getAll().size());
        assertNull(blockRegistry.get(BlockIds.BIRCH_LOG));
    }

    @Test
    public void testRemoveBlock() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        int size = blockRegistry.getAll().size();

        Block block = blockRegistry.get(BlockIds.PALM_TREE_PLANKS);
        blockRegistry.remove(block);
        assertEquals(size - 1, blockRegistry.getAll().size());
        assertNull(blockRegistry.get(BlockIds.PALM_TREE_PLANKS));
    }

    @Test
    public void testLoadBlockFromFile() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        blockRegistry.clear();

        blockRegistry.load(BlockRegistryTest.class.getResourceAsStream("/blocks.yml"));

        Block block = blockRegistry.get("my-block-from-yaml");
        assertNotNull(block);
        assertEquals("grass", block.getType());
        assertEquals("wedge", block.getShape());
        assertFalse(block.isSolid());
        assertTrue(block.isTransparent());
        assertTrue(block.isUsingMultipleImages());

        block = blockRegistry.get("oak_log");
        assertNotNull(block);
        assertEquals(ShapeIds.CUBE, block.getShape());
        assertTrue(block.isSolid());
        assertFalse(block.isTransparent());
        assertFalse(block.isUsingMultipleImages());

        blockRegistry.load(BlockRegistryTest.class.getResourceAsStream("/blocks.json"));

        block = blockRegistry.get("birch_planks-stairs_north");
        assertNotNull(block);
        assertEquals("stairs_north", block.getShape());
    }

    @Test
    public void testRegisterMultipleBlocks() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Block firstBlock = Block.create("a-very-custom-block", TypeIds.GRASS);
        Block secondBlock = Block.create("another-very-custom-block", TypeIds.GRASS);
        blockRegistry.register(firstBlock, secondBlock);

        assertNotNull(blockRegistry.get("a-very-custom-block"));
        assertNotNull(blockRegistry.get("another-very-custom-block"));

        blockRegistry.remove(firstBlock);
        blockRegistry.remove(secondBlock);

        Collection<Block> collection = new HashSet<>();
        collection.add(firstBlock);
        collection.add(secondBlock);

        blockRegistry.register(collection);

        assertNotNull(blockRegistry.get("a-very-custom-block"));
        assertNotNull(blockRegistry.get("another-very-custom-block"));

    }

    @AfterAll
    public static void testClear() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        int size = blockRegistry.getAll().size();

        assertNotEquals(0, size);
        blockRegistry.clear();
        size = blockRegistry.getAll().size();
        assertEquals(0, size);
    }

}
