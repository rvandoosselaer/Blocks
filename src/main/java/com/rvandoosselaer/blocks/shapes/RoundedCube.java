package com.rvandoosselaer.blocks.shapes;

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
 * A shape implementation for a cube. Only 4 vertices are used per face, 2 vertices are shared. A face is only added
 * to the resulting mesh if the face is visible. eg. When there is a block above this block, the top face will not be
 * added to the mesh.
 * The default cube has a Direction.UP.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class RoundedCube implements Shape {

    private final Direction direction;

    public RoundedCube() {
        this(Direction.UP);
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
            createUp(location, rotation, chunkMesh, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
            createDown(location, rotation, chunkMesh, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.WEST, direction))) {
            createWest(location, rotation, chunkMesh, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.EAST, direction))) {
            createEast(location, rotation, chunkMesh, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.SOUTH, direction))) {
            createSouth(location, rotation, chunkMesh, blockScale, multipleImages);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.NORTH, direction))) {
            createNorth(location, rotation, chunkMesh, blockScale, multipleImages);
        }
    }

    private static void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.450f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.450f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.450f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.450f, -0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.450f, -0.485f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 9);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, 0.282f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.000f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.383f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, -0.367f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, 0.000f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, 0.282f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.383f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, -0.282f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.383f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.383f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, 0.367f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.000f, -0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, 0.282f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, -0.367f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, 0.367f, -0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, -0.282f, -0.886f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, -0.303f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, 0.303f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, -0.303f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, 0.303f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, 1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.345f));
            }
        }
    }

    private static void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.450f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.450f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.450f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.485f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.450f, 0.500f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.485f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.485f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.485f, 0.485f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 0);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, 0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, -0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.383f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, -0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.383f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.383f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, 0.367f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, 0.000f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, -0.367f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.000f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, 0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.383f, 0.924f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, -0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, 0.282f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, -0.367f, 0.886f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, 0.367f, 0.886f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, 0.303f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, -0.303f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, 0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.000f, -0.383f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, -0.303f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.000f, 0.303f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.667f));
            }
        }
    }

    private static void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.450f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.450f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, -0.450f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.500f, 0.450f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.450f, 0.485f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 5);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, 0.282f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, -0.383f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, -0.282f, 0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, -0.383f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, 0.367f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, -0.367f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, 0.282f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.383f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, -0.367f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.886f, 0.367f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.924f, 0.000f, 0.383f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.303f, 0.000f, -0.953f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.303f, 0.000f, -0.953f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.303f, 0.000f, -0.953f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.303f, 0.000f, -0.953f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.345f));
            }
        }
    }

    private static void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.450f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.450f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, -0.450f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.500f, 0.450f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.450f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.450f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.450f, -0.485f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 9);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, -0.367f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, 0.282f, 0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.383f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, -0.282f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, -0.383f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, 0.367f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, -0.367f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.886f, 0.367f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.924f, 0.000f, -0.383f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.303f, 0.000f, -0.953f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.000f, 0.000f, -1.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.303f, 0.000f, -0.953f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.303f, 0.000f, -0.953f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(-0.383f, 0.000f, -0.924f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.303f, 0.000f, -0.953f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.383f, 0.000f, -0.924f, 1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.345f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.336f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.664f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.655f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.345f));
            }
        }
    }

    private static void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.500f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.500f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.500f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.485f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.500f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, -0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, -0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, -0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, -0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, -0.485f, 0.485f)), location, blockScale));
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
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 0);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, -0.886f, 0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, -0.886f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, -0.886f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.924f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -1.000f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, -0.886f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, -0.924f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, -0.886f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, -0.886f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, -0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, -0.886f, 0.367f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, -0.303f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.303f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.303f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, 1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, -0.303f, 0.000f, 1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.012f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.002f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.012f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.321f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.331f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.012f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.002f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.321f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.331f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.321f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.321f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.333f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.012f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
            }
        }
    }

    private static void createUp(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        int offset = chunkMesh.getPositions().size();
        // # Positions:16
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.500f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.500f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.500f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.500f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.485f, 0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, 0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.479f, 0.479f, -0.479f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.485f, 0.485f, 0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.450f, 0.485f, -0.485f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.485f, 0.485f, -0.450f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.450f, 0.485f, 0.485f)), location, blockScale));
        // Index:
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 4);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 6);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 15);
        chunkMesh.getIndices().add(offset + 8);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 10);
        chunkMesh.getIndices().add(offset + 0);
        chunkMesh.getIndices().add(offset + 7);
        chunkMesh.getIndices().add(offset + 12);
        chunkMesh.getIndices().add(offset + 2);
        chunkMesh.getIndices().add(offset + 11);
        chunkMesh.getIndices().add(offset + 13);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 9);
        chunkMesh.getIndices().add(offset + 14);
        chunkMesh.getIndices().add(offset + 3);
        chunkMesh.getIndices().add(offset + 5);
        chunkMesh.getIndices().add(offset + 15);
        if (!chunkMesh.isCollisionMesh()) {
            // Normals:
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, 0.886f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, 0.886f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.367f, 0.886f, 0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.924f, 0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, 0.886f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, 0.886f, 0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.367f, 0.886f, -0.282f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.000f, 0.924f, -0.383f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.282f, 0.886f, -0.367f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.383f, 0.924f, 0.000f)));
            chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.282f, 0.886f, 0.367f)));
            // Tangents:
            Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.303f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, -0.303f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.000f, 0.000f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, 0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, 0.303f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.924f, -0.383f, 0.000f, -1.000f)));
            chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.953f, -0.303f, 0.000f, -1.000f)));
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.000f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.007f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.993f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.036f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.964f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.000f));
            } else {
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.988f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.679f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.988f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.679f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.669f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 0.667f));
                chunkMesh.getUvs().add(new Vector2f(0.007f, 0.998f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.988f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.669f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.679f));
                chunkMesh.getUvs().add(new Vector2f(0.993f, 0.998f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(0.000f, 0.679f));
                chunkMesh.getUvs().add(new Vector2f(0.036f, 1.000f));
                chunkMesh.getUvs().add(new Vector2f(1.000f, 0.988f));
                chunkMesh.getUvs().add(new Vector2f(0.964f, 0.667f));
            }
        }

    }

}
