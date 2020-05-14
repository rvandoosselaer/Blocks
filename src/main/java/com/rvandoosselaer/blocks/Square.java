package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for a plane square in a given direction.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class Square implements Shape {

    private final Direction direction;

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();

        switch (direction) {
            case UP:
                createTopFace(location, chunkMesh, blockScale);
                break;
            case DOWN:
                createBottomFace(location, chunkMesh, blockScale);
                break;
            case WEST:
                createLeftFace(location, chunkMesh, blockScale);
                break;
            case EAST:
                createRightFace(location, chunkMesh, blockScale);
                break;
            case SOUTH:
                createFrontFace(location, chunkMesh, blockScale);
                break;
            case NORTH:
                createBackFace(location, chunkMesh, blockScale);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

    }

    private static void createBackFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, 0.0f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
        }
    }

    private static void createFrontFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.0f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, 0.0f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
        }
    }

    private static void createRightFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.5f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
        }
    }

    private static void createLeftFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.0f, 1.0f, -0.5f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
        }
    }

    private static void createBottomFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.5f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.5f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.5f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.5f, 0.5f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
        }
    }

    private static void createTopFace(Vec3i location, ChunkMesh chunkMesh, float blockScale) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.5f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.5f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.5f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.5f, 0.5f), location, blockScale));
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
            chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
            chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
        }
    }

}
