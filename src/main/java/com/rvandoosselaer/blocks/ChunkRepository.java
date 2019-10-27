package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;

/**
 * The contract of a ChunkRepository implementation.
 *
 * @author rvandoosselaer
 */
public interface ChunkRepository {

    /**
     * Loads the chunk for the given chunk location.
     *
     * @param location of the chunk
     * @return chunk or null if the chunk could not be loaded
     */
    Chunk load(Vec3i location);

    /**
     * Saves the chunk
     *
     * @param chunk to save
     * @return true when successfully saved, false otherwise
     */
    boolean save(Chunk chunk);

}
