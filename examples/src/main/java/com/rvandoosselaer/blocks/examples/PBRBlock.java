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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.BloomFilter;
import com.jme3.util.SkyFactory;
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
import lombok.extern.slf4j.Slf4j;

/**
 * An application that renders a single block.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
@Slf4j
public class PBRBlock extends SimpleApplication implements ActionListener {

    private int frame = 0;
    private Chunk chunk;
    private LightProbe lightProbe;

    public static void main(String[] args) {
        PBRBlock pbrBlock = new PBRBlock();
        pbrBlock.start();
    }

    public PBRBlock() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState(),
                new EnvironmentCamera(256, new Vector3f(0, 3f, 0)));
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        typeRegistry.register(TypeIds.WATER, assetManager.loadMaterial("water-pbr/water.j3m"));

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(new Vec3i(0, 0, 0), blockRegistry.get(BlockIds.WATER));
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);

        hideCursor();

        createSky();

        inputManager.addMapping("metallic", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("roughness", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(this, "metallic", "roughness");
    }

    @Override
    public void simpleUpdate(float tpf) {
        frame++;

        if (frame == 2) {
            lightProbe = LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode, new JobProgressAdapter<LightProbe>() {
                @Override
                public void done(LightProbe result) {
                    log.info("Environment map rendered");
                    enqueue(() -> {
                        result.getArea().setRadius(100);
                        getStateManager().getState(PostProcessingState.class).getFilterPostProcessor().addFilter(new BloomFilter(BloomFilter.GlowMode.Scene));
                        rootNode.addLight(result);
                        rootNode.attachChild(chunk.getNode());
                    });
                }
            });
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        Material waterPbr = typeRegistry.get(TypeIds.WATER);

        if ("metallic".equals(name) && isPressed) {
            float metallic = (float) waterPbr.getParam("Metallic").getValue();
            metallic += 0.05f;
            if (metallic > 1) {
                metallic = 0;
            }
            waterPbr.setFloat("Metallic", metallic);
            System.out.println("Metallic value: " + metallic);
        } else if ("roughness".equals(name) && isPressed) {
            float roughness = (float) waterPbr.getParam("Roughness").getValue();
            roughness += 0.05f;
            if (roughness > 1) {
                roughness = 0;
            }
            waterPbr.setFloat("Roughness", roughness);
            System.out.println("Roughness value: " + roughness);
        }
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private void createSky() {
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Path.hdr", SkyFactory.EnvMapType.EquirectMap));
    }

}
