package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void testYawRotation() {
        assertEquals(Shape.getYawFromDirection(Direction.SOUTH, Direction.SOUTH), new Quaternion());
        assertEquals(Shape.getYawFromDirection(Direction.NORTH, Direction.SOUTH), new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
        // error when using up/down directions
        assertThrows(IllegalArgumentException.class, () -> Shape.getYawFromDirection(Direction.EAST, Direction.UP));
        assertEquals(Shape.getYawFromDirection(Direction.SOUTH, Direction.EAST), new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y));
        assertEquals(Shape.getYawFromDirection(Direction.NORTH, Direction.EAST), new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        assertEquals(Shape.getYawFromDirection(Direction.EAST, Direction.WEST), new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
        assertEquals(Shape.getYawFromDirection(Direction.WEST, Direction.EAST), new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
    }

}
