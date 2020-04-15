package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.BlocksTheme;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeRegistry;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application that toggles between a new theme and the default theme.
 * When pressing the space bar, the theme changes.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class ChangeTheme extends SimpleApplication implements ActionListener {

    private int frame;
    private Chunk chunk;
    private TypeRegistry typeRegistry;
    private BlocksTheme sampleTheme = new BlocksTheme("A new theme", "/sample-theme");

    public static void main(String[] args) {
        LogAdapter.initialize();

        ChangeTheme changeTheme = new ChangeTheme();
        changeTheme.start();
    }

    public ChangeTheme() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new WireframeState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState(),
                new EnvironmentCamera(32));
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y == 0) {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.DIRT));
                    } else if (x > 8 && x < 20 && z > 6 && z < 23) {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.WATER));
                    } else {
                        chunk.addBlock(x, y, z, getRandomBlock(blockRegistry));
                    }
                }
            }
        }
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        hideCursor();

        rootNode.attachChild(chunk.getNode());

        inputManager.addListener(this, "toggleTheme");
        inputManager.addMapping("toggleTheme", new KeyTrigger(KeyInput.KEY_SPACE));

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(25, 6, 30));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        frame++;

        if (frame == 2) {
            LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode, new JobProgressAdapter<LightProbe>() {
                @Override
                public void done(LightProbe result) {
                    enqueue(() -> {
                        result.getArea().setRadius(100);
                        rootNode.addLight(result);
                    });
                }
            });
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("toggleTheme".equals(name) && isPressed) {
            if (typeRegistry.getTheme() == null) {
                typeRegistry.setTheme(sampleTheme);
            } else {
                typeRegistry.setTheme(null);
            }
            chunk.getNode().removeFromParent();
            rootNode.attachChild(chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator()));
            System.out.println("Setting theme: " + typeRegistry.getTheme());
        }
    }

    private Block getRandomBlock(BlockRegistry blockRegistry) {
        int random = FastMath.nextRandomInt(0, 9);
        switch (random) {
            case 0:
                return blockRegistry.get(BlockIds.DIRT);
            case 1:
                return blockRegistry.get(BlockIds.GRAVEL);
            case 2:
                return blockRegistry.get(BlockIds.SAND);
            case 3:
                return blockRegistry.get(BlockIds.ROCK);
            default:
                return blockRegistry.get(BlockIds.GRASS);
        }
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

}
