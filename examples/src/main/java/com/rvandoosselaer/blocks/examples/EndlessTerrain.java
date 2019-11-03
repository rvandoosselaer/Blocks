//package com.rvandoosselaer.blocks.examples;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.rvandoosselaer.blocks.*;
//import com.simsilica.mathd.Vec3i;
//import org.slf4j.bridge.SLF4JBridgeHandler;
//
//import java.util.logging.Level;
//import java.util.logging.LogManager;
//
///**
// * An application that pages endless terrain around the camera.
// *
// * @author rvandoosselaer
// */
//public class EndlessTerrain extends SimpleApplication {
//
//    private ChunkPagerState chunkPagerState;
//
//    public static void main(String[] args) {
//        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//
//        EndlessTerrain endlessTerrain = new EndlessTerrain();
//        endlessTerrain.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        BlocksConfig.initialize(assetManager);
//
//        BlocksConfig.getInstance().setGridSize(new Vec3i(9, 1, 9));
//
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//
//        // create the BlocksManager that will generate the terrain
//        BlocksManager blocksManager = BlocksManager.builder()
//                .chunkGenerator(new FlatTerrainGenerator(8, blockRegistry.get(Block.GRASS)))
//                .build();
//
//        // create the appstates that manage the lifecycle of the BlocksManager and ChunkPager
//        BlocksManagerState blocksManagerState = new BlocksManagerState(blocksManager);
//        chunkPagerState = new ChunkPagerState(rootNode, blocksManager);
//
//        // atttach the states
//        stateManager.attachAll(blocksManagerState, chunkPagerState);
//
//        addLights(rootNode);
//        viewPort.setBackgroundColor(ColorRGBA.Cyan);
//        flyCam.setMoveSpeed(10f);
//        cam.setLocation(new Vector3f(0, 20f, 0));
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        chunkPagerState.setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));
//    }
//
//    private static void addLights(Node node) {
//        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
//        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
//    }
//
//}
