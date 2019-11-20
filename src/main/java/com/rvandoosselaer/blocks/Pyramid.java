package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.ToString;

/**
 * A shape implementation for a pyramid.
 *
 * @author rvandoosselaer
 */
@ToString
public class Pyramid implements Shape {

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have only one texture
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        createLeftFace(location, chunkMesh, blockScale, multipleImages);
        createBackFace(location, chunkMesh, blockScale, multipleImages);
        createRightFace(location, chunkMesh, blockScale, multipleImages);
        createFrontFace(location, chunkMesh, blockScale, multipleImages);
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            createBottomFace(location, chunkMesh, blockScale, multipleImages);
        }
    }

    private static void createBottomFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
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
                chunkMesh.getNormals().add(new Vector3f(0.0f, -1.0f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            }
        }
    }

    private void createFrontFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(new Vector3f(0.0f, 0.4472136f, 0.8944272f));
                chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 5f / 6f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
            }
        }
    }

    private static void createRightFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(new Vector3f(0.8944272f, 0.4472136f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, 1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 5f / 6f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
            }
        }
    }

    private static void createBackFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(new Vector3f(0.0f, 0.4472136f, -0.8944272f));
                chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 5f / 6f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
            }
        }
    }

    private static void createLeftFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(new Vector3f(-0.8944272f, 0.4472136f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, -1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 5f / 6f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
            }

        }
    }
}
