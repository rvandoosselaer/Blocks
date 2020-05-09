package com.rvandoosselaer.blocks;

/**
 * Contains all the keys of the shapes that are registered in the {@link ShapeRegistry}. Use these keys to retrieve the
 * shapes from the {@link ShapeRegistry#get(String)}.
 *
 * @author: rvandoosselaer
 */
public interface ShapeIds {

    String CUBE = "cube";
    String SLAB = "slab";
    String DOUBLE_SLAB = "double-slab";
    String PLATE = "plate";
    String STAIRS_BACK = "stairs-back";
    String STAIRS_FRONT = "stairs-front";
    String STAIRS_LEFT = "stairs-left";
    String STAIRS_RIGHT = "stairs-right";
    String WEDGE_BACK = "wedge-back";
    String WEDGE_FRONT = "wedge-front";
    String WEDGE_LEFT = "wedge-left";
    String WEDGE_RIGHT = "wedge-right";
    String PYRAMID = "pyramid";
    String ROUNDED_CUBE = "rounded-cube";
    String PYRAMID_INVERTED = "pyramid-inverted";

}
