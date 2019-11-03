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
// * An application that limits the terrain using the boundaries on the ChunkPager.
// * @author rvandoosselaer
// */
//public class PagerWithBoundaries extends SimpleApplication {
//
//    public static void main(String[] args) {
//        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//
//        PagerWithBoundaries pagerWithBoundaries = new PagerWithBoundaries();
//        pagerWithBoundaries.start();
//    }
//    @Override
//    public void simpleInitApp() {
//        BlocksConfig.initialize(assetManager);
//
//        BlocksManager blocksManager = BlocksManager.builder()
//                .chunkGenerator(new FlatTerrainGenerator(8, BlocksConfig.getInstance().getBlockRegistry().get(Block.GRASS)))
//                .chunkGenerationPoolSize(1)
//                .meshGenerationPoolSize(1)
//                .build();
//
//        ChunkPager chunkPager = new ChunkPager(rootNode, blocksManager);
//        chunkPager.setGridLowerBounds(new Vec3i(-15, -1, -15));
//        chunkPager.setGridUpperBounds(new Vec3i(15, 1, 15));
//
//        ChunkPagerState chunkPagerState = new ChunkPagerState(chunkPager);
//
//        stateManager.attachAll(new BlocksManagerState(blocksManager), chunkPagerState);
//
//        addLights(rootNode);
//        viewPort.setBackgroundColor(ColorRGBA.Cyan);
//        flyCam.setMoveSpeed(10f);
//        cam.setLocation(new Vector3f(0, 20, 0));
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        stateManager.getState(ChunkPagerState.class).setLocation(new Vector3f(cam.getLocation()));
//    }
//
//    private static void addLights(Node node) {
//        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
//        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
//    }
//
//}
