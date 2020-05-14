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
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // top face; when the end y value is 1 (top slab), check if the top face should be rendered
        if (endY < 1 || chunk.isFaceVisible(location, Direction.UP)) {
            createTopFace(location, chunkMesh, blockScale, multipleImages);
        }
        // bottom face; when the start y value is 0 (bottom slab), check if the bottom face should be rendered
        if (startY > 0 || chunk.isFaceVisible(location, Direction.DOWN)) {
            createBottomFace(location, chunkMesh, blockScale, multipleImages);
        }
        // left face
        if (chunk.isFaceVisible(location, Direction.WEST)) {
            createLeftFace(location, chunkMesh, blockScale, multipleImages);
        }
        // right face
        if (chunk.isFaceVisible(location, Direction.EAST)) {
            createRightFace(location, chunkMesh, blockScale, multipleImages);
        }
        // front face
        if (chunk.isFaceVisible(location, Direction.SOUTH)) {
            createFrontFace(location, chunkMesh, blockScale, multipleImages);
        }
        // back face
        if (chunk.isFaceVisible(location, Direction.NORTH)) {
            createBackFace(location, chunkMesh, blockScale, multipleImages);
        }
    }

    private void createBackFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, -0.5f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(1.0f, endY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, endY));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
            }
        }
    }

    private void createFrontFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, 0.5f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(1.0f, endY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, endY));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
            }
        }
    }

    private void createRightFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, 0.5f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(1.0f, endY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, endY));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
            }
        }
    }

    private void createLeftFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, -0.5f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(1.0f, endY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, startY));
                chunkMesh.getUvs().add(new Vector2f(0.0f, endY));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(1.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(startY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
                chunkMesh.getUvs().add(new Vector2f(0.0f, mapValueToRange(endY, new Vector2f(0, 1), new Vector2f(1f / 3f, 2f / 3f))));
            }
        }
    }

    private void createBottomFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, startY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, startY, 0.5f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
            }
        }
    }

    private void createTopFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, endY, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, endY, 0.5f), location, blockScale));
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

    /**
     * Calculate the value in a range to a value in another range.
     *
     * @param value            input value
     * @param range            input value range
     * @param destinationRange destination range
     * @return value in destination range
     */
    private static float mapValueToRange(float value, Vector2f range, Vector2f destinationRange) {
        return (value - range.x) * ((destinationRange.y - destinationRange.x) / (range.y - range.x)) + destinationRange.x;
    }

}
