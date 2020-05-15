package com.rvandoosselaer.blocks;

/**
 * Contains all the keys of the shapes that are registered in the {@link ShapeRegistry}. Use these keys to retrieve the
 * shapes from the {@link ShapeRegistry#get(String)}.
 *
 * @author: rvandoosselaer
 */
public interface ShapeIds {

    String CUBE = "cube_up";
    String CUBE_DOWN = "cube_down";
    String CUBE_NORTH = "cube_north";
    String CUBE_EAST = "cube_east";
    String CUBE_SOUTH = "cube_south";
    String CUBE_WEST = "cube_west";

    String PYRAMID = "pyramid_up";
    String PYRAMID_DOWN = "pyramid_down";
    String PYRAMID_NORTH = "pyramid_north";
    String PYRAMID_EAST = "pyramid_east";
    String PYRAMID_SOUTH = "pyramid_south";
    String PYRAMID_WEST = "pyramid_west";

    String WEDGE_NORTH = "wedge_north";
    String WEDGE_EAST = "wedge_east";
    String WEDGE_SOUTH = "wedge_south";
    String WEDGE_WEST = "wedge_west";
    String WEDGE_INVERTED_NORTH = "wedge_inverted_north";
    String WEDGE_INVERTED_EAST = "wedge_inverted_east";
    String WEDGE_INVERTED_SOUTH = "wedge_inverted_south";
    String WEDGE_INVERTED_WEST = "wedge_inverted_west";

    String POLE = "pole_up";
    String POLE_DOWN = "pole_down";
    String POLE_NORTH = "pole_north";
    String POLE_EAST = "pole_east";
    String POLE_SOUTH = "pole_south";
    String POLE_WEST = "pole_west";

    String ROUNDED_CUBE = "rounded_cube_up";
    String ROUNDED_CUBE_DOWN = "rounded_cube_down";
    String ROUNDED_CUBE_NORTH = "rounded_cube_north";
    String ROUNDED_CUBE_EAST = "rounded_cube_east";
    String ROUNDED_CUBE_SOUTH = "rounded_cube_south";
    String ROUNDED_CUBE_WEST = "rounded_cube_west";

    String SLAB = "slab_up";
    String SLAB_DOWN = "slab_down";
    String SLAB_NORTH = "slab_north";
    String SLAB_EAST = "slab_east";
    String SLAB_SOUTH = "slab_south";
    String SLAB_WEST = "slab_west";
    String DOUBLE_SLAB = "double_slab_up";
    String DOUBLE_SLAB_DOWN = "double_slab_down";
    String DOUBLE_SLAB_NORTH = "double_slab_north";
    String DOUBLE_SLAB_EAST = "double_slab_east";
    String DOUBLE_SLAB_SOUTH = "double_slab_south";
    String DOUBLE_SLAB_WEST = "double_slab_west";

    String SQUARE = "square_up";
    String SQUARE_DOWN = "square_down";
    String SQUARE_NORTH = "square_north";
    String SQUARE_EAST = "square_east";
    String SQUARE_SOUTH = "square_south";
    String SQUARE_WEST = "square_west";

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

//    String SLAB = "slab";
//    String SLAB_TOP = "slab-top";
//    String DOUBLE_SLAB = "double-slab";
//    String DOUBLE_SLAB_TOP = "double-slab-top";
    String PLATE = "plate";
    String STAIRS_BACK = "stairs-back";
    String STAIRS_FRONT = "stairs-front";
    String STAIRS_LEFT = "stairs-left";
    String STAIRS_RIGHT = "stairs-right";
//    String WEDGE_BACK = "wedge-back";
//    String WEDGE_FRONT = "wedge-front";
//    String WEDGE_LEFT = "wedge-left";
//    String WEDGE_RIGHT = "wedge-right";
//    String ROUNDED_CUBE = "rounded-cube";
    String STAIRS_INVERTED_BACK = "stairs-inverted-back";
    String STAIRS_INVERTED_FRONT = "stairs-inverted-front";
    String STAIRS_INVERTED_LEFT = "stairs-inverted-left";
    String STAIRS_INVERTED_RIGHT = "stairs-inverted-right";
//    String WEDGE_INVERTED_BACK = "wedge-inverted-back";
//    String WEDGE_INVERTED_FRONT = "wedge-inverted-front";
//    String WEDGE_INVERTED_LEFT = "wedge-inverted-left";
//    String WEDGE_INVERTED_RIGHT = "wedge-inverted-right";
    String STAIRS_INNER_CORNER_FRONT = "stairs-inner-front";
    String STAIRS_INNER_CORNER_LEFT = "stairs-inner-left";
    String STAIRS_INNER_CORNER_RIGHT = "stairs-inner-right";
    String STAIRS_INNER_CORNER_BACK = "stairs-inner-back";
    String STAIRS_OUTER_CORNER_FRONT = "stairs-outer-front";
    String STAIRS_OUTER_CORNER_INVERTED_FRONT = "stairs-outer-inverted-front";
    String STAIRS_OUTER_CORNER_LEFT = "stairs-outer-left";
    String STAIRS_OUTER_CORNER_INVERTED_LEFT = "stairs-outer-inverted-left";
    String STAIRS_OUTER_CORNER_RIGHT = "stairs-outer-right";
    String STAIRS_OUTER_CORNER_INVERTED_RIGHT = "stairs-outer-inverted-right";
    String STAIRS_OUTER_CORNER_BACK = "stairs-outer-back";
    String STAIRS_OUTER_CORNER_INVERTED_BACK = "stairs-outer-inverted-back";
    String SQUARE_FRONT = "square-front";
    String SQUARE_BACK = "square-back";
    String SQUARE_LEFT = "square-left";
    String SQUARE_RIGHT = "square-right";
    String SQUARE_TOP = "square-top";
    String SQUARE_BOTTOM = "square-bottom";
//    String POLE = "pole";

}
