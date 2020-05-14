package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;

/**
 * The interface describing the shape of a {@link Block} element. The {@link #add(Vec3i, Chunk, ChunkMesh)} method is
 * called for each block in the chunk when the mesh is constructed using the {@link ChunkMeshGenerator}.
 *
 * @author rvandoosselaer
 */
public interface Shape {

    /**
     * Adds the shape at the location in the chunk to the chunk mesh.
     *
     * @param location  of the shape in the chunk
     * @param chunk     of the shape
     * @param chunkMesh to add the shape to
     */
    void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh);

    /**
     * A helper method that offsets a vertex based on the location of the block in the chunk and the block scale.
     *
     * @param vertex
     * @param blockLocation
     * @param blockScale
     * @return the same vertex with an offset
     */
    static Vector3f createVertex(Vector3f vertex, Vec3i blockLocation, float blockScale) {
        return vertex.addLocal(blockLocation.x, blockLocation.y, blockLocation.z).multLocal(blockScale);
    }

    /**
     * A helper method to calculate the rotation of the shape from the given direction. Depending on the direction,
     * the rotation is done around a different axis. This has the effect of 'pushing' a shape over to the direction.
     * This should be used for shapes that don't have a fixed upward position.
     * The default direction is UP. All rotation calculations are relative to the UP direction.
     *
     * @param direction the shape is facing
     * @return the rotation to face the direction
     */
    static Quaternion getRotationFromDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                return new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_X);
            case EAST:
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Z);
            case WEST:
                return new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
            case NORTH:
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X);
            case SOUTH:
                return new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
            default:
                return new Quaternion();
        }
    }

    /**
     * A helper method to calculate the yaw rotation (rotation around the y-axis) of the shape for the given direction.
     * This should be used for shapes that have a fixed upwards position.
     * The default direction is SOUTH. All rotation calculations are relative to the SOUTH direction.
     *
     * @param direction the shape is facing
     * @return the rotation to face the direction
     */
    static Quaternion getYawFromDirection(Direction direction) {
        return getYawFromDirection(direction, Direction.SOUTH);
    }

    static Quaternion getYawFromDirection(Direction direction, Direction startDirection) {
        if (direction == Direction.DOWN || direction == Direction.UP || startDirection == Direction.DOWN || startDirection == Direction.UP) {
            throw new IllegalArgumentException("Unable to rotate from " + startDirection + " to " + direction + " using only yaw (y-axis) rotation!");
        }
        // logic:
        // dot = 1 -> no rotation: we are facing in the same direction
        // dot = -1 -> 180° (PI) rotation: we are facing in the opposite direction
        // dot = 0 -> 90° or -90° (PI/2) rotation: we are facing in the perpendicular direction. Use the cross product
        //                                         to find out if we need to rotate 90° or -90°.
        float dot = direction.getVector().toVector3f().dot(startDirection.getVector().toVector3f());
        if (dot >= 0.95f) {
            return new Quaternion();
        }

        if (dot <= -0.95f) {
            return new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
        }

        if (dot >= -0.05f && dot <= 0.05f) {
            Vector3f cross = direction.getVector().toVector3f().cross(startDirection.getVector().toVector3f());
            if (cross.y < 0) {
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y);
            }
            return new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
        }

        throw new IllegalArgumentException("Unable to determine yaw rotation from " + startDirection + " to " + direction);
    }

    /**
     * Calculates the new direction of a face, based on the rotation of the shape. The north face of a shape that is
     * rotated, is not facing north anymore.
     *
     * @param faceDirection  the direction of the face
     * @param shapeDirection the direction of the shape
     * @return the new face direction
     */
    static Direction getFaceDirection(Direction faceDirection, Direction shapeDirection) {
        Quaternion shapeRotation = getRotationFromDirection(shapeDirection);
        Vector3f newFaceDirection = shapeRotation.mult(faceDirection.getVector().toVector3f());

        return Direction.fromVector(newFaceDirection);
    }

}
