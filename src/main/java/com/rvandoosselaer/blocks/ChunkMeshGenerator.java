package com.rvandoosselaer.blocks;

import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 * The contract for a chunk mesh implementation.
 *
 * @author rvandoosselaer
 */
public interface ChunkMeshGenerator {

    /**
     * Create the node for the chunk.
     *
     * @param chunk to create the node for
     * @return the node holding all chunk geometries
     */
    Node createNode(Chunk chunk);

    /**
     * Create the collision mesh for the chunk.
     *
     * @param chunk to create the collision mesh for
     * @return the collision mesh of the chunk
     */
    Mesh createCollisionMesh(Chunk chunk);

    /**
     * Create the node and the collision mesh for the chunk. The node and collision mesh should be set on the chunk.
     * This is a combination of the {@link #createNode(Chunk)} and {@link #createCollisionMesh(Chunk)}
     * methods.
     *
     * @param chunk
     */
    void createAndSetNodeAndCollisionMesh(Chunk chunk);

}
