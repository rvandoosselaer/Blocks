package com.rvandoosselaer.blocks;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.rvandoosselaer.blocks.Shape.*;

/**
 * A registry for storing and retrieving shapes based on the block shape.
 * @author remy
 */
@Slf4j
public class ShapeRegistry {

    private final Map<String, Shape> shapeRegistry = new HashMap<>();

    public ShapeRegistry() {
        registerDefaultShapes();
    }

    public Shape register(String name, Shape shape) {
        shapeRegistry.put(name, shape);
        if (log.isTraceEnabled()) {
            log.trace("Registered {} -> {}", name, shape);
        }
        return shape;
    }

    public Shape get(String shape) {
        Shape s = shapeRegistry.get(shape);
        if (s == null) {
            log.warn("No shape registered with name {}", shape);
        }
        return s;
    }

    public void clear() {
        shapeRegistry.clear();
    }

    public void registerDefaultShapes() {
        register(CUBE, CUBE_SHAPE);
        register(PYRAMID, PYRAMID_SHAPE);
        register(CUBOID_ONE_THIRD, CUBOID_ONE_THIRD_SHAPE);
        register(CUBOID_TWO_THIRD, CUBOID_TWO_THIRD_SHAPE);
        register(WEDGE_RIGHT, WEDGE_RIGHT_SHAPE);
        register(WEDGE_FRONT, WEDGE_FRONT_SHAPE);
        register(WEDGE_LEFT, WEDGE_LEFT_SHAPE);
        register(WEDGE_BACK, WEDGE_BACK_SHAPE);
    }

}
