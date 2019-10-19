package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * The shape of a {@link Block} element.
 */
public interface Shape {

    String CUBE = "cube";
    Shape CUBE_SHAPE = new Cube();
    String PYRAMID = "pyramid";
    Shape PYRAMID_SHAPE = new Pyramid();
    String CUBOID_ONE_THIRD = "cuboid-one-third";
    Shape CUBOID_ONE_THIRD_SHAPE = new Cuboid(0.333f);
    String CUBOID_TWO_THIRD = "cuboid-two-third";
    Shape CUBOID_TWO_THIRD_SHAPE = new Cuboid(0.666f);
    String WEDGE_RIGHT = "wedge-right";
    Shape WEDGE_RIGHT_SHAPE = new Wedge(Direction.RIGHT);
    String WEDGE_FRONT = "wedge-front";
    Shape WEDGE_FRONT_SHAPE = new Wedge(Direction.FRONT);
    String WEDGE_LEFT = "wedge-left";
    Shape WEDGE_LEFT_SHAPE = new Wedge(Direction.LEFT);
    String WEDGE_BACK = "wedge-back";
    Shape WEDGE_BACK_SHAPE = new Wedge(Direction.BACK);

    /**
     * Adds the shape at the location in the chunk to the chunk mesh.
     *
     * @param location of the shape in the chunk
     * @param chunk of the shape
     * @param chunkMesh to add the shape to
     */
    void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh);

}
