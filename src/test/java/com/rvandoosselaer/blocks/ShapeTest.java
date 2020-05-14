package com.rvandoosselaer.blocks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: rvandoosselaer
 */
public class ShapeTest {

    @Test
    public void testFaceDirectionCalculation() {
        Direction faceDirection = Direction.UP;
        Direction shapeDirection = Direction.UP;

        assertEquals(Shape.getFaceDirection(faceDirection, shapeDirection), Direction.UP);

        faceDirection = Direction.UP;
        shapeDirection = Direction.NORTH;
        assertEquals(Shape.getFaceDirection(faceDirection, shapeDirection), Direction.NORTH);

        faceDirection = Direction.DOWN;
        shapeDirection = Direction.EAST;
        assertEquals(Shape.getFaceDirection(faceDirection, shapeDirection), Direction.WEST);

        faceDirection = Direction.WEST;
        shapeDirection = Direction.DOWN;
        assertEquals(Shape.getFaceDirection(faceDirection, shapeDirection), Direction.WEST);
    }

}
