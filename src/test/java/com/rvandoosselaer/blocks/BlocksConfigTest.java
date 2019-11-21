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
    public static void setup() {
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
        BlocksConfig.getInstance().setGridSize(new Vec3i(11, 11, 11));
        assertEquals(new Vec3i(11, 11, 11), BlocksConfig.getInstance().getGridSize());
    }

    @Test
    public void testInValidGridSize() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setGridSize(new Vec3i(6, 3, 6)));
        assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setGridSize(null));
    }

    @Test
    public void testValidPhysicsGridSize() {
        BlocksConfig.getInstance().setPhysicsGridSize(new Vec3i(7, 7, 7));
        assertEquals(new Vec3i(7, 7, 7), BlocksConfig.getInstance().getPhysicsGridSize());
    }

    @Test
    public void testInValidPhysicsGridSize() {
        assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setPhysicsGridSize(new Vec3i(4, 4, 4)));
        assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setPhysicsGridSize(null));
    }

}
