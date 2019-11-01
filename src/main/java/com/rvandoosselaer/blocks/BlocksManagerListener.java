package com.rvandoosselaer.blocks;

/**
 * A listener for the {@link BlocksManager} that gets called when a chunk is available and when the mesh of the chunk
 * is ready.
 * Applications can use this be notified when an updated version of a chunk mesh is available.
 *
 * @author rvandoosselaer
 */
public interface BlocksManagerListener {

    /**
     * Called when the chunk is available for retrieval in the {@link BlocksManager}
     *
     * @param chunk
     */
    void onChunkAvailable(Chunk chunk);

    /**
     * Called when the (updated) mesh of the chunk is available
     *
     * @param chunk
     */
    void onChunkMeshAvailable(Chunk chunk);

}
