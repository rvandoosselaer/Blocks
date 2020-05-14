package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectionTest {

    @Test
    public void testFront() {
        assertEquals(new Vec3i(0, 0, 1), Direction.SOUTH.getVector());
        assertEquals(Direction.SOUTH, Direction.fromVector(new Vector3f(0, 0, 1)));
        assertEquals(Direction.SOUTH, Direction.fromVector(new Vector3f(0, 0, 0.99f)));
    }

    @Test
    public void testRight() {
        assertEquals(new Vec3i(1, 0, 0), Direction.EAST.getVector());
        assertEquals(Direction.EAST, Direction.fromVector(new Vector3f(1, 0, 0)));
        assertEquals(Direction.EAST, Direction.fromVector(new Vector3f(1.0001f, 0, 0)));
    }

    @Test
    public void testBack() {
        assertEquals(new Vec3i(0, 0, -1), Direction.NORTH.getVector());
        assertEquals(Direction.NORTH, Direction.fromVector(new Vector3f(0, 0, -1)));
        assertEquals(Direction.NORTH, Direction.fromVector(new Vector3f(0, 0, -0.999f)));
    }

    @Test
    public void testLeft() {
        assertEquals(new Vec3i(-1, 0, 0), Direction.WEST.getVector());
        assertEquals(Direction.WEST, Direction.fromVector(new Vector3f(-1, 0, 0)));
        assertEquals(Direction.WEST, Direction.fromVector(new Vector3f(-1.001f, 0, 0)));
    }

    @Test
    public void testTop() {
        assertEquals(new Vec3i(0, 1, 0), Direction.UP.getVector());
        assertEquals(Direction.UP, Direction.fromVector(new Vector3f(0, 1, 0)));
        assertEquals(Direction.UP, Direction.fromVector(new Vector3f(0, 1.001f, 0)));
    }

    @Test
    public void testBottom() {
        assertEquals(new Vec3i(0, -1, 0), Direction.DOWN.getVector());
        assertEquals(Direction.DOWN, Direction.fromVector(new Vector3f(0, -1, 0)));
        assertEquals(Direction.DOWN, Direction.fromVector(new Vector3f(0, -0.999f, 0)));
    }

    @Test
    public void testUnknown() {
        assertThrows(IllegalArgumentException.class, () -> Direction.fromVector(new Vector3f(1, 1, 1)));
    }

    @Test
    public void testOpposites() {
        assertEquals(Direction.UP.opposite(), Direction.DOWN);
        assertEquals(Direction.DOWN.opposite(), Direction.UP);
        assertEquals(Direction.EAST.opposite(), Direction.WEST);
        assertEquals(Direction.WEST.opposite(), Direction.EAST);
        assertEquals(Direction.NORTH.opposite(), Direction.SOUTH);
        assertEquals(Direction.SOUTH.opposite(), Direction.NORTH);
    }

}
