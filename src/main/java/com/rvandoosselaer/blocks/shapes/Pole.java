package com.rvandoosselaer.blocks.shapes;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMesh;
import com.rvandoosselaer.blocks.Direction;
import com.rvandoosselaer.blocks.Shape;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.ToString;

/**
 * A shape implementation for a pole. The default direction of a pole is UP. A direction of NORTH/EAST/SOUTH/WEST will
 * create a horizontal pole, with the top face facing the direction. The Direction UP/DOWN will create a vertical pole.
 * The depth/width of the pole can be configured with the widthExtend.
 *
 * @author rvandoosselaer
 */
@Getter
@ToString
public class Pole implements Shape {

    private final Direction direction;
    private final float widthExtend;

    public Pole() {
        this(Direction.UP, 0.15f);
    }

    public Pole(Direction direction, float widthExtend) {
        this.direction = direction;
        this.widthExtend = FastMath.clamp(Math.abs(widthExtend), 0, 0.5f);
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();
        // get the rotation of the shape based on the direction
        Quaternion rotation = Shape.getRotationFromDirection(direction);

        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.UP, direction))) {
            createUp(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
            createDown(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
        }
        createWest(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
        createEast(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
        createSouth(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
        createNorth(location, rotation, chunkMesh, blockScale, multipleImages, widthExtend);
    }

    private static void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend * -1f)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.0f, -1.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-1.0f, 0.0f, 0.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 2f / 3f));
            }
        }
    }

    private static void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.0f, 1.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 2f / 3f));
            }
        }
    }

    private static void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, -1.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 2f / 3f));
            }
        }
    }

    private static void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend * -1f)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1.0f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 2f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 2f / 3f));
            }
        }
    }

    private static void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, -0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, -0.5f, thicknessExtend)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, -1.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.5f - thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.5f - thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.5f + thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.5f + thicknessExtend));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 6f - thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 6f - thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 1f / 6f + thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 1f / 6f + thicknessExtend / 3f));
            }
        }
    }

    private static void createUp(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages, float thicknessExtend) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend * -1f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend, 0.5f, thicknessExtend)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(thicknessExtend * -1f, 0.5f, thicknessExtend)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 1.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            }
            // uvs
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.5f + thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.5f + thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 0.5f - thicknessExtend));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 0.5f - thicknessExtend));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 5f / 6f + thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 5f / 6f + thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f + thicknessExtend, 5f / 6f - thicknessExtend / 3f));
                chunkMesh.getUvs().add(new Vector2f(0.5f - thicknessExtend, 5f / 6f - thicknessExtend / 3f));
            }
        }
    }

    /**
     * Function that scales an input value in a given range to a given output range.
     */
    public static float map(float value, float startRangeIn, float endRangeIn, float startRangeOut, float endRangeOut) {
        return (value - startRangeIn) * (endRangeOut - startRangeOut) / (endRangeIn - startRangeIn) + startRangeOut;
    }

}
