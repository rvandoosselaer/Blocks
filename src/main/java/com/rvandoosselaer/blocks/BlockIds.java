package com.rvandoosselaer.blocks;

/**
 * Contains all the keys of the default cube shape blocks that are registered in the {@link BlockRegistry}. Use these
 * keys to retrieve the blocks from the {@link BlockRegistry#get(String)}. For non-default shapes, use
 * the {@link #getName(String type, String shape)} method to get the name (id) of the block.
 *
 * @author: rvandoosselaer
 */
public interface BlockIds {

    String NONE = "";
    
    String BIRCH_LOG = getName(TypeIds.BIRCH_LOG, ShapeIds.CUBE);
    String BIRCH_PLANKS = getName(TypeIds.BIRCH_PLANKS, ShapeIds.CUBE);
    String BIRCH_LEAVES = getName(TypeIds.BIRCH_LEAVES, ShapeIds.CUBE);
    
    String BRICKS = getName(TypeIds.BRICKS, ShapeIds.CUBE);
    
    String COBBLESTONE = getName(TypeIds.COBBLESTONE, ShapeIds.CUBE);
    String MOSSY_COBBLESTONE = getName(TypeIds.MOSSY_COBBLESTONE, ShapeIds.CUBE);
    
    String DIRT = getName(TypeIds.DIRT, ShapeIds.CUBE);
    
    String GRAVEL = getName(TypeIds.GRAVEL, ShapeIds.CUBE);
    
    String GRASS = getName(TypeIds.GRASS, ShapeIds.CUBE);
    String GRASS_SNOW = getName(TypeIds.GRASS_SNOW, ShapeIds.CUBE);

    String PALM_TREE_LOG = getName(TypeIds.PALM_TREE_LOG, ShapeIds.CUBE);
    String PALM_TREE_PLANKS = getName(TypeIds.PALM_TREE_PLANKS, ShapeIds.CUBE);
    String PALM_TREE_LEAVES = getName(TypeIds.PALM_TREE_LEAVES, ShapeIds.CUBE);

    String ROCK = getName(TypeIds.ROCK, ShapeIds.CUBE);

    String OAK_LOG = getName(TypeIds.OAK_LOG, ShapeIds.CUBE);
    String OAK_PLANKS = getName(TypeIds.OAK_PLANKS, ShapeIds.CUBE);
    String OAK_LEAVES = getName(TypeIds.OAK_LEAVES, ShapeIds.CUBE);

    String SAND = getName(TypeIds.SAND, ShapeIds.CUBE);
    
    String SNOW = getName(TypeIds.SNOW, ShapeIds.CUBE);

    String SPRUCE_LOG = getName(TypeIds.SPRUCE_LOG, ShapeIds.CUBE);
    String SPRUCE_PLANKS = getName(TypeIds.SPRUCE_PLANKS, ShapeIds.CUBE);
    String SPRUCE_LEAVES = getName(TypeIds.SPRUCE_LEAVES, ShapeIds.CUBE);

    String STONE_BRICKS = getName(TypeIds.STONE_BRICKS, ShapeIds.CUBE);
    String MOSSY_STONE_BRICKS = getName(TypeIds.MOSSY_STONE_BRICKS, ShapeIds.CUBE);

    String WATER = getName(TypeIds.WATER, ShapeIds.CUBE);
    String WATER_STILL = getName(TypeIds.WATER_STILL, ShapeIds.CUBE);

    String WINDOW = getName(TypeIds.WINDOW, ShapeIds.CUBE);

    static String getName(String type, String shape) {
        return ShapeIds.CUBE.equals(shape) ? type : type + "-" + shape;
    }

}
