package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for an inverted wedge.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class InvertedWedge implements Shape {

    private final Direction direction;

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the rotation of the wedge; a front facing wedge will have it's sloping side facing forward (0,0,1)
        Quaternion rotation = new Quaternion().fromAngleAxis(rotationFromDirection(direction), Vector3f.UNIT_Y);
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        createFrontFace(location, chunkMesh, rotation, blockScale, multipleImages);

        if (chunk.isFaceVisible(location, getCorrectedDirection(Direction.LEFT))) {
            createLeftFace(location, chunkMesh, rotation, blockScale, multipleImages);
        }

        if (chunk.isFaceVisible(location, getCorrectedDirection(Direction.RIGHT))) {
            createRightFace(location, chunkMesh, rotation, blockScale, multipleImages);
        }

        if (chunk.isFaceVisible(location, getCorrectedDirection(Direction.BACK))) {
            createBackFace(location, chunkMesh, rotation, blockScale, multipleImages);
        }

        if (chunk.isFaceVisible(location, getCorrectedDirection(Direction.TOP))) {
            createTopFace(location, chunkMesh, rotation, blockScale, multipleImages);
        }
    }

    private static void createTopFace(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 2);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 4; i++) {
                chunkMesh.getNormals().add(new Vector3f(0.0f, 1.0f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
            }
        }
    }

    private static void createBackFace(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, -0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 1);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 4; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.0f, -1.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
            }
        }
    }

    private static void createRightFace(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, 1.0f, -1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
            }
        }
    }

    private static void createLeftFace(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset;
        // calculate index offset, we use this to connect the triangles
        offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
            }
        }
    }

    private static void createFrontFace(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 1.0f, +0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.0f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 1.0f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.0f, -0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 1);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 4; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, -0.70710677f, 0.70710677f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.7071068f, 0.7071068f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
            }
        }
    }

    /**
     * Returns the rotation around the y-axis for the given direction
     *
     * @param direction
     * @return rotation in radians
     */
    private static float rotationFromDirection(Direction direction) {
        switch (direction) {
            case RIGHT:
                return FastMath.HALF_PI;
            case BACK:
                return FastMath.PI;
            case LEFT:
                return -FastMath.HALF_PI;
            default:
                return 0;
        }
    }

    /**
     * Calculates the direction of a side of the wedge, based on the rotation. A wedge facing right, is rotated 90 degrees
     * around the y-axis. The original left side of this wedge is now facing to the front.
     *
     * @param direction
     * @return
     */
    private Direction getCorrectedDirection(Direction direction) {
        Quaternion rotation = new Quaternion().fromAngleAxis(rotationFromDirection(this.direction), Vector3f.UNIT_Y);
        Vector3f correctedDirection = rotation.mult(direction.getVector().toVector3f());

        return Direction.fromVector(correctedDirection);
    }

}
