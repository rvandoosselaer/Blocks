package com.rvandoosselaer.blocks;

/**
 * Contains all the keys of the shapes that are registered in the {@link ShapeRegistry}. Use these keys to retrieve the
 * shapes from the {@link ShapeRegistry#get(String)}.
 *
 * @author rvandoosselaer
 */
public interface ShapeIds {

    String CUBE = "cube_up";
    String CUBE_DOWN = "cube_down";
    String CUBE_NORTH = "cube_north";
    String CUBE_EAST = "cube_east";
    String CUBE_SOUTH = "cube_south";
    String CUBE_WEST = "cube_west";
    String[] ALL_CUBES = {CUBE, CUBE_DOWN, CUBE_NORTH, CUBE_EAST, CUBE_SOUTH, CUBE_WEST};

    String PYRAMID = "pyramid_up";
    String PYRAMID_DOWN = "pyramid_down";
    String PYRAMID_NORTH = "pyramid_north";
    String PYRAMID_EAST = "pyramid_east";
    String PYRAMID_SOUTH = "pyramid_south";
    String PYRAMID_WEST = "pyramid_west";
    String[] ALL_PYRAMIDS = {PYRAMID, PYRAMID_DOWN, PYRAMID_NORTH, PYRAMID_EAST, PYRAMID_SOUTH, PYRAMID_WEST};

    String WEDGE_NORTH = "wedge_north";
    String WEDGE_EAST = "wedge_east";
    String WEDGE_SOUTH = "wedge_south";
    String WEDGE_WEST = "wedge_west";
    String WEDGE_INVERTED_NORTH = "wedge_inverted_north";
    String WEDGE_INVERTED_EAST = "wedge_inverted_east";
    String WEDGE_INVERTED_SOUTH = "wedge_inverted_south";
    String WEDGE_INVERTED_WEST = "wedge_inverted_west";
    String[] ALL_WEDGES = {WEDGE_NORTH, WEDGE_EAST, WEDGE_SOUTH, WEDGE_WEST, WEDGE_INVERTED_NORTH, WEDGE_INVERTED_EAST,
            WEDGE_INVERTED_SOUTH, WEDGE_INVERTED_WEST};

    String POLE = "pole_up";
    String POLE_DOWN = "pole_down";
    String POLE_NORTH = "pole_north";
    String POLE_EAST = "pole_east";
    String POLE_SOUTH = "pole_south";
    String POLE_WEST = "pole_west";
    String[] ALL_POLES = {POLE, POLE_DOWN, POLE_NORTH, POLE_EAST, POLE_SOUTH, POLE_WEST};

    String ROUNDED_CUBE = "rounded_cube_up";
    String ROUNDED_CUBE_DOWN = "rounded_cube_down";
    String ROUNDED_CUBE_NORTH = "rounded_cube_north";
    String ROUNDED_CUBE_EAST = "rounded_cube_east";
    String ROUNDED_CUBE_SOUTH = "rounded_cube_south";
    String ROUNDED_CUBE_WEST = "rounded_cube_west";
    String[] ALL_ROUNDED_CUBES = {ROUNDED_CUBE, ROUNDED_CUBE_DOWN, ROUNDED_CUBE_NORTH, ROUNDED_CUBE_EAST,
            ROUNDED_CUBE_SOUTH, ROUNDED_CUBE_WEST};

    String SLAB = "slab_up";
    String SLAB_DOWN = "slab_down";
    String SLAB_NORTH = "slab_north";
    String SLAB_EAST = "slab_east";
    String SLAB_SOUTH = "slab_south";
    String SLAB_WEST = "slab_west";
    String[] ALL_SLABS = {SLAB, SLAB_DOWN, SLAB_NORTH, SLAB_EAST, SLAB_SOUTH, SLAB_WEST};

    String DOUBLE_SLAB = "double_slab_up";
    String DOUBLE_SLAB_DOWN = "double_slab_down";
    String DOUBLE_SLAB_NORTH = "double_slab_north";
    String DOUBLE_SLAB_EAST = "double_slab_east";
    String DOUBLE_SLAB_SOUTH = "double_slab_south";
    String DOUBLE_SLAB_WEST = "double_slab_west";
    String[] ALL_DOUBLE_SLABS = {DOUBLE_SLAB, DOUBLE_SLAB_DOWN, DOUBLE_SLAB_NORTH, DOUBLE_SLAB_EAST, DOUBLE_SLAB_SOUTH,
            DOUBLE_SLAB_WEST};

    String PLATE = "plate_up";
    String PLATE_DOWN = "plate_down";
    String PLATE_NORTH = "plate_north";
    String PLATE_EAST = "plate_east";
    String PLATE_SOUTH = "plate_south";
    String PLATE_WEST = "plate_west";
    String[] ALL_PLATES = {PLATE, PLATE_DOWN, PLATE_NORTH, PLATE_EAST, PLATE_SOUTH, PLATE_WEST};

    String SQUARE = "square_up";
    String SQUARE_DOWN = "square_down";
    String SQUARE_NORTH = "square_north";
    String SQUARE_EAST = "square_east";
    String SQUARE_SOUTH = "square_south";
    String SQUARE_WEST = "square_west";
    String[] ALL_SQUARES = {SQUARE, SQUARE_DOWN, SQUARE_NORTH, SQUARE_EAST, SQUARE_SOUTH, SQUARE_WEST};

    String STAIRS_NORTH = "stairs_north";
    String STAIRS_EAST = "stairs_east";
    String STAIRS_SOUTH = "stairs_south";
    String STAIRS_WEST = "stairs_west";
    String STAIRS_INVERTED_NORTH = "stairs_inverted_north";
    String STAIRS_INVERTED_EAST = "stairs_inverted_east";
    String STAIRS_INVERTED_SOUTH = "stairs_inverted_south";
    String STAIRS_INVERTED_WEST = "stairs_inverted_west";

    String STAIRS_INNER_CORNER_NORTH = "stairs_inner_corner_north";
    String STAIRS_INNER_CORNER_EAST = "stairs_inner_corner_east";
    String STAIRS_INNER_CORNER_SOUTH = "stairs_inner_corner_south";
    String STAIRS_INNER_CORNER_WEST = "stairs_inner_corner_west";
    String STAIRS_INVERTED_INNER_CORNER_NORTH = "stairs_inverted_inner_corner_north";
    String STAIRS_INVERTED_INNER_CORNER_EAST = "stairs_inverted_inner_corner_east";
    String STAIRS_INVERTED_INNER_CORNER_SOUTH = "stairs_inverted_inner_corner_south";
    String STAIRS_INVERTED_INNER_CORNER_WEST = "stairs_inverted_inner_corner_west";

    String STAIRS_OUTER_CORNER_NORTH = "stairs_outer_corner_north";
    String STAIRS_OUTER_CORNER_EAST = "stairs_outer_corner_east";
    String STAIRS_OUTER_CORNER_SOUTH = "stairs_outer_corner_south";
    String STAIRS_OUTER_CORNER_WEST = "stairs_outer_corner_west";
    String STAIRS_INVERTED_OUTER_CORNER_NORTH = "stairs_inverted_outer_corner_north";
    String STAIRS_INVERTED_OUTER_CORNER_EAST = "stairs_inverted_outer_corner_east";
    String STAIRS_INVERTED_OUTER_CORNER_SOUTH = "stairs_inverted_outer_corner_south";
    String STAIRS_INVERTED_OUTER_CORNER_WEST = "stairs_inverted_outer_corner_west";

    String[] ALL_STAIRS = {STAIRS_NORTH, STAIRS_EAST, STAIRS_SOUTH, STAIRS_WEST,
            STAIRS_INVERTED_NORTH, STAIRS_INVERTED_EAST, STAIRS_INVERTED_SOUTH, STAIRS_INVERTED_WEST,
            STAIRS_INNER_CORNER_NORTH, STAIRS_INNER_CORNER_EAST, STAIRS_INNER_CORNER_SOUTH, STAIRS_INNER_CORNER_WEST,
            STAIRS_INVERTED_INNER_CORNER_NORTH, STAIRS_INVERTED_INNER_CORNER_EAST, STAIRS_INVERTED_INNER_CORNER_SOUTH, STAIRS_INVERTED_INNER_CORNER_WEST,
            STAIRS_OUTER_CORNER_NORTH, STAIRS_OUTER_CORNER_EAST, STAIRS_OUTER_CORNER_SOUTH, STAIRS_OUTER_CORNER_WEST,
            STAIRS_INVERTED_OUTER_CORNER_NORTH, STAIRS_INVERTED_OUTER_CORNER_EAST, STAIRS_INVERTED_OUTER_CORNER_SOUTH, STAIRS_INVERTED_OUTER_CORNER_WEST};

    /**
     * This is a special shape. Although it is not a cube, it is considered as a cube in the visible face check
     * algorithm of a chunk. Blocks with this shape that are next to each other will not render the shared face between
     * them, even if the 'shared' faces are not connected.
     */
    String SQUARE_CUBOID_NINE_TENTHS = "square_cuboid_nine_tenths_up";

    String CYLINDER_UP = "cylinder_up";
    String CYLINDER_DOWN = "cylinder_down";
    String CYLINDER_NORTH = "cylinder_north";
    String CYLINDER_SOUTH = "cylinder_south";
    String CYLINDER_EAST = "cylinder_east";
    String CYLINDER_WEST = "cylinder_west";
    String[] ALL_CYLINDERS = { CYLINDER_UP, CYLINDER_DOWN, CYLINDER_NORTH, CYLINDER_SOUTH, CYLINDER_EAST, CYLINDER_WEST };
}
