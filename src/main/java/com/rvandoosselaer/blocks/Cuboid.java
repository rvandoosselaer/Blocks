package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * A shape implementation for a cuboid. Only 4 vertices are used per face, 2 vertices are shared.
 *
 * @author remy
 */
@Slf4j
@ToString
public class Cuboid implements Shape {

    private final float height;

    public Cuboid(float height) {
        if (height <= 0 || height > 1) {
            log.warn("Invalid height of {} given for cuboid shape. Clamping to a [0-1] value.", height);
        }
        this.height = FastMath.clamp(height, 0, 1);
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // top face
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(new Vector3f(0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
        chunkMesh.getPositions().add(new Vector3f(-0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.6666666f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.6666666f));
            }
        }

        // bottom face
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(0.0f, -1.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.3333333f));
                }
            }
        }
        // left face
        if (chunk.isFaceVisible(location, Direction.LEFT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(-1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.6666666f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.6666666f));
                }
            }
        }
        // right face
        if (chunk.isFaceVisible(location, Direction.RIGHT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(1.0f, 0.0f, 0.0f));
                    chunkMesh.getTangents().add(new Vector4f(0.0f, 0.0f, -1.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.6666666f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.6666666f));
                }
            }
        }
        // front face
        if (chunk.isFaceVisible(location, Direction.FRONT)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, height, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, 1.0f));
                    chunkMesh.getTangents().add(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.6666666f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.6666666f));
                }
            }
        }
        // back face
        if (chunk.isFaceVisible(location, Direction.BACK)) {
            // calculate index offset, we use this to connect the triangles
            offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, 0.0f, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, height, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
                    chunkMesh.getNormals().add(new Vector3f(0.0f, 0.0f, -1.0f));
                    chunkMesh.getTangents().add(new Vector4f(-1.0f, 0.0f, 0.0f, 1.0f));
                }
                // uvs
                if (!multipleImages) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                } else {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.6666666f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.3333333f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.6666666f));
                }
            }
        }
    }

}