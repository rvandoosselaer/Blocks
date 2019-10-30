//package com.rvandoosselaer.blocks;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector3f;
//import com.simsilica.mathd.Vec3i;
//
///**
// * @author rvandoosselaer
// */
//public class BlockTest extends SimpleApplication {
//
//    private BlocksManager blocksManager;
//
//    public static void main(String[] args) {
//        BlockTest blockTest = new BlockTest();
//        blockTest.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        BlocksConfig.initialize(assetManager);
//
//        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
//
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//        Block grass = blockRegistry.get(Block.GRASS);
//        chunk.addBlock(0, 0 ,0, grass);
//
//        MeshGenerationStrategy meshGenerator = BlocksConfig.getInstance().getMeshGenerationStrategy();
//        chunk.createNode(meshGenerator);
//
//        rootNode.attachChild(chunk.getNode());
//
////        BlocksConfig.initialize(assetManager);
////
////        blocksManager = new BlocksManager();
////        blocksManager.initialize();
////
////        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
////        Block grass = blockRegistry.get(Block.GRASS);
////        blocksManager.addBlock(new Vec3i(0, 0, 0), grass);
////
////        blocksManager.update();
////
////        Vec3i chunkLocation = BlocksManager.getChunkLocation(new Vector3f(0, 0, 0));
////        Chunk chunk = blocksManager.getChunk(chunkLocation);
////        rootNode.attachChild(chunk.getNode());
//
////        BlocksConfig.initialize(assetManager);
////
////        BlocksManager blocksManager = new BlocksManager();
////        BlocksManagerState blocksManagerState = new BlocksManagerState(blocksManager);
////
////        ChunkPager chunkPager = new ChunkPager(rootNode, blocksManager);
////        ChunkPagerState chunkPagerState = new ChunkPagerState(chunkPager);
////
////        stateManager.attachAll(blocksManagerState, chunkPagerState);
////
////        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
////        Block grass = blockRegistry.get(Block.GRASS);
////
////        blocksManager.addBlock(new Vec3i(0, 0, 0), grass);
//
//        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f)));
//        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), new ColorRGBA(2f, 2f, 2f, 1f)));
//    }
//
////    @Override
////    public void simpleUpdate(float tpf) {
////        blocksManager.update();
////    }
////
////    @Override
////    public void destroy() {
////        super.destroy();
////        blocksManager.cleanup();
////    }
//}
