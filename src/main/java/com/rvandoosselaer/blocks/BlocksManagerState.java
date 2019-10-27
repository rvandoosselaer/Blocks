package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An AppState implementation to manage the lifecycle of a {@link BlocksManager}.
 *
 * @author rvandoosselaer
 */
@Getter
@RequiredArgsConstructor
public class BlocksManagerState extends BaseAppState {

    @NonNull
    private final BlocksManager blocksManager;

    @Override
    protected void initialize(Application app) {
        blocksManager.initialize();
    }

    @Override
    protected void cleanup(Application app) {
        blocksManager.cleanup();
    }

    @Override
    public void update(float tpf) {
        blocksManager.update();
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

}
