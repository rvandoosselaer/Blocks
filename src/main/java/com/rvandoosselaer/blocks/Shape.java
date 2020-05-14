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
     * A helper method to calculate the rotation of the shape from the given direction.
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
     * Calculates the new direction of a face, based on the rotation of the shape. The north face of a shape that is
     * rotated, is not facing north anymore.
     *
     * @param faceDirection the direction of the face
     * @param shapeDirection the direction of the shape
     * @return the new face direction
     */
    static Direction getFaceDirection(Direction faceDirection, Direction shapeDirection) {
        Quaternion shapeRotation = getRotationFromDirection(shapeDirection);
        Vector3f newFaceDirection = shapeRotation.mult(faceDirection.getVector().toVector3f());

        return Direction.fromVector(newFaceDirection);
    }

}
