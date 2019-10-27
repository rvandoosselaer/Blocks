package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An AppState implementation that manages the lifecycle of a {@link PhysicalChunkPager}.
 *
 * @author rvandoosselaer
 */
@RequiredArgsConstructor
public class PhysicalChunkPagerState extends BaseAppState {

    private final PhysicalChunkPager physicalChunkPager;

    public PhysicalChunkPagerState(PhysicsSpace physicsSpace, BlocksManager blocksManager) {
        this(new PhysicalChunkPager(physicsSpace, blocksManager));
    }

    @Override
    protected void initialize(Application app) {
        physicalChunkPager.initialize();
    }

    @Override
    protected void cleanup(Application app) {
        physicalChunkPager.cleanup();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void update(float tpf) {
        physicalChunkPager.update();
    }

    public void setPhysicsSpace(@NonNull PhysicsSpace physicsSpace) {
        physicalChunkPager.setPhysicsSpace(physicsSpace);
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicalChunkPager.getPhysicsSpace();
    }

    public void setLocation(Vector3f location) {
        physicalChunkPager.setLocation(location);
    }

    public Vector3f getLocation() {
        return physicalChunkPager.getLocation();
    }
}
