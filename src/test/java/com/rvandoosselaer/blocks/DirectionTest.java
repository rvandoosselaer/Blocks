package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

    @Test
    public void testFront() {
        assertEquals(new Vec3i(0, 0, 1), Direction.FRONT.getVector());
        assertEquals(Direction.FRONT, Direction.fromVector(new Vector3f(0, 0, 1)));
        assertEquals(Direction.FRONT, Direction.fromVector(new Vector3f(0, 0, 0.99f)));
    }

    @Test
    public void testRight() {
        assertEquals(new Vec3i(1, 0, 0), Direction.RIGHT.getVector());
        assertEquals(Direction.RIGHT, Direction.fromVector(new Vector3f(1, 0, 0)));
        assertEquals(Direction.RIGHT, Direction.fromVector(new Vector3f(1.0001f, 0, 0)));
    }

    @Test
    public void testBack() {
        assertEquals(new Vec3i(0, 0, -1), Direction.BACK.getVector());
        assertEquals(Direction.BACK, Direction.fromVector(new Vector3f(0, 0, -1)));
        assertEquals(Direction.BACK, Direction.fromVector(new Vector3f(0, 0, -0.999f)));
    }

    @Test
    public void testLeft() {
        assertEquals(new Vec3i(-1, 0, 0), Direction.LEFT.getVector());
        assertEquals(Direction.LEFT, Direction.fromVector(new Vector3f(-1, 0, 0)));
        assertEquals(Direction.LEFT, Direction.fromVector(new Vector3f(-1.001f, 0, 0)));
    }

    @Test
    public void testTop() {
        assertEquals(new Vec3i(0, 1, 0), Direction.TOP.getVector());
        assertEquals(Direction.TOP, Direction.fromVector(new Vector3f(0, 1, 0)));
        assertEquals(Direction.TOP, Direction.fromVector(new Vector3f(0, 1.001f, 0)));
    }

    @Test
    public void testBottom() {
        assertEquals(new Vec3i(0, -1, 0), Direction.BOTTOM.getVector());
        assertEquals(Direction.BOTTOM, Direction.fromVector(new Vector3f(0, -1, 0)));
        assertEquals(Direction.BOTTOM, Direction.fromVector(new Vector3f(0, -0.999f, 0)));
    }

    @Test
    public void testUnknown() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Direction.fromVector(new Vector3f(1, 1, 1)));
    }

    @Test
    public void testOpposites() {
        assertEquals(Direction.TOP.opposite(), Direction.BOTTOM);
        assertEquals(Direction.BOTTOM.opposite(), Direction.TOP);
        assertEquals(Direction.RIGHT.opposite(), Direction.LEFT);
        assertEquals(Direction.LEFT.opposite(), Direction.RIGHT);
        assertEquals(Direction.BACK.opposite(), Direction.FRONT);
        assertEquals(Direction.FRONT.opposite(), Direction.BACK);
    }

}
