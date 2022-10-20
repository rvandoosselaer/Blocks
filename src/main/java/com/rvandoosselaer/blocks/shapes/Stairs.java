package com.rvandoosselaer.blocks.shapes;

import com.jme3.math.FastMath;
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
            rotation = inverse.multLocal(rotation.inverse());
        }
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        Block block = chunk.getBlock(location.x, location.y, location.z);
        String typeName = block.getType();
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Function<Direction, TextureCoordinates> textureCoordinatesFunction = typeRegistry.get(typeName).getTextureCoordinatesFunction();

        createUp(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        createSouth(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(upsideDown ? Direction.EAST : Direction.WEST, direction))) {
            createWest(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(upsideDown ? Direction.WEST : Direction.EAST, direction))) {
            createEast(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(Direction.NORTH, direction))) {
            createNorth(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        }
        if (chunk.isFaceVisible(location, Shape.getYawFaceDirection(upsideDown ? Direction.UP : Direction.DOWN, direction))) {
            createDown(location, chunkMesh, rotation, blockScale, textureCoordinatesFunction);
        }
    }

    private static void createUp(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.UP);
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            float oneThird = 1 / (numberOfTextures * 3);
            float twoThird = 2 / (numberOfTextures * 3);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
        }
    }

    private static void createDown(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.DOWN);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
        }
    }

    private static void createNorth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.NORTH);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
        }
    }

    private static void createEast(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.EAST);
            float numberOfTexturesY = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            float oneThirdY = 1 / (numberOfTexturesY * 3);
            float twoThirdY = 2 / (numberOfTexturesY * 3);
            float numberOfTexturesX = 1 / (textureCoordinates.getMax().x - textureCoordinates.getMin().x);
            float oneThirdX = 1 / (numberOfTexturesX * 3);
            float twoThirdX = 2 / (numberOfTexturesX * 3);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMin().y + oneThirdY));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMin().y + twoThirdY));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + oneThirdY));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMin().y + twoThirdY));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
        }
    }

    private static void createWest(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.WEST);
            float numberOfTexturesY = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            float oneThirdY = 1 / (numberOfTexturesY * 3);
            float twoThirdY = 2 / (numberOfTexturesY * 3);
            float numberOfTexturesX = 1 / (textureCoordinates.getMax().x - textureCoordinates.getMin().x);
            float oneThirdX = 1 / (numberOfTexturesX * 3);
            float twoThirdX = 2 / (numberOfTexturesX * 3);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMin().y + twoThirdY));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMin().y));//
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMin().y + twoThirdY));//
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + oneThirdX, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + oneThirdY));//
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));//
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x + twoThirdX, textureCoordinates.getMin().y + oneThirdY));
        }
    }

    private static void createSouth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
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
            // uv
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.SOUTH);
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            float oneThird = 1 / (numberOfTextures * 3);
            float twoThird = 2 / (numberOfTextures * 3);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + twoThird)); //
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y)); //
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + twoThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y + oneThird));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y + twoThird)); //
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y)); //
        }
    }

}
