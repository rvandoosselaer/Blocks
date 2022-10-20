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
 * A shape implementation for a wedge. The default facing of a wedge is South: the sloping side (hypotenuse) will face
 * south.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class Wedge implements Shape {

    private static final Quaternion PI_X = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_X);
    private static final Quaternion PI_Y = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);

    private final Direction direction;
    private final boolean upsideDown;

    public Wedge() {
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

    private static void createDown(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
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

    private static void createNorth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.5f, -0.5f)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.0f, -1.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(1.0f, 0.0f, 0.0f, -1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.NORTH);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
        }
    }

    private static void createEast(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, 1.0f, -1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.EAST);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
        }
    }

    private static void createWest(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        int offset;
        // calculate index offset, we use this to connect the triangles
        offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, 0.5f)), location, blockScale));
        // indices
        chunkMesh.getIndices().add(offset);
        chunkMesh.getIndices().add(offset + 1);
        chunkMesh.getIndices().add(offset + 2);

        if (!chunkMesh.isCollisionMesh()) {
            // normals and tangents
            for (int i = 0; i < 3; i++) {
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(-1.0f, 0.0f, 0.0f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f)));
            }
            // uvs
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.WEST);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
        }
    }

    private static void createSouth(Vec3i location, ChunkMesh chunkMesh, Quaternion rotation, float blockScale, Function<Direction, TextureCoordinates> textureCoordinatesFunction) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, 0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, -0.5f, 0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0.5f, 0.5f, -0.5f)), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(-0.5f, -0.5f, 0.5f)), location, blockScale));
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
                chunkMesh.getNormals().add(rotation.mult(new Vector3f(0.0f, 0.70710677f, 0.70710677f)));
                Matrix4f rotationMatrix = rotation.toRotationMatrix(new Matrix4f());
                chunkMesh.getTangents().add(rotationMatrix.mult(new Vector4f(0.0f, -0.7071068f, 0.7071068f, 1.0f)));
            }
            // uvs
            // the hypotenuse of the wedge is by default facing south but we want to use the texture that is facing up
            // to get a 'slice-through' effect
            TextureCoordinates textureCoordinates = textureCoordinatesFunction.apply(Direction.UP);
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMin().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMax().x, textureCoordinates.getMax().y));
            chunkMesh.getUvs().add(new Vector2f(textureCoordinates.getMin().x, textureCoordinates.getMin().y));
        }
    }

}
