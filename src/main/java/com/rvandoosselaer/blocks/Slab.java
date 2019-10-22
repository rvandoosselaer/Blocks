package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * A shape implementation for a slab. A slab is actual a cube shape with controllable y (height) values. If you specify
 * a starting y value of 0 and an end y value of 1, you have a unit cube shape.
 * Only 4 vertices are used per face, 2 vertices are shared.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@ToString
public class Slab implements Shape {

    private final float startY;
    private final float endY;

    public Slab(float startY, float endY) {
        if (startY < 0 || startY > 1 || endY < 0 || endY > 1 || startY > endY) {
            this.endY = FastMath.clamp(endY, 0, 1);
            this.startY = Math.min(this.endY, FastMath.clamp(startY, 0, 1));
            log.warn("Invalid height values specified: start y: {}, end y: {}. Normalized values to: start y: {}, end y: {}.", startY, endY, this.startY, this.endY);
        } else {
            this.startY = startY;
            this.endY = endY;
        }

        if (log.isTraceEnabled()) {
            log.trace("Created {}", this);
        }
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // top face; when the end y value is 1 (top slab), check if the top face should be rendered
        if (endY == 1 && chunk.isFaceVisible(location, Direction.TOP)) {
            // calculate index offset, we use this to connect the triangles
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
        }
        // bottom face; when the start y value is 0 (bottom slab), check if the bottom face should be rendered
        if (startY == 0 && chunk.isFaceVisible(location, Direction.BOTTOM)) {
            // calculate index offset, we use this to connect the triangles
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, 0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
            int offset = chunkMesh.getPositions().size();
            // vertices
            chunkMesh.getPositions().add(new Vector3f(-0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(-0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, startY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
            chunkMesh.getPositions().add(new Vector3f(0.5f, endY, -0.5f).addLocal(location.x, location.y, location.z).multLocal(blockScale));
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
