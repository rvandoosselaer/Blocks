package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.ToString;

/**
 * A shape implementation for a pyramid.
 *
 * @author remy
 */
@ToString
public class Pyramid implements Shape {

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have only one texture
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // left
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.0f, 1.0f, 0.0f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.5f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.5f));
            }

        }

        // back
        offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.0f, 1.0f, 0.0f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.5f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.5f));
            }
        }

        // right
        offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.0f, 1.0f, 0.0f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.5f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.5f));
            }
        }

        // front
        offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.0f, 1.0f, 0.0f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.5f));
                chunkMesh.getUvs().add(new Vector2f(0.5f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.5f));
            }
        }

        // bottom
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.5f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.5f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                }
            }

        }
    }
}
