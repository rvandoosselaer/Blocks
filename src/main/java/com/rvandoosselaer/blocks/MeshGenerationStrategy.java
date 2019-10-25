package com.rvandoosselaer.blocks;

import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 * @author rvandoosselaer
 */
public interface MeshGenerationStrategy {

    /**
     * Generate the node for the chunk. Each type of block in the chunk has a separate geometry attached the the node.
     *
     * @param chunk to generate the node for
     * @return the node holding all chunk geometries
     */
    Node generateNode(Chunk chunk);

    /**
     * Generate the collision mesh for the chunk.
     *
     * @param chunk to generate the collision mesh for
     * @return the collision mesh of the chunk
     */
    Mesh generateCollisionMesh(Chunk chunk);

    /**
     * Generates the node and the collision mesh for the chunk. The node and collision mesh are set on the chunk and
     * can be retrieved from the chunk.
     * This is a combination of the {@link #generateNode(Chunk)} and {@link #generateNodeAndCollisionMesh(Chunk)}
     * methods.
     *
     * @param chunk
     */
    void generateNodeAndCollisionMesh(Chunk chunk);

}
