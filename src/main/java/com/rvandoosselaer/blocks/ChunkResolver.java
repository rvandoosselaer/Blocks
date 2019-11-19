package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.NonNull;

/**
 * A service to retrieve chunks.
 *
 * @author: rvandoosselaer
 */
public interface ChunkResolver {

    Chunk getChunk(@NonNull Vec3i location);

}
