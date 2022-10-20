package com.rvandoosselaer.blocks.shapes;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
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
 * A shape implementation for a cylinder. The default direction of a cylinder is UP. A direction of NORTH/EAST/SOUTH/WEST will
 * create a horizontal cylinder, with the top face facing the direction. The Direction UP/DOWN will create a vertical cylinder.
 * The top and bottom radius of the cylinder can be configured as the radial samples.
 *
 * @author rvandoosselaer
 */
@Getter
@ToString
public class Cylinder implements Shape {

    private final Direction direction;
    private final float topRadius;
    private final float bottomRadius;
    private final int radialSamples;
    private final int axisSamples = 2;
    private final float height = 1;

    public Cylinder() {
        this(Direction.UP, 0.5f, 0.5f, 12);
    }

    public Cylinder(float radius) {
        this(Direction.UP, radius, radius, 12);
    }

    public Cylinder(Direction direction, float radius) {
        this(direction, radius, radius, 12);
    }

    public Cylinder(Direction direction, float topRadius, float bottomRadius, int radialSamples) {
        this.direction = direction;
        this.topRadius = FastMath.clamp(FastMath.abs(topRadius), 0, 0.5f);
        this.bottomRadius = FastMath.clamp(FastMath.abs(bottomRadius), 0, 0.5f);
        this.radialSamples = Math.max(radialSamples, 3);
    }

    @Override
    public void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh) {
        // get the block scale, we multiply it with the vertex positions
        float blockScale = BlocksConfig.getInstance().getBlockScale();

        Block block = chunk.getBlock(location.x, location.y, location.z);
        String typeName = block.getType();
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Function<Direction, TextureCoordinates> textureCoordinatesFunction = typeRegistry.get(typeName).getTextureCoordinatesFunction();
        TextureCoordinates textureCoordinatesUp = textureCoordinatesFunction.apply(Direction.UP);
        TextureCoordinates textureCoordinatesDown = textureCoordinatesFunction.apply(Direction.DOWN);
        TextureCoordinates textureCoordinatesSide = textureCoordinatesFunction.apply(Direction.NORTH);
        float numberOfTexturesY = 1 / (textureCoordinatesUp.getMax().y - textureCoordinatesUp.getMin().y);

        // get the rotation of the shape based on the direction
        // we use the jme implementation of a cylinder shape, this shape is by default rotated Direction.SOUTH: horizontal
        // and pointing to the camera.
        // We override the #Shape.getRotationFromDirection method as this method starts from a default rotation of
        // Direction.UP to calculate the rotations.
        Quaternion rotation = getRotationFromDirection(direction);

        // Vertices : One per radial sample plus one duplicate for texture closing around the sides.
        int verticesCount = axisSamples * (radialSamples + 1);
        // Triangles: Two per side rectangle, which is the product of numbers of samples.
        int trianglesCount = axisSamples * radialSamples * 2;
        // add two additional rims and two summits for the caps.
        verticesCount += 2 + 2 * (radialSamples + 1);
        // Add one triangle per radial sample, twice, to form the caps.
        trianglesCount += 2 * radialSamples;

        int offset = chunkMesh.getPositions().size();

        // Compute the points along a unit circle:
        float[][] circlePoints = new float[radialSamples + 1][2];
        for (int circlePoint = 0; circlePoint < radialSamples; circlePoint++) {
            float angle = FastMath.TWO_PI / radialSamples * circlePoint;
            circlePoints[circlePoint][0] = FastMath.cos(angle);
            circlePoints[circlePoint][1] = FastMath.sin(angle);
        }
        // Add a point to close the texture around the side of the cylinder.
        circlePoints[radialSamples][0] = circlePoints[0][0];
        circlePoints[radialSamples][1] = circlePoints[0][1];

        // Calculate normals.
        //
        // A---------B
        //  \        |
        //   \       |
        //    \      |
        //     D-----C
        //
        // Let be B and C the top and bottom points of the axis, and A and D the top and bottom edges.
        // The normal in A and D is simply orthogonal to AD, which means we can get it once per sample.
        //
        Vector3f[] circleNormals = new Vector3f[radialSamples + 1];
        for (int circlePoint = 0; circlePoint < radialSamples + 1; circlePoint++) {
            // The normal is the orthogonal to the side, which can be got without trigonometry.
            // The edge direction is oriented so that it goes up by Height, and out by the radius difference; let's use
            // those values in reverse order.
            Vector3f normal = new Vector3f(height * circlePoints[circlePoint][0],
                    height * circlePoints[circlePoint][1],
                    bottomRadius - topRadius);
            circleNormals[circlePoint] = normal.normalizeLocal();
        }

        // Add a circle of points for each axis sample.
        for (int axisSample = 0; axisSample < axisSamples; axisSample++) {
            float currentHeight = -height / 2 + height * axisSample / (axisSamples - 1);
            float currentRadius = bottomRadius + (topRadius - bottomRadius) * axisSample / (axisSamples - 1);

            for (int circlePoint = 0; circlePoint < radialSamples + 1; circlePoint++) {
                // Position, by multiplying the position on a unit circle with the current radius.
                float x = circlePoints[circlePoint][0] * currentRadius;
                float y = circlePoints[circlePoint][1] * currentRadius;
                float z = currentHeight;

                chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(x, y, z)), location, blockScale));

                Vector3f currentNormal = circleNormals[circlePoint];
                chunkMesh.getNormals().add(Shape.createVertex(rotation.mult(currentNormal), location, blockScale));

                // Texture
                // The X is the angular position of the point.
                float uvX = (float) circlePoint / radialSamples;
                float uvY = (height / 2 + currentHeight / numberOfTexturesY) - textureCoordinatesFunction.apply(Direction.NORTH).getMax().y;

                chunkMesh.getUvs().add(new Vector2f(uvX, uvY));
            }
        }

        // Bottom
        for (int circlePoint = 0; circlePoint < radialSamples + 1; circlePoint++) {
            float x = circlePoints[circlePoint][0] * bottomRadius;
            float y = circlePoints[circlePoint][1] * bottomRadius;
            float z = -height / 2;

            chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(x, y, z)), location, blockScale));

            chunkMesh.getNormals().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, -1)), location, blockScale));

            float uvX = (float) circlePoint / radialSamples;
            float uvY = bottomRadius / (bottomRadius + (height / numberOfTexturesY) + topRadius);
            chunkMesh.getUvs().add(new Vector2f(uvX, uvY));
        }
        // Top
        for (int circlePoint = 0; circlePoint < radialSamples + 1; circlePoint++) {
            float x = circlePoints[circlePoint][0] * topRadius;
            float y = circlePoints[circlePoint][1] * topRadius;
            float z = height / 2;

            chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(x, y, z)), location, blockScale));

            chunkMesh.getNormals().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, 1)), location, blockScale));

            float uvX = (float) circlePoint / radialSamples;
            float uvY = (bottomRadius + (height / numberOfTexturesY)) / (bottomRadius + (height / numberOfTexturesY) + topRadius);
            chunkMesh.getUvs().add(new Vector2f(uvX, uvY));
        }
        // Add the centers of the caps.
        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, -height / 2)), location, blockScale));
        chunkMesh.getNormals().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, -1)), location, blockScale));
        float oneThirdY = 1 / (numberOfTexturesY * 3);
        chunkMesh.getUvs().add(new Vector2f(0.5f, textureCoordinatesFunction.apply(Direction.DOWN).getMin().y + oneThirdY));

        chunkMesh.getPositions().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, height / 2)), location, blockScale));
        chunkMesh.getNormals().add(Shape.createVertex(rotation.mult(new Vector3f(0, 0, 1)), location, blockScale));
        chunkMesh.getUvs().add(new Vector2f(0.5f, textureCoordinatesFunction.apply(Direction.UP).getMin().y));

        // Add the triangles indexes.
        for (int axisSample = 0; axisSample < axisSamples - 1; axisSample++) {
            for (int circlePoint = 0; circlePoint < radialSamples; circlePoint++) {
                chunkMesh.getIndices().add(axisSample * (radialSamples + 1) + circlePoint + offset);
                chunkMesh.getIndices().add(axisSample * (radialSamples + 1) + circlePoint + 1 + offset);
                chunkMesh.getIndices().add((axisSample + 1) * (radialSamples + 1) + circlePoint + offset);

                chunkMesh.getIndices().add((axisSample + 1) * (radialSamples + 1) + circlePoint + offset);
                chunkMesh.getIndices().add(axisSample * (radialSamples + 1) + circlePoint + 1 + offset);
                chunkMesh.getIndices().add((axisSample + 1) * (radialSamples + 1) + circlePoint + 1 + offset);
            }
        }
        // Add caps
        short bottomCapIndex = (short) (verticesCount - 2);
        short topCapIndex = (short) (verticesCount - 1);

        int bottomRowOffset = (axisSamples) * (radialSamples + 1);
        int topRowOffset = (axisSamples + 1) * (radialSamples + 1);

        for (int circlePoint = 0; circlePoint < radialSamples; circlePoint++) {
            chunkMesh.getIndices().add(bottomRowOffset + circlePoint + 1 + offset);
            chunkMesh.getIndices().add(bottomRowOffset + circlePoint + offset);
            chunkMesh.getIndices().add(bottomCapIndex + offset);

            chunkMesh.getIndices().add(topRowOffset + circlePoint + offset);
            chunkMesh.getIndices().add(topRowOffset + circlePoint + 1 + offset);
            chunkMesh.getIndices().add(topCapIndex + offset);
        }

    }

    private static Quaternion getRotationFromDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                return ROTATION_SOUTH;
            case EAST:
                return YAW_WEST;
            case WEST:
                return YAW_EAST;
            case NORTH:
                return ROTATION_DOWN;
            case SOUTH:
                return ROTATION_UP;
            case UP:
                return ROTATION_NORTH;
            default:
                return ROTATION_UP;
        }
    }

}
