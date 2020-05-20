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
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for a stair. The default facing of a stair is South: the steps will face south.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class Stairs implements Shape {

    private static final Quaternion PI_X = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_X);
    private static final Quaternion PI_Y = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);

    private final Direction direction;
    private final boolean upsideDown;

    public Stairs() {
        this(Direction.UP, false);
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // when the shape is upside down (inverted), we need to perform 3 rotations. Two to invert the shape and one
        // for the direction.
        Quaternion rotation = Shape.getYawFromDirection(direction);
        if (upsideDown) {
            Quaternion inverse = PI_X.mult(PI_Y);
            rotation = inverse.multLocal(rotation.inverseLocal());
        }
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        createUp(location, chunkMesh, rotation, blockScale, multipleImages);
        createSouth(location, chunkMesh, rotation, blockScale, multipleImages);
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(Direction.WEST, direction))) {
            createWest(location, chunkMesh, rotation, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(Direction.EAST, direction))) {
            createEast(location, chunkMesh, rotation, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(Direction.NORTH, direction))) {
            createNorth(location, chunkMesh, rotation, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(Direction.DOWN, direction))) {
            createDown(location, chunkMesh, rotation, blockScale, multipleImages);
        }
    }

    private static void createUp(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:12
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.500f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 7);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 1.000f, 0.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.778f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.889f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.778f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.778f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.889f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.889f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.778f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.889f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
            }
        }
    }

    private static void createDown(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:4
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, 0.500f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 1);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -1.000f, 0.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
            }
        }
    }

    private static void createNorth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:4
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.500f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 1);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, -1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, -1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, -1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, -1.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
            }
        }
    }

    private static void createEast(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:10
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.500f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 6);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.000f, 0.000f, 0.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
            }
        }
    }

    private static void createWest(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:10
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.167f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 1);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.000f, 0.000f, 0.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, 1.000f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.333f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.333f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.667f, 0.444f));
            }
        }
    }

    private static void createSouth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:12
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.500f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.500f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.500f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.167f, -0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.167f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.167f, 0.167f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.500f, -0.167f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 7);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.000f, 1.000f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 1.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.444f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.556f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.667f));
            }
        }
    }

}
