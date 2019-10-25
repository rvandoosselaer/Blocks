package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.debug.BulletDebugAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * @author rvandoosselaer
 */
public class PhysicalChunkPagerTest extends SimpleApplication {

    private PhysicalChunkPagerState physicalChunkPagerState;
    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        PhysicalChunkPagerTest chunkPagerTest = new PhysicalChunkPagerTest();
        chunkPagerTest.start();
    }

    @Override
    public void simpleInitApp() {
        Configurator.setLevel("com.rvandoosselaer.blocks", Level.DEBUG);

        BlocksConfig.initialize(assetManager);
        BlocksConfig config = BlocksConfig.getInstance();

        config.setPhysicsGridSize(new Vec3i(3, 1, 3));

        BlocksManager blocksManager = BlocksManager.builder()
                .chunkGenerationPoolSize(1)
                .chunkGenerator(new FlatTerrainGenerator(1, 31, config.getBlockRegistry().get("grass")))
                .meshGenerationPoolSize(1)
                .build();

        BlocksManagerState blocksManagerState = new BlocksManagerState(blocksManager);
        bulletAppState = new BulletAppState();
        physicalChunkPagerState = new PhysicalChunkPagerState(new PhysicalChunkPager(blocksManager));

        stateManager.attachAll(blocksManagerState, physicalChunkPagerState, bulletAppState);

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));

        flyCam.setMoveSpeed(20f);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        physicalChunkPagerState.setLocation(new Vector3f(cam.getLocation().x, 0, cam.getLocation().z));

        if (bulletAppState.isInitialized() && physicalChunkPagerState.getPhysicsSpace() == null) {
            PhysicsSpace physicsSpace = stateManager.getState(BulletAppState.class).getPhysicsSpace();
            physicalChunkPagerState.setPhysicsSpace(physicsSpace);
            stateManager.attach(new BulletDebugAppState(physicsSpace));
        }

    }

    @RequiredArgsConstructor
    public class FlatTerrainGenerator implements ChunkGenerator {

        private final int minY;
        private final int maxY;
        private final Block block;

        @Override
        public Chunk generate(Vec3i location) {
            Chunk chunk = Chunk.createAt(location);

            int randomY = FastMath.nextRandomInt(minY, maxY);
            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
            for (int x = 0; x < chunkSize.x; x++) {
                for (int y = 0; y < chunkSize.y; y++) {
                    for (int z = 0; z < chunkSize.z; z++) {
                        if (y <= randomY) {
                            chunk.addBlock(x, y, z, block);
                        }
                    }
                }
            }

            return chunk;
        }
    }
}
