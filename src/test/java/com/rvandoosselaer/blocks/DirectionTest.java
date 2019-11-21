package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DirectionTest {

    @Test
    public void testFront() {
        Assertions.assertEquals(new Vec3i(0, 0, 1), Direction.FRONT.getVector());
        Assertions.assertEquals(Direction.FRONT, Direction.fromVector(new Vector3f(0, 0, 1)));
        Assertions.assertEquals(Direction.FRONT, Direction.fromVector(new Vector3f(0, 0, 0.99f)));

    }

    @Test
    public void assertRight() {
        Assertions.assertEquals(new Vec3i(1, 0, 0), Direction.RIGHT.getVector());
        Assertions.assertEquals(Direction.RIGHT, Direction.fromVector(new Vector3f(1, 0, 0)));
        Assertions.assertEquals(Direction.RIGHT, Direction.fromVector(new Vector3f(1.0001f, 0, 0)));
    }

    @Test
    public void assertBack() {
        Assertions.assertEquals(new Vec3i(0, 0, -1), Direction.BACK.getVector());
        Assertions.assertEquals(Direction.BACK, Direction.fromVector(new Vector3f(0, 0, -1)));
        Assertions.assertEquals(Direction.BACK, Direction.fromVector(new Vector3f(0, 0, -0.999f)));
    }

    @Test
    public void assertLeft() {
        Assertions.assertEquals(new Vec3i(-1, 0, 0), Direction.LEFT.getVector());
        Assertions.assertEquals(Direction.LEFT, Direction.fromVector(new Vector3f(-1, 0, 0)));
        Assertions.assertEquals(Direction.LEFT, Direction.fromVector(new Vector3f(-1.001f, 0, 0)));
    }

    @Test
    public void assertTop() {
        Assertions.assertEquals(new Vec3i(0, 1, 0), Direction.TOP.getVector());
        Assertions.assertEquals(Direction.TOP, Direction.fromVector(new Vector3f(0, 1, 0)));
        Assertions.assertEquals(Direction.TOP, Direction.fromVector(new Vector3f(0, 1.001f, 0)));
    }

    @Test
    public void assertBottom() {
        Assertions.assertEquals(new Vec3i(0, -1, 0), Direction.BOTTOM.getVector());
        Assertions.assertEquals(Direction.BOTTOM, Direction.fromVector(new Vector3f(0, -1, 0)));
        Assertions.assertEquals(Direction.BOTTOM, Direction.fromVector(new Vector3f(0, -0.999f, 0)));
    }

    @Test
    public void assertUnknown() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Direction.fromVector(new Vector3f(1, 1, 1)));
    }

    @Test
    public void assertOpposites() {
        Assertions.assertEquals(Direction.TOP.opposite(), Direction.BOTTOM);
        Assertions.assertEquals(Direction.BOTTOM.opposite(), Direction.TOP);
        Assertions.assertEquals(Direction.RIGHT.opposite(), Direction.LEFT);
        Assertions.assertEquals(Direction.LEFT.opposite(), Direction.RIGHT);
        Assertions.assertEquals(Direction.BACK.opposite(), Direction.FRONT);
        Assertions.assertEquals(Direction.FRONT.opposite(), Direction.BACK);
    }

}
