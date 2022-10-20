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
 * A shape implementation for a pyramid. The default direction of a pyramid is UP: the point will face the UP direction.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class Pyramid implements Shape {

    private final Direction direction;

    public Pyramid() {
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
        Quaternion rotation = Shape.getRotationFromDirection(direction);

        createWest(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        createNorth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        createEast(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        createSouth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
            createDown(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
    }

    private static void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, 0.5f)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, -1.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.DOWN);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

    private static void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.0f, 0.5f, 0.0f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.4472136f, 0.8944272f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.SOUTH);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

    private static void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.0f, 0.5f, 0.0f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.8944272f, 0.4472136f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, -1.0f, 1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.EAST);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

    private static void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.0f, 0.5f, 0.0f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, -0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.4472136f, -0.8944272f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.NORTH);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

    private static void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.0f, 0.5f, 0.0f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, -0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);
        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(-0.8944272f, 0.4472136f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, -1.0f, -1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.WEST);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

}
