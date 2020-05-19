package com.rvandoosselaer.blocks.examples;

import com.jayfella.fastnoise.FastNoise;
import com.jayfella.fastnoise.LayeredNoise;
import com.jayfella.fastnoise.NoiseLayer;
import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkGenerator;
import com.rvandoosselaer.blocks.ChunkManager;
import com.rvandoosselaer.blocks.ChunkManagerState;
import com.rvandoosselaer.blocks.ChunkPagerState;
import com.rvandoosselaer.blocks.PagerListener;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

import java.util.Random;

/**
 * An application that renders a terrain generated from noise.
 *
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class ProceduralTerrain extends SimpleApplication {

    public static void main(String[] args) {
        LogAdapter.initialize();

        ProceduralTerrain proceduralTerrain = new ProceduralTerrain();
        proceduralTerrain.start();
    }

    public ProceduralTerrain() {
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

        BlocksConfig.getInstance().setGrid(new Vec3i(11, 1, 11));
        BlocksConfig.getInstance().setChunkSize(new Vec3i(32, 256, 32));

        ChunkManager chunkManager = ChunkManager.builder()
                .generatorPoolSize(2)
                .meshPoolSize(2)
                .generator(new ChunkNoiseGenerator(System.currentTimeMillis()))
                .triggerAdjacentChunkUpdates(true)
                .build();

        stateManager.attachAll(new ChunkManagerState(chunkManager), new ChunkPagerState(rootNode, chunkManager));
        stateManager.getState(ChunkPagerState.class).getChunkPager().addListener(new PagerListenerOutput());

        hideCursor();

        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        rootNode.attachChild(sky);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 256, 0));
        cam.lookAt(new Vector3f(128, 128, 128), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        stateManager.getState(ChunkPagerState.class).setLocation(new Vector3f(cam.getLocation().x, 255, cam.getLocation().z));
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private static class PagerListenerOutput implements PagerListener<Node> {

        @Override
        public void onPageDetached(Vec3i location, Node page) {
            System.out.println("Chunk detached - " + location + " - " + page);
        }

        @Override
        public void onPageAttached(Vec3i location, Node page) {
            System.out.println("Chunk attached - " + location + " - " + page);
        }

        @Override
        public void onPageUpdated(Vec3i location, Node oldPage, Node newPage) {
            System.out.println("Chunk updated - " + location + " - " + " old page: " + oldPage + ", new page: " + newPage);
        }

    }

    private static class ChunkNoiseGenerator implements ChunkGenerator {

        private final long seed;
        private LayeredNoise layeredNoise;
        private float waterHeight = 30f;

        public ChunkNoiseGenerator(long seed) {
            this.seed = seed;
            createWorldNoise();
        }

        @Override
        public Chunk generate(Vec3i location) {
            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

            Chunk chunk = Chunk.createAt(location);
            for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
                for (int y = 0; y < BlocksConfig.getInstance().getChunkSize().y; y++) {
                    for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
                        float height = getHeight(getWorldLocation(new Vector3f(x, 0, z), chunk));
                        int worldY = (chunk.getLocation().y * chunkSize.y) + y;

                        float h = Math.max(height, waterHeight);
                        if (worldY <= h) {
                            Block block = worldY <= height ? BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS) : BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.WATER);
                            chunk.addBlock(x, y, z, block);
                        }
                    }
                }
            }
            return chunk;
        }

        private Vector3f getWorldLocation(Vector3f blockLocation, Chunk chunk) {
            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

            return new Vector3f((chunk.getLocation().x * chunkSize.x) + blockLocation.x,
                    (chunk.getLocation().y * chunkSize.y) + blockLocation.y,
                    (chunk.getLocation().z * chunkSize.z) + blockLocation.z);
        }

        private void createWorldNoise() {
            Random random = new Random(seed);
            layeredNoise = new LayeredNoise();

            layeredNoise.setHardFloor(true);
            layeredNoise.setHardFloorHeight(waterHeight);
            layeredNoise.setHardFloorStrength(0.6f);

            NoiseLayer mountains = new NoiseLayer("mountains");
            mountains.setSeed(random.nextInt());
            mountains.setNoiseType(FastNoise.NoiseType.PerlinFractal);
            mountains.setStrength(128);
            mountains.setFrequency(mountains.getFrequency() / 4);
            layeredNoise.addLayer(mountains);

            NoiseLayer hills = new NoiseLayer("Hills");
            hills.setSeed(random.nextInt());
            hills.setNoiseType(FastNoise.NoiseType.PerlinFractal);
            hills.setStrength(64);
            hills.setFrequency(hills.getFrequency() / 2);
            layeredNoise.addLayer(hills);

            NoiseLayer details = new NoiseLayer("Details");
            details.setSeed(random.nextInt());
            details.setNoiseType(FastNoise.NoiseType.PerlinFractal);
            details.setStrength(15);
            layeredNoise.addLayer(details);
        }

        private float getHeight(Vector3f blockLocation) {
            float height = 32f;
            return layeredNoise.evaluate(new Vector2f(blockLocation.x, blockLocation.z)) + height;
        }

    }
}
