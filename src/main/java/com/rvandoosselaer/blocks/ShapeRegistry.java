package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.shapes.Cube;
import com.rvandoosselaer.blocks.shapes.Pole;
import com.rvandoosselaer.blocks.shapes.Pyramid;
import com.rvandoosselaer.blocks.shapes.RoundedCube;
import com.rvandoosselaer.blocks.shapes.Slab;
import com.rvandoosselaer.blocks.shapes.Square;
import com.rvandoosselaer.blocks.shapes.Stairs;
import com.rvandoosselaer.blocks.shapes.StairsInnerCorner;
import com.rvandoosselaer.blocks.shapes.StairsOuterCorner;
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
        registerCubes();

        registerPyramids();

        registerWedges();

        registerPoles();

        registerRoundedCubes();

        registerSlabs();

        registerSquares();

        registerStairs();
    }

    private void registerStairs() {
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

        register(ShapeIds.STAIRS_OUTER_CORNER_NORTH, new StairsOuterCorner(Direction.NORTH, false));
        register(ShapeIds.STAIRS_OUTER_CORNER_EAST, new StairsOuterCorner(Direction.EAST, false));
        register(ShapeIds.STAIRS_OUTER_CORNER_SOUTH, new StairsOuterCorner(Direction.SOUTH, false));
        register(ShapeIds.STAIRS_OUTER_CORNER_WEST, new StairsOuterCorner(Direction.WEST, false));
        register(ShapeIds.STAIRS_INVERTED_OUTER_CORNER_NORTH, new StairsOuterCorner(Direction.NORTH, true));
        register(ShapeIds.STAIRS_INVERTED_OUTER_CORNER_EAST, new StairsOuterCorner(Direction.EAST, true));
        register(ShapeIds.STAIRS_INVERTED_OUTER_CORNER_SOUTH, new StairsOuterCorner(Direction.SOUTH, true));
        register(ShapeIds.STAIRS_INVERTED_OUTER_CORNER_WEST, new StairsOuterCorner(Direction.WEST, true));
    }

    private void registerSquares() {
        register(ShapeIds.SQUARE, new Square());
        register(ShapeIds.SQUARE_DOWN, new Square(Direction.DOWN));
        register(ShapeIds.SQUARE_NORTH, new Square(Direction.NORTH));
        register(ShapeIds.SQUARE_EAST, new Square(Direction.EAST));
        register(ShapeIds.SQUARE_SOUTH, new Square(Direction.SOUTH));
        register(ShapeIds.SQUARE_WEST, new Square(Direction.WEST));
    }

    private void registerSlabs() {
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
    }

    private void registerRoundedCubes() {
        register(ShapeIds.ROUNDED_CUBE, new RoundedCube());
        register(ShapeIds.ROUNDED_CUBE_DOWN, new RoundedCube(Direction.DOWN));
        register(ShapeIds.ROUNDED_CUBE_NORTH, new RoundedCube(Direction.NORTH));
        register(ShapeIds.ROUNDED_CUBE_EAST, new RoundedCube(Direction.EAST));
        register(ShapeIds.ROUNDED_CUBE_SOUTH, new RoundedCube(Direction.SOUTH));
        register(ShapeIds.ROUNDED_CUBE_WEST, new RoundedCube(Direction.WEST));
    }

    private void registerPoles() {
        register(ShapeIds.POLE, new Pole(Direction.UP, 0.15f));
        register(ShapeIds.POLE_DOWN, new Pole(Direction.DOWN, 0.15f));
        register(ShapeIds.POLE_NORTH, new Pole(Direction.NORTH, 0.15f));
        register(ShapeIds.POLE_EAST, new Pole(Direction.EAST, 0.15f));
        register(ShapeIds.POLE_SOUTH, new Pole(Direction.SOUTH, 0.15f));
        register(ShapeIds.POLE_WEST, new Pole(Direction.WEST, 0.15f));
    }

    private void registerWedges() {
        register(ShapeIds.WEDGE_NORTH, new Wedge(Direction.NORTH, false));
        register(ShapeIds.WEDGE_EAST, new Wedge(Direction.EAST, false));
        register(ShapeIds.WEDGE_SOUTH, new Wedge(Direction.SOUTH, false));
        register(ShapeIds.WEDGE_WEST, new Wedge(Direction.WEST, false));
        register(ShapeIds.WEDGE_INVERTED_NORTH, new Wedge(Direction.NORTH, true));
        register(ShapeIds.WEDGE_INVERTED_EAST, new Wedge(Direction.EAST, true));
        register(ShapeIds.WEDGE_INVERTED_SOUTH, new Wedge(Direction.SOUTH, true));
        register(ShapeIds.WEDGE_INVERTED_WEST, new Wedge(Direction.WEST, true));
    }

    private void registerPyramids() {
        register(ShapeIds.PYRAMID, new Pyramid());
        register(ShapeIds.PYRAMID_DOWN, new Pyramid(Direction.DOWN));
        register(ShapeIds.PYRAMID_NORTH, new Pyramid(Direction.NORTH));
        register(ShapeIds.PYRAMID_EAST, new Pyramid(Direction.EAST));
        register(ShapeIds.PYRAMID_SOUTH, new Pyramid(Direction.SOUTH));
        register(ShapeIds.PYRAMID_WEST, new Pyramid(Direction.WEST));
    }

    private void registerCubes() {
        register(ShapeIds.CUBE, new Cube());
        register(ShapeIds.CUBE_DOWN, new Cube(Direction.DOWN));
        register(ShapeIds.CUBE_NORTH, new Cube(Direction.NORTH));
        register(ShapeIds.CUBE_EAST, new Cube(Direction.EAST));
        register(ShapeIds.CUBE_SOUTH, new Cube(Direction.SOUTH));
        register(ShapeIds.CUBE_WEST, new Cube(Direction.WEST));
    }

}
