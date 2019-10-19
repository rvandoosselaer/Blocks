package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * @author remy
 */
public interface ChunkLoader {

    /**
     * Loads the chunk for the given chunk location.
     *
     * @param location of the chunk
     * @return chunk or null if the chunk could not be loaded
     */
    Chunk load(Vec3i location);

}
