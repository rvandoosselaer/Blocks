package com.rvandoosselaer.blocks;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A shape implementation for a cube. Only 4 vertices are used per face, 2 vertices are shared. A face is only added
 * to the resulting mesh if the face is visible. eg. When there is a block above this block, the top face will not be
 * added to the mesh. This cube can be rotated in a given direction. The only difference with the normal cube shape are
 * the uv coordinates.
 * When the direction is Direction.TOP, the default cube is returned.
 *
 * @author rvandoosselaer
 */
@ToString
@RequiredArgsConstructor
public class RotatedCube implements Shape {

    private final Direction direction;

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();
        // check if we have 3 textures or only one
        boolean multipleImages = chunk.getBlock(location.x, location.y, location.z).isUsingMultipleImages();

        // top face
        if (chunk.isFaceVisible(location, Direction.TOP)) {
            createTopFace(location, chunkMesh, blockScale, multipleImages);
        }
        // bottom face
        if (chunk.isFaceVisible(location, Direction.BOTTOM)) {
            createBottomFace(location, chunkMesh, blockScale, multipleImages);
        }
        // left face
        if (chunk.isFaceVisible(location, Direction.LEFT)) {
            createLeftFace(location, chunkMesh, blockScale, multipleImages);
        }
        // right face
        if (chunk.isFaceVisible(location, Direction.RIGHT)) {
            createRightFace(location, chunkMesh, blockScale, multipleImages);
        }
        // front face
        if (chunk.isFaceVisible(location, Direction.FRONT)) {
            createFrontFace(location, chunkMesh, blockScale, multipleImages);
        }
        // back face
        if (chunk.isFaceVisible(location, Direction.BACK)) {
            createBackFace(location, chunkMesh, blockScale, multipleImages);
        }
    }

    private void createBackFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, -0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                }
            }
        }
    }

    private void createFrontFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, 0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                }
            }
        }
    }

    private void createRightFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, 0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                }
            }
        }
    }

    private void createLeftFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, -0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                }
            }
        }
    }

    private void createBottomFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 0.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 0.0f, 0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                }
            }
        }
    }

    private void createTopFace(Vec3i location, ChunkMesh chunkMesh, float blockScale, boolean multipleImages) {
        // calculate index offset, we use this to connect the triangles
        int offset = chunkMesh.getPositions().size();
        // vertices
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, -0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(0.5f, 1.0f, 0.5f), location, blockScale));
        chunkMesh.getPositions().add(Shape.createVertex(new Vector3f(-0.5f, 1.0f, 0.5f), location, blockScale));
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
            if (!multipleImages) {
                chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
            } else {
                if (direction == Direction.TOP) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BOTTOM) {
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 0.0f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                } else if (direction == Direction.LEFT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.RIGHT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                } else if (direction == Direction.FRONT) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                } else if (direction == Direction.BACK) {
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 2f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(1.0f, 1f / 3f));
                    chunkMesh.getUvs().add(new Vector2f(0.0f, 1f / 3f));
                }
            }
        }
    }

}
