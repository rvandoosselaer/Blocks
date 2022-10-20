package com.rvandoosselaer.blocks.shapes;

import com.jme3.math.Quaternion;
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
 * A shape implementation for a square cuboid. A square cuboid is a cube shape with a controllable y (height) value just
 * as a {@link Slab}. The main difference between a square cuboid and a slab, is that a square cuboid is considered as
 * a cube in the face visible check algorithm. Even if the y values are different from the default cube (greater then 0
 * or smaller then 1), the faces between adjacent square cuboids will not be rendered even if they are not shared or
 * touching.
 *
 * @author rvandoosselaer
 */
@Slf4j
@ToString
public class SquareCuboid extends Slab {

    public SquareCuboid(float startY, float endY) {
        super(startY, endY);
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

}
