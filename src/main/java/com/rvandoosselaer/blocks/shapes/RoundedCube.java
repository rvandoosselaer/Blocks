package com.rvandoosselaer.blocks.shapes;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMesh;
import com.rvandoosselaer.blocks.Direction;
import com.rvandoosselaer.blocks.Shape;
import com.rvandoosselaer.blocks.TextureCoordinates;
import com.rvandoosselaer.blocks.TypeRegistry;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.function.Function;

/**
 * A shape implementation for a rounded cube. The default rounded cube has a Direction.UP.
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
        Block block = chunk.getBlock(location.x, location.y, location.z);
        String typeName = block.getType();
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Function<Direction, TextureCoordinates> textureCoordinatesFunction = typeRegistry.get(typeName).getTextureCoordinatesFunction();
        // get the rotation of the shape based on the direction
        Quaternion rotation = Shape.getRotationFromDirection(direction);

        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.UP, direction))) {
            createUp(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
            createDown(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.WEST, direction))) {
            createWest(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.EAST, direction))) {
            createEast(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.SOUTH, direction))) {
            createSouth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.NORTH, direction))) {
            createNorth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
    }

    private static void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.NORTH);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
        }
    }

    private static void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.SOUTH);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
        }
    }

    private static void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.EAST);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
        }
    }

    private static void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.WEST);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
        }
    }

    private static void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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

            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.DOWN);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
        }
    }

    private static void createUp(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.UP);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMin().y + 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.007f, textureCoordinates.getMax().y - 0.007f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + 0.036f, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y - 0.036f));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x - 0.036f, textureCoordinates.getMin().y));
        }

    }

}
