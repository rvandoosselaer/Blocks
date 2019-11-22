package com.rvandoosselaer.blocks;

/**
 * A listener that can be registered to the {@link ChunkManager}. Use this to get notified when the mesh of a chunk is
 * updated or when a new chunk is available for retrieval.
 *
 * @author: rvandoosselaer
 */
public interface ChunkManagerListener {

    void onChunkUpdated(Chunk chunk);

    void onChunkAvailable(Chunk chunk);

}
