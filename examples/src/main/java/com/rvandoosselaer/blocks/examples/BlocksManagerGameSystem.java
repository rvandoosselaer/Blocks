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
import com.simsilica.sim.AbstractGameSystem;
import com.simsilica.sim.SimTime;
import com.simsilica.state.GameSystemsState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An application that updates the lifecycle of the BlocksManager as a <a href="https://github.com/Simsilica/SiO2">SiO2</a> GameSystem.
 * Each frame update, a block is placed at a random location in the grid.
 *
 * @author rvandoosselaer
 */
public class BlocksManagerGameSystem extends SimpleApplication {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        BlocksManagerGameSystem blocksManagerGameSystem = new BlocksManagerGameSystem();
        blocksManagerGameSystem.start();
    }

    @Override
    public void simpleInitApp() {
        BlocksConfig.initialize(assetManager);

        BlocksConfig.getInstance().setGridSize(new Vec3i(5, 1, 5));

        GameSystemsState systemsState = new GameSystemsState(true);
        systemsState.register(BlocksManagerSystem.class, new BlocksManagerSystem(new BlocksManager()));

        stateManager.attach(systemsState);

        addLights(rootNode);
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 20, 0));
    }

    @Override
    public void simpleUpdate(float tpf) {
        BlocksManagerSystem blocksManagerSystem = stateManager.getState(GameSystemsState.class).get(BlocksManagerSystem.class);
        if (blocksManagerSystem.isInitialized()) {
            if (stateManager.getState(ChunkPagerState.class) == null) {
                stateManager.attach(new ChunkPagerState(rootNode, blocksManagerSystem.getBlocksManager()));
            }
            BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
            blocksManagerSystem.getBlocksManager().addBlock(getRandomBlockLocation(), blockRegistry.get(Block.GRASS));
        }
    }

    private static Vec3i getRandomBlockLocation() {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        Vec3i gridSize = BlocksConfig.getInstance().getGridSize();

        return new Vec3i(FastMath.nextRandomInt(((gridSize.x - 1) / -2) * chunkSize.x, ((gridSize.x - 1) / 2) * chunkSize.x),
                FastMath.nextRandomInt(0, chunkSize.y),
                FastMath.nextRandomInt(((gridSize.z - 1) / -2) * chunkSize.z, ((gridSize.z - 1) / 2) * chunkSize.z));
    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }

    @RequiredArgsConstructor
    public class BlocksManagerSystem extends AbstractGameSystem {

        @Getter
        private final BlocksManager blocksManager;

        @Override
        protected void initialize() {
            blocksManager.initialize();
        }

        @Override
        protected void terminate() {
            blocksManager.cleanup();
        }

        @Override
        public void update(SimTime time) {
            blocksManager.update();
        }

    }

}
