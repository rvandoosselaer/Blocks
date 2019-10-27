package com.rvandoosselaer.blocks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread safe register for shapes. The register is used so only one instance of a shape is used throughout the Blocks
 * framework.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class ShapeRegistry {

    private final ConcurrentMap<String, Shape> shapeRegistry = new ConcurrentHashMap<>();

    public ShapeRegistry() {
        registerDefaultShapes();
    }

    public Shape register(String name, Shape shape) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name " + name + " specified.");
        }

        shapeRegistry.put(name, shape);
        if (log.isTraceEnabled()) {
            log.trace("Registered shape {} -> {}", name, shape);
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
        register(Shape.CUBE, new Cube());
        register(Shape.PYRAMID, new Pyramid());
        register(Shape.WEDGE_FRONT, new Wedge(Direction.FRONT));
        register(Shape.WEDGE_RIGHT, new Wedge(Direction.RIGHT));
        register(Shape.WEDGE_BACK, new Wedge(Direction.BACK));
        register(Shape.WEDGE_LEFT, new Wedge(Direction.LEFT));
        register(Shape.SLAB, new Slab(0, 1f / 3f));
        register(Shape.DOUBLE_SLAB, new Slab(0, 2f / 3f));
        register(Shape.STAIR_FRONT, new Stair(Direction.FRONT));
        register(Shape.STAIR_RIGHT, new Stair(Direction.RIGHT));
        register(Shape.STAIR_BACK, new Stair(Direction.BACK));
        register(Shape.STAIR_LEFT, new Stair(Direction.LEFT));
    }

}
