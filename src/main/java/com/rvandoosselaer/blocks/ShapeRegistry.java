package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.shapes.Cube;
import com.rvandoosselaer.blocks.shapes.Pole;
import com.rvandoosselaer.blocks.shapes.Pyramid;
import com.rvandoosselaer.blocks.shapes.RoundedCube;
import com.rvandoosselaer.blocks.shapes.Slab;
import com.rvandoosselaer.blocks.shapes.Square;
import com.rvandoosselaer.blocks.shapes.Stairs;
import com.rvandoosselaer.blocks.shapes.StairsInnerCorner;
import com.rvandoosselaer.blocks.shapes.Wedge;
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
        register(ShapeIds.CUBE_DOWN, new Cube(Direction.DOWN));
        register(ShapeIds.CUBE_NORTH, new Cube(Direction.NORTH));
        register(ShapeIds.CUBE_EAST, new Cube(Direction.EAST));
        register(ShapeIds.CUBE_SOUTH, new Cube(Direction.SOUTH));
        register(ShapeIds.CUBE_WEST, new Cube(Direction.WEST));

        register(ShapeIds.PYRAMID, new Pyramid());
        register(ShapeIds.PYRAMID_DOWN, new Pyramid(Direction.DOWN));
        register(ShapeIds.PYRAMID_NORTH, new Pyramid(Direction.NORTH));
        register(ShapeIds.PYRAMID_EAST, new Pyramid(Direction.EAST));
        register(ShapeIds.PYRAMID_SOUTH, new Pyramid(Direction.SOUTH));
        register(ShapeIds.PYRAMID_WEST, new Pyramid(Direction.WEST));

        register(ShapeIds.WEDGE_NORTH, new Wedge(Direction.NORTH, false));
        register(ShapeIds.WEDGE_EAST, new Wedge(Direction.EAST, false));
        register(ShapeIds.WEDGE_SOUTH, new Wedge(Direction.SOUTH, false));
        register(ShapeIds.WEDGE_WEST, new Wedge(Direction.WEST, false));
        register(ShapeIds.WEDGE_INVERTED_NORTH, new Wedge(Direction.NORTH, true));
        register(ShapeIds.WEDGE_INVERTED_EAST, new Wedge(Direction.EAST, true));
        register(ShapeIds.WEDGE_INVERTED_SOUTH, new Wedge(Direction.SOUTH, true));
        register(ShapeIds.WEDGE_INVERTED_WEST, new Wedge(Direction.WEST, true));

        register(ShapeIds.POLE, new Pole(Direction.UP, 0.15f));
        register(ShapeIds.POLE_DOWN, new Pole(Direction.DOWN, 0.15f));
        register(ShapeIds.POLE_NORTH, new Pole(Direction.NORTH, 0.15f));
        register(ShapeIds.POLE_EAST, new Pole(Direction.EAST, 0.15f));
        register(ShapeIds.POLE_SOUTH, new Pole(Direction.SOUTH, 0.15f));
        register(ShapeIds.POLE_WEST, new Pole(Direction.WEST, 0.15f));

        register(ShapeIds.ROUNDED_CUBE, new RoundedCube());
        register(ShapeIds.ROUNDED_CUBE_DOWN, new RoundedCube(Direction.DOWN));
        register(ShapeIds.ROUNDED_CUBE_NORTH, new RoundedCube(Direction.NORTH));
        register(ShapeIds.ROUNDED_CUBE_EAST, new RoundedCube(Direction.EAST));
        register(ShapeIds.ROUNDED_CUBE_SOUTH, new RoundedCube(Direction.SOUTH));
        register(ShapeIds.ROUNDED_CUBE_WEST, new RoundedCube(Direction.WEST));

        register(ShapeIds.SLAB, new Slab(0, 1f / 3f));
        register(ShapeIds.SLAB_DOWN, new Slab(0, 1f / 3f, Direction.DOWN));
        register(ShapeIds.SLAB_NORTH, new Slab(0, 1f / 3f, Direction.NORTH));
        register(ShapeIds.SLAB_EAST, new Slab(0, 1f / 3f, Direction.EAST));
        register(ShapeIds.SLAB_SOUTH, new Slab(0, 1f / 3f, Direction.SOUTH));
        register(ShapeIds.SLAB_WEST, new Slab(0, 1f / 3f, Direction.WEST));
        register(ShapeIds.DOUBLE_SLAB, new Slab(0, 2f / 3f));
        register(ShapeIds.DOUBLE_SLAB_DOWN, new Slab(0, 2f / 3f, Direction.DOWN));
        register(ShapeIds.DOUBLE_SLAB_NORTH, new Slab(0, 2f / 3f, Direction.NORTH));
        register(ShapeIds.DOUBLE_SLAB_EAST, new Slab(0, 2f / 3f, Direction.EAST));
        register(ShapeIds.DOUBLE_SLAB_SOUTH, new Slab(0, 2f / 3f, Direction.SOUTH));
        register(ShapeIds.DOUBLE_SLAB_WEST, new Slab(0, 2f / 3f, Direction.WEST));

        register(ShapeIds.SQUARE, new Square());
        register(ShapeIds.SQUARE_DOWN, new Square(Direction.DOWN));
        register(ShapeIds.SQUARE_NORTH, new Square(Direction.NORTH));
        register(ShapeIds.SQUARE_EAST, new Square(Direction.EAST));
        register(ShapeIds.SQUARE_SOUTH, new Square(Direction.SOUTH));
        register(ShapeIds.SQUARE_WEST, new Square(Direction.WEST));

        register(ShapeIds.STAIRS_NORTH, new Stairs(Direction.NORTH, false));
        register(ShapeIds.STAIRS_EAST, new Stairs(Direction.EAST, false));
        register(ShapeIds.STAIRS_SOUTH, new Stairs(Direction.SOUTH, false));
        register(ShapeIds.STAIRS_WEST, new Stairs(Direction.WEST, false));
        register(ShapeIds.STAIRS_INVERTED_NORTH, new Stairs(Direction.NORTH, true));
        register(ShapeIds.STAIRS_INVERTED_EAST, new Stairs(Direction.EAST, true));
        register(ShapeIds.STAIRS_INVERTED_SOUTH, new Stairs(Direction.SOUTH, true));
        register(ShapeIds.STAIRS_INVERTED_WEST, new Stairs(Direction.WEST, true));

        register(ShapeIds.STAIRS_INNER_CORNER_NORTH, new StairsInnerCorner(Direction.NORTH, false));
        register(ShapeIds.STAIRS_INNER_CORNER_EAST, new StairsInnerCorner(Direction.EAST, false));
        register(ShapeIds.STAIRS_INNER_CORNER_SOUTH, new StairsInnerCorner(Direction.SOUTH, false));
        register(ShapeIds.STAIRS_INNER_CORNER_WEST, new StairsInnerCorner(Direction.WEST, false));
        register(ShapeIds.STAIRS_INVERTED_INNER_CORNER_NORTH, new StairsInnerCorner(Direction.NORTH, true));
        register(ShapeIds.STAIRS_INVERTED_INNER_CORNER_EAST, new StairsInnerCorner(Direction.EAST, true));
        register(ShapeIds.STAIRS_INVERTED_INNER_CORNER_SOUTH, new StairsInnerCorner(Direction.SOUTH, true));
        register(ShapeIds.STAIRS_INVERTED_INNER_CORNER_WEST, new StairsInnerCorner(Direction.WEST, true));

//        register(ShapeIds.CUBE, new Cube());
//        register(ShapeIds.SLAB, new Slab(0, 1f / 3f));
//        register(ShapeIds.SLAB_TOP, new Slab(2f / 3f, 1));
//        register(ShapeIds.DOUBLE_SLAB, new Slab(0, 2f / 3f));
//        register(ShapeIds.DOUBLE_SLAB_TOP, new Slab(1f / 3f, 1));
//        register(ShapeIds.PLATE, new Slab(0, 0.1f));
//        register(ShapeIds.STAIRS_BACK, new Stair(Direction.NORTH));
//        register(ShapeIds.STAIRS_FRONT, new Stair(Direction.SOUTH));
//        register(ShapeIds.STAIRS_LEFT, new Stair(Direction.WEST));
//        register(ShapeIds.STAIRS_RIGHT, new Stair(Direction.EAST));
//        register(ShapeIds.WEDGE_BACK, new Wedge(Direction.NORTH));
//        register(ShapeIds.WEDGE_FRONT, new Wedge(Direction.SOUTH));
//        register(ShapeIds.WEDGE_LEFT, new Wedge(Direction.WEST));
//        register(ShapeIds.WEDGE_RIGHT, new Wedge(Direction.EAST));
//        register(ShapeIds.PYRAMID, new Pyramid());
//        register(ShapeIds.ROUNDED_CUBE, new RoundedCube());
//        register(ShapeIds.PYRAMID_INVERTED, new InvertedPyramid());
//        register(ShapeIds.STAIRS_INVERTED_BACK, new InvertedStair(Direction.NORTH));
//        register(ShapeIds.STAIRS_INVERTED_FRONT, new InvertedStair(Direction.SOUTH));
//        register(ShapeIds.STAIRS_INVERTED_LEFT, new InvertedStair(Direction.WEST));
//        register(ShapeIds.STAIRS_INVERTED_RIGHT, new InvertedStair(Direction.EAST));
//        register(ShapeIds.WEDGE_INVERTED_BACK, new InvertedWedge(Direction.NORTH));
//        register(ShapeIds.WEDGE_INVERTED_FRONT, new InvertedWedge(Direction.SOUTH));
//        register(ShapeIds.WEDGE_INVERTED_LEFT, new InvertedWedge(Direction.WEST));
//        register(ShapeIds.WEDGE_INVERTED_RIGHT, new InvertedWedge(Direction.EAST));
//        register(ShapeIds.STAIRS_INNER_CORNER_FRONT, new StairInnerCorner(Direction.SOUTH));
//        register(ShapeIds.STAIRS_INNER_CORNER_LEFT, new StairInnerCorner(Direction.WEST));
//        register(ShapeIds.STAIRS_INNER_CORNER_RIGHT, new StairInnerCorner(Direction.EAST));
//        register(ShapeIds.STAIRS_INNER_CORNER_BACK, new StairInnerCorner(Direction.NORTH));
//        register(ShapeIds.STAIRS_OUTER_CORNER_FRONT, new StairOuterCorner(Direction.SOUTH));
//        register(ShapeIds.STAIRS_OUTER_CORNER_LEFT, new StairOuterCorner(Direction.WEST));
//        register(ShapeIds.STAIRS_OUTER_CORNER_RIGHT, new StairOuterCorner(Direction.EAST));
//        register(ShapeIds.STAIRS_OUTER_CORNER_BACK, new StairOuterCorner(Direction.NORTH));
//        register(ShapeIds.SQUARE_BACK, new Square(Direction.NORTH));
//        register(ShapeIds.SQUARE_FRONT, new Square(Direction.SOUTH));
//        register(ShapeIds.SQUARE_LEFT, new Square(Direction.WEST));
//        register(ShapeIds.SQUARE_RIGHT, new Square(Direction.EAST));
//        register(ShapeIds.SQUARE_TOP, new Square(Direction.UP));
//        register(ShapeIds.SQUARE_BOTTOM, new Square(Direction.DOWN));
//        register(ShapeIds.POLE, new Pole());
//        register(ShapeIds.CUBE_UPSIDE_DOWN, new RotatedCube(Direction.DOWN));
//        register(ShapeIds.CUBE_LEFT, new RotatedCube(Direction.WEST));
//        register(ShapeIds.CUBE_RIGHT, new RotatedCube(Direction.EAST));
//        register(ShapeIds.CUBE_FRONT, new RotatedCube(Direction.SOUTH));
//        register(ShapeIds.CUBE_BACK, new RotatedCube(Direction.NORTH));
//        register(ShapeIds.STAIRS_OUTER_CORNER_INVERTED_FRONT, new StairOuterCornerInverted(Direction.SOUTH));
//        register(ShapeIds.STAIRS_OUTER_CORNER_INVERTED_LEFT, new StairOuterCornerInverted(Direction.WEST));
//        register(ShapeIds.STAIRS_OUTER_CORNER_INVERTED_RIGHT, new StairOuterCornerInverted(Direction.EAST));
//        register(ShapeIds.STAIRS_OUTER_CORNER_INVERTED_BACK, new StairOuterCornerInverted(Direction.NORTH));
    }

}
