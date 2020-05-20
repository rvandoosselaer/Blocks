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
        switch (direction) {
            case NORTH:
                return new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);
            case EAST:
                return new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
            case WEST:
                return new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y);
            default:
                return new Quaternion();
        }
    }

    /**
     * Calculates the new direction of a face, based on the rotation of the shape. eg. The north face of a shape that is
     * rotated, is not facing north anymore.
     *
     * @param faceDirection  the original direction of the face
     * @param shapeDirection the direction of the shape
     * @return the new direction of the face based on the direction of the shape
     */
    static Direction getFaceDirection(Direction faceDirection, Direction shapeDirection) {
        Quaternion shapeRotation = getRotationFromDirection(shapeDirection);
        Vector3f newFaceDirection = shapeRotation.mult(faceDirection.getVector().toVector3f());

        return Direction.fromVector(newFaceDirection);
    }

    /**
     * Calculates the new yaw direction (rotation around the y-axis) of a face, based on the yaw rotation of the shape.
     * This should be used for shapes that have a fixed upwards position.
     *
     * @param faceDirection  the original direction of the face
     * @param shapeDirection the direction of the shape
     * @return the new direction of the face based on the direction of the shape
     */
    static Direction getYawFaceDirection(Direction faceDirection, Direction shapeDirection) {
        Quaternion shapeRotation = getYawFromDirection(shapeDirection);
        Vector3f newFaceDirection = shapeRotation.mult(faceDirection.getVector().toVector3f());

        return Direction.fromVector(newFaceDirection);
    }

}
