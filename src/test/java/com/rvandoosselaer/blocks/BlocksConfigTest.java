package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author rvandoosselaer
 */
public class BlocksConfigTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testValidChunkSize() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(3, 3, 3));
        assertEquals(new Vec3i(3, 3, 3), BlocksConfig.getInstance().getChunkSize());
    }

    @Test
    public void testInvalidChunkSize() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setChunkSize(new Vec3i(3, -3, 3)));
        assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setChunkSize(null));
    }

    @Test
    public void testValidBlockScale() {
        BlocksConfig.getInstance().setBlockScale(5f);
        assertEquals(5f, BlocksConfig.getInstance().getBlockScale());
    }

    @Test
    public void testInvalidBlockScale() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setBlockScale(0));
    }

    @Test
    public void testValidGridSize() {
        BlocksConfig.getInstance().setGrid(new Vec3i(11, 11, 11));
        assertEquals(new Vec3i(11, 11, 11), BlocksConfig.getInstance().getGrid());
    }

    @Test
    public void testInValidGridSize() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setGrid(new Vec3i(6, 3, 6)));
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setGrid(new Vec3i(3, 3, -3)));
        assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setGrid(null));
    }

    @Test
    public void testValidPhysicsGridSize() {
        BlocksConfig.getInstance().setPhysicsGrid(new Vec3i(7, 7, 7));
        assertEquals(new Vec3i(7, 7, 7), BlocksConfig.getInstance().getPhysicsGrid());
    }

    @Test
    public void testInValidPhysicsGridSize() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setPhysicsGrid(new Vec3i(4, 4, 4)));
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setPhysicsGrid(new Vec3i(-5, -5, -5)));
        assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setPhysicsGrid(null));
    }

}
