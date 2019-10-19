package com.rvandoosselaer.blocks;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Application that shows all the available blocks.
 * You can move through the scene using the mouse, arrow keys and A, D, W, S, Q and Z keys.
 *
 * @author remy
 */
public class BlocksTest extends SimpleApplication {

    public static void main(String[] args) {
        BlocksTest normalMapTest = new BlocksTest();
        normalMapTest.start();
    }

    @Override
    public void simpleInitApp() {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        BlocksConfig.getInstance().setChunkSize(new Vec3i(Blocks.values().length * 2, Blocks.values().length * 2, Blocks.values().length * 2));

        MaterialRegistry materialRegistry = new MaterialRegistry(assetManager);

        ShapeRegistry shapeRegistry = new ShapeRegistry();

        Chunk chunk = Chunk.create(new Vec3i());
        Vec3i blockLocation = new Vec3i(0, 0, 0);
        for (Block block : Blocks.values()) {
            chunk.addBlock(blockLocation.x, blockLocation.y, blockLocation.z, block);
            blockLocation.addLocal(2, 0, 0);
        }
        chunk.createNode(new FacesMeshGeneration(shapeRegistry, materialRegistry));

        rootNode.attachChild(chunk.getNode());
        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));

        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        stateManager.attachAll(new FlyCamAppState(), new StatsAppState());

        cam.setLocation(new Vector3f(Blocks.values().length, 3f, 10));
    }
}