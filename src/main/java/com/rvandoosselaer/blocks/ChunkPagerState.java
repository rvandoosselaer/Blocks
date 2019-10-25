package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An {@link com.jme3.app.state.AppState} that manages the lifecycle of a {@link ChunkPager}.
 *
 * @author rvandoosselaer
 */
@Getter
@RequiredArgsConstructor
public class ChunkPagerState extends BaseAppState {

    private final ChunkPager chunkPager;

    @Override
    protected void initialize(Application app) {
        chunkPager.initialize();
    }

    @Override
    protected void cleanup(Application app) {
        chunkPager.cleanup();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void update(float tpf) {
        chunkPager.update();
    }

    public void setLocation(Vector3f location) {
        chunkPager.setLocation(location);
    }

    public Vector3f getLocation() {
        return chunkPager.getLocation();
    }

}
