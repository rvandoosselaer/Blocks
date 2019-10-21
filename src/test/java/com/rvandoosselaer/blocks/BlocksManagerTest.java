package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class BlocksManagerTest extends SimpleApplication {

    private BlocksManager blocksManager;

    public static void main(String[] args) {
        BlocksManagerTest blocksManagerTest = new BlocksManagerTest();
        blocksManagerTest.start();
    }

    @Override
    public void simpleInitApp() {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        MeshGenerationStrategy meshGenerationStrategy = new FacesMeshGeneration(new ShapeRegistry(), new MaterialRegistry(assetManager));

        blocksManager = new BlocksManager.Builder()
                .meshGenerationPoolSize(1)
                .meshGenerationStrategy(meshGenerationStrategy)
                .build();

        blocksManager.initialize();

        blocksManager.addBlock(new Vec3i(0, 0, 0), Blocks.GRASS);

        Chunk chunk = blocksManager.getChunk(BlocksManager.getChunkLocation(new Vector3f(0, 0, 0)));

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
    }

    @Override
    public void simpleUpdate(float tpf) {
        blocksManager.update();

        Chunk chunk = blocksManager.getChunk(BlocksManager.getChunkLocation(new Vec3i(0, 0, 0).toVector3f()));
        if (chunk != null && chunk.getNode() != null && chunk.getNode().getParent() == null) {
            rootNode.attachChild(chunk.getNode());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        blocksManager.cleanup();
        System.out.println("Done");
    }
}
