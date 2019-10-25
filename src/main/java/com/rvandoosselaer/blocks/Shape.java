package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * The shape of a {@link Block} element.
 */
public interface Shape {

    String CUBE = "cube";
    String PYRAMID = "pyramid";
    String WEDGE_FRONT = "wedge-front";
    String WEDGE_RIGHT = "wedge-right";
    String WEDGE_BACK = "wedge-back";
    String WEDGE_LEFT = "wedge-left";
    String SLAB = "slab";
    String DOUBLE_SLAB = "double-slab";

    /**
     * Adds the shape at the location in the chunk to the chunk mesh.
     *
     * @param location of the shape in the chunk
     * @param chunk of the shape
     * @param chunkMesh to add the shape to
     */
    void add(Vec3i location, Chunk chunk, ChunkMesh chunkMesh);

}
