package com.rvandoosselaer.blocks;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector3f;

import java.util.Optional;

/**
 * @author: rvandoosselaer
 */
public class BlockManager {

    public void addBlock(Vector3f location) {
    }

    public void removeBlock(Vector3f location) {
    }

    public Optional<Block> getBlock(Vector3f location) {
        return null;
    }

    public Optional<Block> getBlock(CollisionResult collisionResult) {
        return null;
    }

    public Optional<Block> getBlockNeighbour(Vector3f location, Direction direction) {
        return null;
    }

    public Optional<Block> getBlockNeighbour(CollisionResult collisionResult) {
        return null;
    }

}
