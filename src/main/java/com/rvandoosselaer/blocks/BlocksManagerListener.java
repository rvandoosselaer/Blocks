package com.rvandoosselaer.blocks;

/**
 * A listener for the {@link BlocksManager} that gets called when a chunk is available to be retrieved from the
 * BlocksManagers cache.
 * Applications can use this be notified when a chunk or an updated version of a chunk is available.
 *
 * @deprecated use {@link ChunkManagerListener}
 * @author rvandoosselaer
 */
@Deprecated
public interface BlocksManagerListener {

    /**
     * Called when the chunk is available and the node and collision mesh are created.
     *
     * @param chunk
     */
    void onChunkAvailable(Chunk chunk);

}
