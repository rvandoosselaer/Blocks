package com.rvandoosselaer.blocks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

//TODO
@Getter
@RequiredArgsConstructor
public class BlocksManagerState extends BaseAppState {

    @NonNull
    private final BlocksManager blocksManager;

//    public BlocksManagerState(ChunkLoader chunkLoader, ChunkGenerator chunkGenerator, MeshGenerationStrategy meshGenerationStrategy) {
//        this(0, 0, chunkLoader, chunkGenerator, meshGenerationStrategy);
//    }

//    public BlocksManagerState(int poolSize, int cacheSize, ChunkLoader chunkLoader, ChunkGenerator chunkGenerator, MeshGenerationStrategy meshGenerationStrategy) {
//        this(new BlocksManager(poolSize, cacheSize, chunkLoader, chunkGenerator, meshGenerationStrategy));
//    }

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
