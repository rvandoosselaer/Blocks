//package com.rvandoosselaer.blocks.examples;
//
//import com.jme3.app.SimpleApplication;
//import com.jme3.light.AmbientLight;
//import com.jme3.light.DirectionalLight;
//import com.jme3.material.Material;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Node;
//import com.rvandoosselaer.blocks.*;
//import com.simsilica.mathd.Vec3i;
//import org.slf4j.bridge.SLF4JBridgeHandler;
//
//import java.util.logging.Level;
//import java.util.logging.LogManager;
//
///**
// * An application that shows the collision mesh of the chunks. Blocks that are not solid, are not part of the mesh.
// *
// * @author rvandoosselaer
// */
//public class CollisionMesh extends SimpleApplication {
//
//    public static void main(String[] args) {
//        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//
//        CollisionMesh collisionMesh = new CollisionMesh();
//        collisionMesh.start();
//    }
//
//    @Override
//    public void simpleInitApp() {
//        BlocksConfig.initialize(assetManager);
//
//        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//
//        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
//        for (int x = 0; x < chunkSize.x; x++) {
//            for (int y = 0; y < 2; y++) {
//                for (int z = 0; z < chunkSize.z; z++) {
//                    if (y == 1) {
//                        if (x >= 13 && x < 19 && z >= 13 && z < 19) {
//                            chunk.addBlock(x, y, z, blockRegistry.get(Block.GRASS));
//                        } else if (x == 0 || x == chunkSize.x - 1 || z == 0 || z == chunkSize.z - 1) {
//                            chunk.addBlock(x, y, z, blockRegistry.get(Block.SAND));
//                        } else {
//                            chunk.addBlock(x, y, z, blockRegistry.get(Block.WATER));
//                        }
//                    } else {
//                        chunk.addBlock(x, y, z, blockRegistry.get(Block.SAND));
//                    }
//                }
//            }
//        }
//        chunk.update();
//
//        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
//        chunk.createNode(meshGenerator);
//        chunk.createCollisionMesh(meshGenerator);
//
//        // create and visualize the collision mesh
//        Geometry collision = new Geometry("collision mesh", chunk.getCollisionMesh());
//        collision.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
//        collision.getMaterial().setColor("Color", ColorRGBA.Red);
//        collision.getMaterial().getAdditionalRenderState().setWireframe(true);
//        collision.setLocalTranslation(chunk.getWorldLocation());
//
//        rootNode.attachChild(chunk.getNode());
//        rootNode.attachChild(collision);
//
//        viewPort.setBackgroundColor(ColorRGBA.Cyan);
//        addLights(rootNode);
//        flyCam.setMoveSpeed(10f);
//        cam.setLocation(new Vector3f(8, 4, 9));
//        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
//    }
//
//    private static void addLights(Node node) {
//        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
//        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
//    }
//
//}
