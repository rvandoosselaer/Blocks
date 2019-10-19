package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for a wedge.
 *
 * @author remy
 */
@ToString
@RequiredArgsConstructor
public class Wedge implements Shape {

    private final Direction direction;

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        switch (direction) {
            case RIGHT:
                createRightFacing(location, chunk, chunkMesh);
                break;
            case FRONT:
                createFrontFacing(location, chunk, chunkMesh);
                break;
            case LEFT:
                createLeftFacing(location, chunk, chunkMesh);
                break;
            case BACK:
                createBackFacing(location, chunk, chunkMesh);
                break;
        }
    }

    private void createRightFacing(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // right
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getNormals().add(new Vector3f(0.70710677f, 0.70710677f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(0.70710677f, -0.70710677f, 0.0f, 1.0f));
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

        // left
        if (chunk.isFaceVisible(location, Direction.LEFT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(-1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, -1.0f));
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

        // back
        if (chunk.isFaceVisible(location, Direction.BACK)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, -1.0f));
                    chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f));
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

        // front
        if (chunk.isFaceVisible(location, Direction.FRONT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, 1.0f));
                    chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
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

        // bottom
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
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
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                }
            }
        }

    }

    private void createFrontFacing(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // front
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getNormals().add(new Vector3f(0.0f, 0.70710677f, 0.70710677f));
                chunkMesh.getTangents().add(new Vector4f(0.0f, -0.7071068f, 0.7071068f, 1.0f));
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

        // left
        if (chunk.isFaceVisible(location, Direction.LEFT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(-1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f));
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

        // right
        if (chunk.isFaceVisible(location, Direction.RIGHT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, 1.0f, -1.0f));
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

        // back
        if (chunk.isFaceVisible(location, Direction.BACK)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, -1.0f));
                    chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f));
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

        // bottom
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
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
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                }
            }
        }

    }

    private void createLeftFacing(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // left
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getNormals().add(new Vector3f(-0.70710677f, 0.70710677f, 0.0f));
                chunkMesh.getTangents().add(new Vector4f(-0.70710677f, -0.70710677f, 0.0f, 1.0f));
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

        // front
        if (chunk.isFaceVisible(location, Direction.FRONT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, 1.0f));
                    chunkMesh.getTangents().add(new Vector4f(-1.0f, 0.0f, 0.0f, -1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
                }
            }
        }

        // right
        if (chunk.isFaceVisible(location, Direction.RIGHT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, 1.0f, -1.0f));
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

        // back
        if (chunk.isFaceVisible(location, Direction.BACK)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, -1.0f));
                    chunkMesh.getTangents().add(new Vector4f(-1.0f, 0.0f, 0.0f, 1.0f));
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

        // bottom
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
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
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                }
            }
        }

    }

    private void createBackFacing(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // back
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getNormals().add(new Vector3f(0.0f, 0.7071067f, -0.7071068f));
                chunkMesh.getTangents().add(new Vector4f(0.0f, -0.7071068f, -0.7071067f, 1.0f));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
            }
        }

        // front
        if (chunk.isFaceVisible(location, Direction.FRONT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, 1.0f));
                    chunkMesh.getTangents().add(new Vector4f(-1.0f, 0.0f, 0.0f, -1.0f));
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

        // left
        if (chunk.isFaceVisible(location, Direction.LEFT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(-1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, -1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
                }
            }
        }

        // right
        if (chunk.isFaceVisible(location, Direction.RIGHT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 1.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            // indices
            chunkMesh.getIndices().add(offset);
            chunkMesh.getIndices().add(offset + 1);
            chunkMesh.getIndices().add(offset + 2);

            if (!chunkMesh.isCollisionMesh()) {
                // normals and tangents
                for (int i = 0; i < 3; i++) {
                    chunkMesh.getNormals().add(new Vector3f(1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.666f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.666f));
                }
            }
        }

        // bottom
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
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
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                }
            }
        }

    }

}
