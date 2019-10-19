package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author rvandoosselaer
 */
public class BlocksConfigTest {

    @Test
    public void testValidChunkSize() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(3, 3, 3));
        Assertions.assertEquals(new Vec3i(3, 3, 3), BlocksConfig.getInstance().getChunkSize());
    }

    @Test
    public void testInvalidChunkSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setChunkSize(new Vec3i(3, -3, 3)));
        Assertions.assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setChunkSize(null));
    }

    @Test
    public void testValidBlockScale() {
        BlocksConfig.getInstance().setBlockScale(5f);
        Assertions.assertEquals(5f, BlocksConfig.getInstance().getBlockScale());
    }

    @Test
    public void testInvalidBlockScale() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setBlockScale(0));
    }

    @Test
    public void testValidGridSize() {
        BlocksConfig.getInstance().setGridSize(new Vec3i(11, 11, 11));
        Assertions.assertEquals(new Vec3i(11, 11, 11), BlocksConfig.getInstance().getGridSize());
    }

    @Test
    public void testInValidGridSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setGridSize(new Vec3i(6, 3, 6)));
        Assertions.assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setGridSize(null));
    }

    @Test
    public void testValidPhysicsGridSize() {
        BlocksConfig.getInstance().setPhysicsGridSize(new Vec3i(7, 7, 7));
        Assertions.assertEquals(new Vec3i(7, 7, 7), BlocksConfig.getInstance().getPhysicsGridSize());
    }

    @Test
    public void testInValidPhysicsGridSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setPhysicsGridSize(new Vec3i(4, 4, 4)));
        Assertions.assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setPhysicsGridSize(null));
    }

    @Test
    public void testValidThemesFolder() {
        BlocksConfig.getInstance().setThemesFolder(System.getProperty("user.home"));
        Assertions.assertEquals(System.getProperty("user.home"), BlocksConfig.getInstance().getThemesFolder());
    }

    @Test
    public void testInvalidThemesfolder() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> BlocksConfig.getInstance().setThemesFolder("/a/path"));
        Assertions.assertThrows(NullPointerException.class, () -> BlocksConfig.getInstance().setThemesFolder(null));
    }
}
