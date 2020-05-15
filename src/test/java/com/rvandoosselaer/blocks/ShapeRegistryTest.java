package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.rvandoosselaer.blocks.shapes.Slab;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertEquals(size + 1, shapeRegistry.getAll().size());
        assertEquals(shape, shapeRegistry.get("custom-shape"));
    }

    @Test
    public void testRemoveShape() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
        int size = shapeRegistry.getAll().size();

        shapeRegistry.remove(ShapeIds.CUBE);
        assertEquals(size - 1, shapeRegistry.getAll().size());
        assertNull(shapeRegistry.get(ShapeIds.CUBE));
    }

    @AfterAll
    public static void testClear() {
        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
        int size = shapeRegistry.getAll().size();

        assertNotEquals(0, size);
        shapeRegistry.clear();
        size = shapeRegistry.getAll().size();
        assertEquals(0, size);
    }

}
