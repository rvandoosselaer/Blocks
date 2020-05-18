package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.serialize.BlockDefinition;
import com.rvandoosselaer.blocks.serialize.BlockFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: rvandoosselaer
 */
public class BlockFactoryTest {

    @Test
    public void testBlockCreation() {
        BlockDefinition definition = new BlockDefinition(TypeIds.GRASS, false, true, true)
                .addShape(ShapeIds.CUBE)
                .addShape(ShapeIds.CUBE_DOWN)
                .addShapes(ShapeIds.CUBE_NORTH, ShapeIds.CUBE_EAST, ShapeIds.CUBE_SOUTH, ShapeIds.CUBE_WEST);

        Collection<Block> blocks = BlockFactory.create(definition);

        assertEquals(6, blocks.size());
    }

    @Test
    public void testBlockCreationFromCollection() {
        BlockDefinition cubeDefinition = new BlockDefinition(TypeIds.GRASS, false, true, true)
                .addShapes(ShapeIds.CUBE, ShapeIds.CUBE_DOWN, ShapeIds.CUBE_NORTH, ShapeIds.CUBE_EAST, ShapeIds.CUBE_SOUTH, ShapeIds.CUBE_WEST);
        BlockDefinition pyramidDefinition = new BlockDefinition(TypeIds.BIRCH_LOG, true, false, true)
                .addShapes(ShapeIds.PYRAMID, ShapeIds.PYRAMID_DOWN, ShapeIds.PYRAMID_NORTH, ShapeIds.PYRAMID_EAST, ShapeIds.PYRAMID_SOUTH, ShapeIds.PYRAMID_WEST);

        List<BlockDefinition> definitions = new ArrayList<>();
        definitions.add(cubeDefinition);
        definitions.add(pyramidDefinition);

        Collection<Block> blocks = BlockFactory.create(definitions);

        assertEquals(12, blocks.size());
    }

    @Test
    public void testBlockCreationFromFile() {
        Collection<Block> blocks = BlockFactory.create(BlockFactoryTest.class.getResourceAsStream("/blockDefinitions.yml"));

        assertEquals(8, blocks.size());
    }

    @Test
    public void testDoNotProcessNullShape() {
        BlockDefinition blockDefinition = new BlockDefinition(TypeIds.BIRCH_PLANKS, true, false, false)
                .addShape(null);

        assertEquals(0, BlockFactory.create(blockDefinition).size());

        blockDefinition.addShapes(null);
        assertEquals(0, BlockFactory.create(blockDefinition).size());
    }

}
