package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
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
 * An application that renders all the available blocks.
 *
 * @author rvandoosselaer
 */
public class DefaultBlocks extends SimpleApplication {

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        DefaultBlocks defaultBlocks = new DefaultBlocks();
        defaultBlocks.start();
    }

    @Override
    public void simpleInitApp() {
        BlocksConfig.initialize(assetManager);

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        BlocksConfig.getInstance().setChunkSize(new Vec3i(blockRegistry.getAll().size() * 2, 1, blockRegistry.getAll().size() * 2));

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));

        Vec3i blockLocation = new Vec3i(0, 0, 0);
        for (Block block : blockRegistry.getAll()) {
            chunk.addBlock(blockLocation, block);
            blockLocation = blockLocation.add(2, 0, 0);
        }
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        rootNode.attachChild(chunk.getNode());

        addLights(rootNode);

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        flyCam.setMoveSpeed(10f);
    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }
}
