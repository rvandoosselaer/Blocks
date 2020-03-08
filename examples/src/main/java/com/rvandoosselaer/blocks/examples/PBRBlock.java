package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.util.SkyFactory;
import com.rvandoosselaer.blocks.Block;
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
public class PBRBlock extends SimpleApplication {

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

        chunk = createChunk();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Path.hdr", SkyFactory.EnvMapType.EquirectMap));

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);

        hideCursor();
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
                        rootNode.addLight(result);
                        rootNode.attachChild(chunk.getNode());

                        FilterPostProcessor fpp = getStateManager().getState(PostProcessingState.class).getFilterPostProcessor();
                        fpp.addFilter(new BloomFilter(BloomFilter.GlowMode.Scene));
                        fpp.addFilter(new ToneMapFilter(Vector3f.UNIT_XYZ.mult(4.0f)));
                    });
                }
            });
        }
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private Chunk createChunk() {
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        BlocksConfig.getInstance().setChunkSize(new Vec3i(32, 2, 32));

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 32; z++) {
                    if (y == 0) {
                        chunk.addBlock(x, y, z, blockRegistry.get(TypeIds.DIRT));
                    } else {
                        Block block = x > 3 && x < 28 && z > 3 && z < 28 ? blockRegistry.get(TypeIds.WATER) : blockRegistry.get(TypeIds.GRASS);
                        chunk.addBlock(x, y, z, block);
                    }
                }
            }
        }

        chunk.update();
        return chunk;
    }

}
