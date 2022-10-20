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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * A shape implementation for a slab. A slab is actual a cube shape with a controllable y (height) value. If you specify
 * a starting y value of 0 and an end y value of 1, you have a unit cube shape.
 * Only 4 vertices are used per face, 2 vertices are shared.
 *
 * @author rvandoosselaer
 */
@Slf4j
@ToString
public class Slab implements Shape {

    protected final float startY;
    protected final float endY;
    protected final Direction direction;

    public Slab(float startY, float endY) {
        this(startY, endY, Direction.UP);
    }

    public Slab(float startY, float endY, Direction direction) {
        if (startY < 0 || startY > 1 || endY < 0 || endY > 1 || startY > endY) {
            endY = FastMath.clamp(endY, 0, 1);
            startY = Math.min(endY, FastMath.clamp(startY, 0, 1));
            log.warn("Invalid height values specified. Normalized values to: start y: {}, end y: {}.", startY, endY);
        }
        this.startY = startY - 0.5f;
        this.endY = endY - 0.5f;
        this.direction = direction;
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

        if (endY < 1 || chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.UP, direction))) {
            createUp(location, rotation, chunkMesh, blockScale, textureCoordinatesFunction);
        }
        if (startY > 0 || chunk.isFaceVisible(location, Shape.getFaceDirection(Direction.DOWN, direction))) {
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

    protected void createNorth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, -0.5f)), location, blockScale));
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
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
        }
    }

    protected void createSouth(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, 0.5f)), location, blockScale));
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
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
        }
    }

    protected void createEast(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, 0.5f)), location, blockScale));
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
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
        }
    }

    protected void createWest(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, -0.5f)), location, blockScale));
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
            float numberOfTextures = 1 / (textureCoordinates.getMax().y - textureCoordinates.getMin().y);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(startY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, mapValueToRange(endY + 0.5f, new Vector2f(0, 1), new Vector2f(0, 1 / numberOfTextures))));
        }
    }

    protected void createDown(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, startY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, startY, 0.5f)), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
        }
    }

    protected void createUp(Vec3i location, Quaternion rotation, ChunkMesh chunkMesh, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, endY, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, endY, 0.5f)), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
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
