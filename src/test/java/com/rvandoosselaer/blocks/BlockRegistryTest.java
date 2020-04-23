package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author: rvandoosselaer
 */
public class BlockRegistryTest {

    @BeforeAll
    public static void setUp() {
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
