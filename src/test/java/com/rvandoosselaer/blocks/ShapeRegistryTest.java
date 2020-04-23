package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author: rvandoosselaer
 */
public class ShapeRegistryTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testRegisterShape() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
        int size = shapeRegistry.getAll().size();

        Shape shape = shapeRegistry.register("custom-shape", new Slab(0.3f, 0.6f));
        Assertions.assertEquals(size + 1, shapeRegistry.getAll().size());
        Assertions.assertEquals(shape, shapeRegistry.get("custom-shape"));
    }

    @Test
    public void testGetDefaultShape() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();

        Assertions.assertNotNull(shapeRegistry.get(ShapeIds.CUBE));
    }

    @Test
    public void testRemoveShape() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
        int size = shapeRegistry.getAll().size();

        shapeRegistry.remove(ShapeIds.PYRAMID);
        Assertions.assertEquals(size - 1, shapeRegistry.getAll().size());
        Assertions.assertNull(shapeRegistry.get(ShapeIds.PYRAMID));
    }

    @AfterAll
    public static void testClear() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
        int size = shapeRegistry.getAll().size();

        Assertions.assertNotEquals(0, size);
        shapeRegistry.clear();
        size = shapeRegistry.getAll().size();
        Assertions.assertEquals(0, size);
    }

}
