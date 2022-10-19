//package com.rvandoosselaer.blocks.examples;
//
//import com.jme3.app.BasicProfilerState;
//import com.jme3.app.DebugKeysAppState;
//import com.jme3.app.FlyCamAppState;
//import com.jme3.app.SimpleApplication;
//import com.jme3.app.StatsAppState;
//import com.jme3.math.ColorRGBA;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.rvandoosselaer.blocks.Block;
//import com.rvandoosselaer.blocks.BlockIds;
//import com.rvandoosselaer.blocks.BlockRegistry;
//import com.rvandoosselaer.blocks.BlocksConfig;
//import com.rvandoosselaer.blocks.Chunk;
//import com.rvandoosselaer.blocks.ChunkGenerator;
//import com.rvandoosselaer.blocks.ChunkManager;
//import com.rvandoosselaer.blocks.ChunkManagerState;
//import com.rvandoosselaer.blocks.ChunkPagerState;
//import com.simsilica.lemur.Axis;
//import com.simsilica.lemur.Container;
//import com.simsilica.lemur.FillMode;
//import com.simsilica.lemur.GuiGlobals;
//import com.simsilica.lemur.Label;
//import com.simsilica.lemur.component.QuadBackgroundComponent;
//import com.simsilica.lemur.component.SpringGridLayout;
//import com.simsilica.lemur.style.BaseStyles;
//import com.simsilica.mathd.Vec3i;
//import com.simsilica.util.LogAdapter;
//import lombok.RequiredArgsConstructor;
//
///**
// * An endless runner application.
// *
// * Default key mappings:
// * print camera position:            c
// * print direct memory information:  m
// * toggle wireframe:                 p
// * toggle profiler:                  F6
// *
// * @author: rvandoosselaer
// */
//public class EndlessRunner extends SimpleApplication {
//
//    private Label location;
//    private float runSpeed = 20f;
//
//    public static void main(String[] args) {
//        LogAdapter.initialize();
//
//        EndlessRunner endlessRunner = new EndlessRunner();
//        endlessRunner.start();
//    }
//
//    public EndlessRunner() {
//        super(new StatsAppState(),
//                new FlyCamAppState(),
//                new DebugKeysAppState(),
//                new LightingState(),
//                new WireframeState(),
//                new PostProcessingState(),
//                new BasicProfilerState(false),
//                new MemoryDebugState());
//    }
//
//    @Override
//    public void simpleInitApp() {
//        GuiGlobals.initialize(this);
//        BaseStyles.loadGlassStyle();
//        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
//
//        BlocksConfig.initialize(assetManager);
//
//        BlocksConfig.getInstance().setGrid(new Vec3i(9, 1, 9));
//
//        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
//        ChunkManager chunkManager = ChunkManager.builder()
//                .generator(new TerrainGenerator(16, blockRegistry))
//                .build();
//
//        stateManager.attachAll(new ChunkManagerState(chunkManager), new ChunkPagerState(rootNode, chunkManager));
//
//        hideCursor();
//        createLocationContainer();
//
//        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
//        cam.setLocation(new Vector3f(0, 19, 0));
//    }
//
//    @Override
//    public void simpleUpdate(float tpf) {
//        cam.setLocation(cam.getLocation().add(0, 0, runSpeed * -tpf));
//        stateManager.getState(ChunkPagerState.class).setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));
//
//        location.setText(getLocationString());
//    }
//
//    private String getLocationString() {
//        Vector3f location = getCamera().getLocation();
//        return String.format("%.0f, %.0f, %.0f, Chunk: %s", location.x, location.y, location.z, ChunkManager.getChunkLocation(location));
//    }
//
//    private void createLocationContainer() {
//        Container container = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.None, FillMode.None));
//        container.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 0.9f)));
//        container.addChild(new Label("Location: "));
//        location = container.addChild(new Label(getLocationString()));
//
//        Camera cam = getCamera();
//        container.setLocalTranslation(0, cam.getHeight(), 1);
//
//        guiNode.attachChild(container);
//    }
//
//    private void hideCursor() {
//        GuiGlobals.getInstance().setCursorEventsEnabled(false);
//        inputManager.setCursorVisible(false);
//    }
//
//    @RequiredArgsConstructor
//    private static class TerrainGenerator implements ChunkGenerator {
//
//        /**
//         * the y value (inclusive) of the highest blocks
//         */
//        private final int y;
//        private final BlockRegistry blockRegistry;
//
//        @Override
//        public Chunk generate(Vec3i location) {
//            Chunk chunk = Chunk.createAt(location);
//
//            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
//            for (int x = 0; x < chunkSize.x; x++) {
//                for (int i = 0; i <= y; i++) {
//                    for (int z = 0; z < chunkSize.z; z++) {
//                        chunk.addBlock(x, i, z, getBlock());
//                    }
//                }
//            }
//
//            return chunk;
//        }
//
//        private Block getBlock() {
//            int random = FastMath.nextRandomInt(0, 19); // [0, 19]
//            return random == 0 ? blockRegistry.get(BlockIds.DIRT) : blockRegistry.get(BlockIds.GRASS);
//        }
//
//    }
//
//}
