package com.rvandoosselaer.blocks;

/**
 * Contains all the keys of the default cube shape blocks that are registered in the {@link BlockRegistry}. Use these
 * keys to retrieve the blocks from the {@link BlockRegistry#get(String)}. For non-default shapes, use
 * the {@link #getName(String type, String shape)} method to get the name (id) of the block.
 * The convention is that the name of the block is a concatenation of the type and shape with a dash or hyphen in
 * between. When the shape of the block is the default shape (ShapeIds.CUBE) the shape can be left out in the name.
 *
 * Some examples:
 * - block (type: grass, shape: cube)
 *   name: grass
 * - block (type: dirt, shape: cube_west)
 *   name: dirt-cube_west
 * - block (type: mossy_cobblestone, shape: stairs_inverted_inner_corner_east)
 *   name: mossy_cobblestone-stairs_inverted_inner_corner_east
 *
 * @author: rvandoosselaer
 */
public interface BlockIds {

    String NONE = "";
    
    String BIRCH_LOG = TypeIds.BIRCH_LOG;
    String BIRCH_PLANKS = TypeIds.BIRCH_PLANKS;
    String BIRCH_LEAVES = TypeIds.BIRCH_LEAVES;
    
    String BRICKS = TypeIds.BRICKS;
    
    String COBBLESTONE = TypeIds.COBBLESTONE;
    String MOSSY_COBBLESTONE = TypeIds.MOSSY_COBBLESTONE;
    
    String DIRT = TypeIds.DIRT;
    
    String GRAVEL = TypeIds.GRAVEL;
    
    String GRASS = TypeIds.GRASS;
    String GRASS_SNOW = TypeIds.GRASS_SNOW;

    String PALM_TREE_LOG = TypeIds.PALM_TREE_LOG;
    String PALM_TREE_PLANKS = TypeIds.PALM_TREE_PLANKS;
    String PALM_TREE_LEAVES = TypeIds.PALM_TREE_LEAVES;

    String ROCK = TypeIds.ROCK;

    String OAK_LOG = TypeIds.OAK_LOG;
    String OAK_PLANKS = TypeIds.OAK_PLANKS;
    String OAK_LEAVES = TypeIds.OAK_LEAVES;

    String SAND = TypeIds.SAND;
    
    String SNOW = TypeIds.SNOW;

    String SPRUCE_LOG = TypeIds.SPRUCE_LOG;
    String SPRUCE_PLANKS = TypeIds.SPRUCE_PLANKS;
    String SPRUCE_LEAVES = TypeIds.SPRUCE_LEAVES;

    String STONE_BRICKS = TypeIds.STONE_BRICKS;
    String MOSSY_STONE_BRICKS = TypeIds.MOSSY_STONE_BRICKS;

    String WATER = TypeIds.WATER;
    String WATER_STILL = TypeIds.WATER_STILL;

    String WINDOW = TypeIds.WINDOW;

    static String getName(String type, String shape) {
        return ShapeIds.CUBE.equals(shape) ? type : type + "-" + shape;
    }

}
