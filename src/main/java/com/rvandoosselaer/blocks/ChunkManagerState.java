package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * An application state to handle the lifecycle of a ChunkManager.
 *
 * @author: rvandoosselaer
 */
@Getter
@RequiredArgsConstructor
public class ChunkManagerState extends BaseAppState {

    private final ChunkManager chunkManager;

    @Override
    protected void initialize(Application app) {
        chunkManager.initialize();
    }

    @Override
    protected void cleanup(Application app) {
        chunkManager.cleanup();
    }

    @Override
    public void update(float tpf) {
        chunkManager.update();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

}
