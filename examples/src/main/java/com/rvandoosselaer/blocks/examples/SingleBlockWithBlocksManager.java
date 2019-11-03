//package com.rvandoosselaer.blocks.examples;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.rvandoosselaer.blocks.Block;
//import com.rvandoosselaer.blocks.BlockRegistry;
//import com.rvandoosselaer.blocks.BlocksConfig;
//import com.rvandoosselaer.blocks.BlocksManager;
//import com.simsilica.mathd.Vec3i;
//
///**
// * An application that renders a single block using the BlocksManager.
// *
// * @author rvandoosselaer
// */
//public class SingleBlockWithBlocksManager extends SimpleApplication {
//
//    private BlocksManager blocksManager;
//
//    public static void main(String[] args) {
//        SingleBlockWithBlocksManager singleBlockWithBlocksManager = new SingleBlockWithBlocksManager();
//        singleBlockWithBlocksManager.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        BlocksConfig.initialize(assetManager);
//
//        blocksManager = new BlocksManager();
//        blocksManager.initialize();
//
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//
//        // add the block to the chunk that contains the location
//        blocksManager.addBlock(new Vec3i(0, 0, 0), blockRegistry.get(Block.GRASS));
//
//        blocksManager.update();
//
//        // calculate the location of the chunk from the location where we placed the block
//        Vec3i chunkLocation = BlocksManager.getChunkLocation(new Vector3f(0, 0, 0));
//        rootNode.attachChild(blocksManager.getChunk(chunkLocation).getNode());
//
//        addLights(rootNode);
//        viewPort.setBackgroundColor(ColorRGBA.Cyan);
//        flyCam.setMoveSpeed(10f);
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        blocksManager.update();
//    }
//
//    private static void addLights(Node node) {
//        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
//        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
//    }
//
//}
