package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An AppState implementation that manages the lifecycle of a {@link PhysicsChunkPager}.
 *
 * @author rvandoosselaer
 */
@Getter
@RequiredArgsConstructor
public class PhysicsChunkPagerState extends BaseAppState {

    private final PhysicsChunkPager physicsChunkPager;

    public PhysicsChunkPagerState(PhysicsSpace physicsSpace, ChunkManager chunkManager) {
        this(new PhysicsChunkPager(physicsSpace, chunkManager));
    }

    @Override
    protected void initialize(Application app) {
        physicsChunkPager.initialize();
    }

    @Override
    protected void cleanup(Application app) {
        physicsChunkPager.cleanup();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void update(float tpf) {
        physicsChunkPager.update();
    }

    public void setPhysicsSpace(@NonNull PhysicsSpace physicsSpace) {
        physicsChunkPager.setPhysicsSpace(physicsSpace);
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsChunkPager.getPhysicsSpace();
    }

    public void setLocation(Vector3f location) {
        physicsChunkPager.setLocation(location);
    }

    public Vector3f getLocation() {
        return physicsChunkPager.getLocation();
    }
}
