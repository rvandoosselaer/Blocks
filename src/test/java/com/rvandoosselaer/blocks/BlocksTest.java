//package com.rvandoosselaer.blocks;
//
//import com.jme3.app.FlyCamAppState;
//import com.jme3.app.SimpleApplication;
//import com.jme3.app.StatsAppState;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.material.Material;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector3f;
//import com.simsilica.mathd.Vec3i;
//import org.slf4j.bridge.SLF4JBridgeHandler;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.LogManager;
//import java.util.stream.Collectors;
//
///**
// * Application that shows all the available blocks.
// * You can move through the scene using the mouse, arrow keys and A, D, W, S, Q and Z keys.
// *
// * @author remy
// */
////public class BlocksTest {
////
////    @Test
////    public void testEquals() {
////        Block block = new Block("block", "a shape", "another type", true, false, true);
////        Block otherBlock = new Block("block", "b shape", "yet another type", false, true, false);
////
////        Assertions.assertEquals(block, otherBlock);
////    }
////
////}
//
//
//public class BlocksTest extends SimpleApplication {
//
//    public static void main(String[] args) {
//        BlocksTest normalMapTest = new BlocksTest();
//        normalMapTest.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//
//        BlocksConfig.initialize(assetManager);
//        BlocksConfig config = BlocksConfig.getInstance();
//        config.setChunkSize(new Vec3i(config.getBlockRegistry().getAll().size() * 2, config.getBlockRegistry().getAll().size() * 2, config.getBlockRegistry().getAll().size() * 2));
//
//        Chunk chunk = Chunk.createAt(new Vec3i());
//        Vec3i blockLocation = new Vec3i(0, 0, 0);
//        for (Block block : config.getBlockRegistry().getAll()) {
//            chunk.addBlock(blockLocation.x, blockLocation.y, blockLocation.z, block);
//            blockLocation.addLocal(2, 0, 0);
//        }
//        chunk.createNode(config.getMeshGenerationStrategy());
//
//        rootNode.attachChild(chunk.getNode());
//        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
//        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));
//
//        viewPort.setBackgroundColor(ColorRGBA.Cyan);
//
//        stateManager.attachAll(new FlyCamAppState(), new StatsAppState());
//
//        cam.setLocation(new Vector3f(config.getBlockRegistry().getAll().size(), 3f, 10));
//
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//
//        List<Block> sorted = blockRegistry.getAll().stream()
//                .sorted(Comparator.comparing(Block::getShape)
//                        .thenComparing(Block::getName))
//                .collect(Collectors.toList());
//
//        sorted.forEach(block -> System.out.println("| " + block.getName() + " | " + block.getShape() + " | " + block.getType() + " |"));
//
//        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
//
//        List<String> sortedMaterials = typeRegistry.getAll().stream()
//                .sorted()
//                .collect(Collectors.toList());
//
//        //sortedMaterials.forEach(material -> System.out.println("| " + material + " |"));
//
//        ShapeRegistry shapeRegistry = BlocksConfig.getInstance().getShapeRegistry();
//
//        List<String> sortedShapes = shapeRegistry.getAll().stream()
//                .sorted()
//                .collect(Collectors.toList());
//
//        //sortedShapes.forEach(s -> System.out.println("| " + s + " |"));
//
//        stop();
//    }
//}