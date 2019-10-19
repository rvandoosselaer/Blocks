package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * @author remy
 */
public interface ChunkGenerator {

    /**
     * Generates the chunk for the given chunk location.
     *
     * @param location of the chunk
     * @return chunk
     */
    Chunk generate(Vec3i location);

}
