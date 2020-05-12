package com.rvandoosselaer.blocks;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
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

    public Shape register(@NonNull String name, Shape shape) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Invalid shape name " + name + " specified.");
        }

        shapeRegistry.put(name, shape);
        if (log.isTraceEnabled()) {
            log.trace("Registered shape {} -> {}", name, shape);
        }
        return shape;
    }

    public Shape get(String name) {
        Shape s = shapeRegistry.get(name);
        if (s == null) {
            log.warn("No shape registered with name {}", name);
        }
        return s;
    }

    public boolean remove(@NonNull String name) {
        if (shapeRegistry.containsKey(name)) {
            Shape shape = shapeRegistry.remove(name);
            if (log.isTraceEnabled()) {
                log.trace("Removed shape {} -> {}", name, shape);
            }
            return true;
        }
        return false;
    }

    public void clear() {
        shapeRegistry.clear();
    }

    public Collection<String> getAll() {
        return Collections.unmodifiableCollection(shapeRegistry.keySet());
    }

    public void registerDefaultShapes() {
        register(ShapeIds.CUBE, new Cube());
        register(ShapeIds.SLAB, new Slab(0, 1f / 3f));
        register(ShapeIds.DOUBLE_SLAB, new Slab(0, 2f / 3f));
        register(ShapeIds.PLATE, new Slab(0, 0.1f));
        register(ShapeIds.STAIRS_BACK, new Stair(Direction.BACK));
        register(ShapeIds.STAIRS_FRONT, new Stair(Direction.FRONT));
        register(ShapeIds.STAIRS_LEFT, new Stair(Direction.LEFT));
        register(ShapeIds.STAIRS_RIGHT, new Stair(Direction.RIGHT));
        register(ShapeIds.WEDGE_BACK, new Wedge(Direction.BACK));
        register(ShapeIds.WEDGE_FRONT, new Wedge(Direction.FRONT));
        register(ShapeIds.WEDGE_LEFT, new Wedge(Direction.LEFT));
        register(ShapeIds.WEDGE_RIGHT, new Wedge(Direction.RIGHT));
        register(ShapeIds.PYRAMID, new Pyramid());
        register(ShapeIds.ROUNDED_CUBE, new RoundedCube());
        register(ShapeIds.PYRAMID_INVERTED, new InvertedPyramid());
        register(ShapeIds.STAIRS_INVERTED_BACK, new InvertedStair(Direction.BACK));
        register(ShapeIds.STAIRS_INVERTED_FRONT, new InvertedStair(Direction.FRONT));
        register(ShapeIds.STAIRS_INVERTED_LEFT, new InvertedStair(Direction.LEFT));
        register(ShapeIds.STAIRS_INVERTED_RIGHT, new InvertedStair(Direction.RIGHT));
        register(ShapeIds.WEDGE_INVERTED_BACK, new InvertedWedge(Direction.BACK));
        register(ShapeIds.WEDGE_INVERTED_FRONT, new InvertedWedge(Direction.FRONT));
        register(ShapeIds.WEDGE_INVERTED_LEFT, new InvertedWedge(Direction.LEFT));
        register(ShapeIds.WEDGE_INVERTED_RIGHT, new InvertedWedge(Direction.RIGHT));
        register(ShapeIds.STAIRS_INNER_CORNER_FRONT, new StairInnerCorner(Direction.FRONT));
        register(ShapeIds.STAIRS_INNER_CORNER_LEFT, new StairInnerCorner(Direction.LEFT));
        register(ShapeIds.STAIRS_INNER_CORNER_RIGHT, new StairInnerCorner(Direction.RIGHT));
        register(ShapeIds.STAIRS_INNER_CORNER_BACK, new StairInnerCorner(Direction.BACK));
        register(ShapeIds.STAIRS_OUTER_CORNER_FRONT, new StairOuterCorner(Direction.FRONT));
        register(ShapeIds.STAIRS_OUTER_CORNER_LEFT, new StairOuterCorner(Direction.LEFT));
        register(ShapeIds.STAIRS_OUTER_CORNER_RIGHT, new StairOuterCorner(Direction.RIGHT));
        register(ShapeIds.STAIRS_OUTER_CORNER_BACK, new StairOuterCorner(Direction.BACK));
        register(ShapeIds.SQUARE_BACK, new Square(Direction.BACK));
        register(ShapeIds.SQUARE_FRONT, new Square(Direction.FRONT));
        register(ShapeIds.SQUARE_LEFT, new Square(Direction.LEFT));
        register(ShapeIds.SQUARE_RIGHT, new Square(Direction.RIGHT));
        register(ShapeIds.SQUARE_TOP, new Square(Direction.TOP));
        register(ShapeIds.SQUARE_BOTTOM, new Square(Direction.BOTTOM));
    }

}
