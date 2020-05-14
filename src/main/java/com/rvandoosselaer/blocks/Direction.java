package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;

/**
 * An enum holding direction information in a right-handed coordinate system, just as OpenGL.
 *
 * @author rvandoosselaer
 */
@Getter
@ToString
public enum Direction {
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    NORTH(0, 0, -1);

    private final Vec3i vector;

    Direction(int x, int y, int z) {
        this.vector = new Vec3i(x, y, z);
    }

    public static Direction fromVector(@NonNull Vector3f vector3f) {
        return Arrays.stream(Direction.values())
                .filter(direction -> {
                    // we cannot use the vector.equals(other) since we will have rounding issues.
                    // a vector(0, 0.99, 0) should also return Direction.TOP. We use the dot product to compare the 2 vectors
                    float dotProduct = vector3f.normalize().dot(direction.getVector().toVector3f());
                    return dotProduct > 0.95f;
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find direction from vector: " + vector3f));
    }

    public Direction opposite() {
        return Direction.fromVector(getVector().toVector3f().negate());
    }

}
