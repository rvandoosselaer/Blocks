package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An endless runner application.
 *
 * @author: rvandoosselaer
 */
public class EndlessRunner extends SimpleApplication {

    private float runSpeed = 10f;

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        EndlessRunner endlessRunner = new EndlessRunner();
        endlessRunner.start();
    }

    @Override
    public void simpleInitApp() {
        BlocksConfig.initialize(assetManager);

        BlocksConfig.getInstance().setGridSize(new Vec3i(9, 1, 9));

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        BlocksManager blocksManager = BlocksManager.builder()
                .chunkGenerationPoolSize(1)
                .chunkGenerator(new TerrainGenerator(16, blockRegistry))
                .meshGenerationPoolSize(1)
                .build();

        stateManager.attachAll(new BlocksManagerState(blocksManager),
                new ChunkPagerState(rootNode, blocksManager));

        addLights(rootNode);

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        cam.setLocation(new Vector3f(0, 18, 0));
    }

    @Override
    public void simpleUpdate(float tpf) {
        cam.setLocation(cam.getLocation().add(0, 0, runSpeed * -tpf));
        stateManager.getState(ChunkPagerState.class).setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));
    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }

    @RequiredArgsConstructor
    private class TerrainGenerator implements ChunkGenerator {

        /**
         * the y value (inclusive) of the highest blocks
         */
        private final int y;
        private final BlockRegistry blockRegistry;

        @Override
        public Chunk generate(Vec3i location) {
            Chunk chunk = Chunk.createAt(location);

            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
            for (int x = 0; x < chunkSize.x; x++) {
                for (int i = 0; i <= y; i++) {
                    for (int z = 0; z < chunkSize.z; z++) {
                        chunk.addBlock(x, i, z, getBlock());
                    }
                }
            }

            return chunk;
        }

        private Block getBlock() {
            int random = FastMath.nextRandomInt(0, 19); // [1, 19]
            return random == 0 ? blockRegistry.get(Block.DIRT) : blockRegistry.get(Block.GRASS);
        }
    }

}
