package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.TypeRegistry;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application that renders a single block with an overlay map and color. Pressing space bar will set a random
 * overlay color.
 *
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class ColorizeBlock extends SimpleApplication implements ActionListener {

    private Material grassMaterial;

    public static void main(String[] args) {
        LogAdapter.initialize();

        ColorizeBlock singleBlock = new ColorizeBlock();
        singleBlock.start();
    }

    public ColorizeBlock() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new WireframeState(),
                new MemoryDebugState());
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Texture grassTexture = TypeRegistry.combineTextures(
                assetManager.loadTexture("/test-overlay/grass_up.png"),
                assetManager.loadTexture("/test-overlay/grass_side.png"),
                assetManager.loadTexture("/test-overlay/grass_down.png")
        );
        Texture grassOverlayTexture = TypeRegistry.combineTextures(
                assetManager.loadTexture("/test-overlay/full_overlay.png"),
                assetManager.loadTexture("/test-overlay/grass_side-overlay.png"),
                assetManager.loadTexture("/test-overlay/no_overlay.png")
        );
        grassMaterial = typeRegistry.get(TypeIds.GRASS);
        grassMaterial.setTexture("DiffuseMap", grassTexture);
        grassMaterial.setTexture("OverlayMap", grassOverlayTexture);
        grassMaterial.setColor("OverlayColor", ColorRGBA.randomColor());

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(new Vec3i(0, 0, 0), blockRegistry.get(BlockIds.GRASS));
        chunk.update();

        chunk.createNode(meshGenerator);

        rootNode.attachChild(chunk.getNode());

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);

        hideCursor();

        inputManager.addMapping("changeOverlayColor", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "changeOverlayColor");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            grassMaterial.setColor("OverlayColor", ColorRGBA.randomColor());
        }
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

}
