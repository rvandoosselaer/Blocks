package com.rvandoosselaer.blocks;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A pager implementation that attaches and detaches collision meshes to the given physicsSpace based on the location
 * in the grid.
 *
 * @author remy
 */
@Slf4j
@Getter
@Setter
public class PhysicalChunkPager extends Pager<PhysicsRigidBody> {

    private PhysicsSpace physicsSpace;

    public PhysicalChunkPager(@NonNull BlocksManager blocksManager) {
        this(null, blocksManager);
    }

    public PhysicalChunkPager(PhysicsSpace physicsSpace, @NonNull BlocksManager blocksManager) {
        super(blocksManager);
        this.physicsSpace = physicsSpace;
        this.gridSize = BlocksConfig.getInstance().getPhysicsGridSize();
    }

    @Override
    protected PhysicsRigidBody createPage(Chunk chunk) {
        if (chunk == null || chunk.getCollisionMesh() == null || physicsSpace == null) {
            return null;
        }

        PhysicsRigidBody physicsRigidBody = new PhysicsRigidBody(new MeshCollisionShape(chunk.getCollisionMesh()), 0);
        physicsRigidBody.setPhysicsLocation(chunk.getWorldLocation());

        return physicsRigidBody;
    }

    @Override
    protected void detachPage(PhysicsRigidBody page) {
        if (physicsSpace == null) {
            return;
        }

        if (log.isTraceEnabled()) {
            log.trace("Detaching {} from {}", page, physicsSpace);
        }
        physicsSpace.removeCollisionObject(page);
    }

    @Override
    protected void attachPage(PhysicsRigidBody page) {
        if (physicsSpace == null) {
            return;
        }

        if (log.isTraceEnabled()) {
            log.trace("Attaching {} to {}", page, physicsSpace);
        }
        physicsSpace.addCollisionObject(page);
    }

}
