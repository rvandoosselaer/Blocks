package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.NonNull;

import java.util.Optional;

/**
 * A service to retrieve chunks.
 *
 * @author rvandoosselaer
 */
public interface ChunkResolver {

    /**
     * Return a Chunk optional.
     *
     * @param location of the chunk
     * @return chunk
     */
    Optional<Chunk> get(@NonNull Vec3i location);

}
