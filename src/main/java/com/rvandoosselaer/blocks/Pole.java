package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for a vertical pole. The thickness of the pole can be set with the thicknessExtend.
 * An extend of 0.15f will create a pole of size 0.3f x 1.0f
 *
 * @author rvandoosselaer
 */
@Getter
@ToString
@NoArgsConstructor
public class Pole implements Shape {

    private float thicknessExtend = 0.15f;

    public Pole(float thicknessExtend) {
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        this.thicknessExtend = FastMath.clamp(thicknessExtend, 0, blockScale * 0.5f);;
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        if (chunk.isFaceVisible(location, Direction.TOP)) {
            createTopFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
        }
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            createBottomFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
        }
        createLeftFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
        createRightFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
        createFrontFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
        createBackFace(location, chunkMesh, blockScale, multipleImages, thicknessExtend);
    }

    private static void createBackFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend * -1f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
            }
        }
    }

    private static void createFrontFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
            }
        }
    }

    private static void createRightFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
            }
        }
    }

    private static void createLeftFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend * -1f), location, blockScale));
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
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
            }
        }
    }

    private static void createBottomFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 0.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 0.0f, thicknessExtend), location, blockScale));
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

    private static void createTopFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend * -1f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend, 1.0f, thicknessExtend), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(thicknessExtend * -1f, 1.0f, thicknessExtend), location, blockScale));
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

}
