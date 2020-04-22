package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.filters.FluidDepthFilter;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application that renders a coastline scene to test the FluidDepthFilter.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author: rvandoosselaer
 */
public class WaterDepthTest extends SimpleApplication {

    private Chunk chunk;

    public static void main(String[] args) {
        LogAdapter.initialize();

        WaterDepthTest waterDepthTest = new WaterDepthTest();
        waterDepthTest.start();
    }

    public WaterDepthTest() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new WireframeState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState());
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        BlocksConfig.getInstance().setChunkSize(new Vec3i(15, 7, 15));

        chunk = createChunk(BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.WATER));
        chunk.update();

        ChunkMeshGenerator chunkMeshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(chunkMeshGenerator);

        rootNode.attachChild(chunk.getNode());

        hideCursor();
        createCrossHair();

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        cam.setLocation(new Vector3f(-15.381589f, 24.353018f, 30.988268f));
        cam.lookAt(new Vector3f(chunkSize.x * 0.5f, 0, chunkSize.z * 0.5f), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        PostProcessingState postProcessingState = getStateManager().getState(PostProcessingState.class);
        if (postProcessingState.getFilterPostProcessor() != null && postProcessingState.getFilterPostProcessor().getFilter(FluidDepthFilter.class) == null) {
            FluidDepthFilter fluidDepthFilter = new FluidDepthFilter();
            fluidDepthFilter.addFluidGeometry((Geometry) chunk.getNode().getChild(TypeIds.WATER));
            postProcessingState.getFilterPostProcessor().addFilter(fluidDepthFilter);
        }
    }

    private Chunk createChunk(Block waterDepthBlock) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < chunkSize.y; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y > 0) {
                        if (x == 0 || x == 1 || x == chunkSize.x - 2 || x == chunkSize.x - 1 ||
                                z == 0 || z == 1 || z == chunkSize.z - 2 || z == chunkSize.z - 1) {
                            chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
                        } else {
                            if (x == 7 && z == 7) {
                                chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
                            } else {
                                chunk.addBlock(x, y, z, waterDepthBlock);
                            }
                        }
                    } else {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
                    }
                }
            }
        }
        return chunk;
    }

    private void createCrossHair() {
        Label label = new Label("+");
        label.setColor(ColorRGBA.White);

        Camera cam = getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();
        label.setLocalTranslation((width / 2) - (label.getPreferredSize().getX() / 2), (height / 2) + (label.getPreferredSize().getY() / 2), label.getLocalTranslation().getZ());

        guiNode.attachChild(label);
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

}
