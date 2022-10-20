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
import lombok.Getter;
import lombok.ToString;

import java.util.function.Function;

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
        Block block = chunk.getBlock(location.x, location.y, location.z);
        String typeName = block.getType();
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Function<Direction, TextureCoordinates> textureCoordinatesFunction = typeRegistry.get(typeName).getTextureCoordinatesFunction();
        // get the rotation of the shape based on the direction
        Quaternion rotation = Shape.getRotationFromDirection(direction);

        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.UP, direction))) {
            createUp(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
        }
        if (chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
            createDown(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
        }
        createWest(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
        createEast(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
        createSouth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
        createNorth(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction, widthExtend);
    }

    private static void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.NORTH);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMax().y));
        }
    }

    private static void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.SOUTH);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMax().y));
        }
    }

    private static void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.EAST);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMax().y));
        }
    }

    private static void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.WEST);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, textureCoordinates.getMax().y));
        }
    }

    private static void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.DOWN);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            float middleY = ((textureCoordinates.getMax().y - textureCoordinates.getMin().y) / 2) + textureCoordinates.getMin().y;
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, middleY - thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, middleY - thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, middleY + thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, middleY + thicknessExtend / numberOfTextures));
        }
    }

    private static void createUp(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction, float thicknessExtend) {
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
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.UP);
            float middleX = ((textureCoordinates.getMax().x - textureCoordinates.getMin().x) / 2) + textureCoordinates.getMin().x;
            float middleY = ((textureCoordinates.getMax().y - textureCoordinates.getMin().y) / 2) + textureCoordinates.getMin().y;
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, middleY + thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, middleY + thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX + thicknessExtend, middleY - thicknessExtend / numberOfTextures));
            chunkMesh.getUvs().add(new Vector2f(middleX - thicknessExtend, middleY - thicknessExtend / numberOfTextures));
        }
    }

    /**
     * Function that scales an input value in a given range to a given output range.
     */
    public static float map(float value, float startRangeIn, float endRangeIn, float startRangeOut, float endRangeOut) {
        return (value - startRangeIn) * (endRangeOut - startRangeOut) / (endRangeIn - startRangeIn) + startRangeOut;
    }

}
