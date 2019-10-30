package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An application that toggles between a new theme and the default theme. When pressing the space bar, the theme changes.
 * @author rvandoosselaer
 */
public class ChangeTheme extends SimpleApplication implements ActionListener {

    private TypeRegistry typeRegistry;
    private BlocksTheme sampleTheme;

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        ChangeTheme changeTheme = new ChangeTheme();
        changeTheme.start();
    }

    @Override
    public void simpleInitApp() {
        BlocksConfig.initialize(assetManager);

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        for (int x = 0; x < chunkSize.x; x++) {
            for (int z = 0; z < chunkSize.z; z++) {
                if (x > 8 && x < 20 && z > 6 && z < 23) {
                    chunk.addBlock(x, 0, z, blockRegistry.get(Block.WATER));
                } else {
                    chunk.addBlock(x, 0, z, getRandomBlock(blockRegistry));
                }
            }
        }
        chunk.update();

        MeshGenerationStrategy meshGenerator = BlocksConfig.getInstance().getMeshGenerationStrategy();
        chunk.createNode(meshGenerator);

        inputManager.addListener(this, "toggleTheme");
        inputManager.addMapping("toggleTheme", new KeyTrigger(KeyInput.KEY_SPACE));

        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(25, 6, 30));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        rootNode.attachChild(chunk.getNode());
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        addLights(rootNode);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("toggleTheme".equals(name) && isPressed) {
            if (typeRegistry.getTheme() == null) {
                typeRegistry.setTheme(getSampleTheme());
            } else {
                typeRegistry.setTheme(null);
            }
            System.out.println("Setting theme: " + typeRegistry.getTheme());
        }
    }

    private Block getRandomBlock(BlockRegistry blockRegistry) {
        int random = FastMath.nextRandomInt(0, 9);
        switch (random) {
            case 0:
                return blockRegistry.get(Block.DIRT);
            case 1:
                return blockRegistry.get(Block.OAK);
            case 2:
                return blockRegistry.get(Block.SAND);
            case 3:
                return blockRegistry.get(Block.STONE);
            default:
                return blockRegistry.get(Block.GRASS);
        }
    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }

    private BlocksTheme getSampleTheme() {
        if (sampleTheme == null) {
            sampleTheme = BlocksTheme.builder()
                    .name("A new theme")
                    .path("/sample-theme")
                    .build();
        }
        return sampleTheme;
    }
}
