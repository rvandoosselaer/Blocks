package com.rvandoosselaer.blocks;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: rvandoosselaer
 */
public class BlockFactoryTest {

    @Test
    public void testBlockCreation() {
        BlockFactory.BlockDefinition definition = new BlockFactory.BlockDefinition(TypeIds.GRASS, false, true, true)
                .addShape(ShapeIds.CUBE)
                .addShape(ShapeIds.CUBE_DOWN)
                .addShapes(ShapeIds.CUBE_NORTH, ShapeIds.CUBE_EAST, ShapeIds.CUBE_SOUTH, ShapeIds.CUBE_WEST);

        Collection<Block> blocks = BlockFactory.create(definition);

        assertEquals(6, blocks.size());
    }

}
