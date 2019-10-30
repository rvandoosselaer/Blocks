package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.debug.BulletDebugAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An application that pages the chunks and the physics chunk meshes around the camera
 *
 * @author rvandoosselaer
 */
public class EndlessPhysicsTerrain extends SimpleApplication {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        EndlessPhysicsTerrain endlessPhysicsTerrain = new EndlessPhysicsTerrain();
        endlessPhysicsTerrain.start();
    }

    @Override
    public void simpleInitApp() {
        BlocksConfig.initialize(assetManager);

        BlocksConfig.getInstance().setGridSize(new Vec3i(9, 1, 9));
        BlocksConfig.getInstance().setPhysicsGridSize(new Vec3i(3, 1, 3));

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        BlocksManager blocksManager = BlocksManager.builder()
                .cacheSize(256)
                .chunkGenerator(new FlatTerrainGenerator(8, blockRegistry.get(Block.GRASS)))
                .meshGenerationPoolSize(1)
                .chunkGenerationPoolSize(1)
                .build();

        stateManager.attachAll(new BlocksManagerState(blocksManager), new BulletAppState());

        addLights(rootNode);
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 20, 0));
    }

    @Override
    public void simpleUpdate(float tpf) {
        BulletAppState bulletAppState = stateManager.getState(BulletAppState.class);
        BlocksManagerState blocksManagerState = stateManager.getState(BlocksManagerState.class);

        if (bulletAppState.isInitialized() && blocksManagerState.isInitialized()) {
            if (stateManager.getState(ChunkPagerState.class) == null) {
                stateManager.attachAll(new ChunkPagerState(rootNode, blocksManagerState.getBlocksManager()),
                        new PhysicsChunkPagerState(bulletAppState.getPhysicsSpace(), blocksManagerState.getBlocksManager()),
                        new BulletDebugAppState(bulletAppState.getPhysicsSpace()));
            }

            stateManager.getState(ChunkPagerState.class).setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));
            stateManager.getState(PhysicsChunkPagerState.class).setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));
        }

    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }

}
