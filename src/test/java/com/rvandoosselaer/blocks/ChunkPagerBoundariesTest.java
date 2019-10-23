package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
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
public class ChunkPagerBoundariesTest extends SimpleApplication {

    private ChunkPagerState chunkPagerState;

    public static void main(String[] args) {
        ChunkPagerBoundariesTest chunkPagerTest = new ChunkPagerBoundariesTest();
        chunkPagerTest.start();
    }

    @Override
    public void simpleInitApp() {
        Configurator.setLevel("com.rvandoosselaer.blocks", Level.DEBUG);

        BlocksConfig.getInstance().setGridSize(new Vec3i(3, 3, 3));

        BlocksManager blocksManager = BlocksManager.builder()
                .chunkGenerationPoolSize(1)
                .chunkGenerator(new FlatTerrainGenerator(1, 31))
                .meshGenerationPoolSize(1)
                .meshGenerationStrategy(FacesMeshGeneration.create(assetManager))
                .build();

        BlocksManagerState blocksManagerState = new BlocksManagerState(blocksManager);

        ChunkPager chunkPager = new ChunkPager(rootNode, blocksManager);
        chunkPager.setGridLowerBounds(new Vec3i(-9, 0, -9));
        chunkPager.setGridUpperBounds(new Vec3i(9, 9, 9));

        chunkPagerState = new ChunkPagerState(chunkPager);

        stateManager.attachAll(blocksManagerState, chunkPagerState);

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));

        flyCam.setMoveSpeed(20f);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        chunkPagerState.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z));
    }

    @RequiredArgsConstructor
    public class FlatTerrainGenerator implements ChunkGenerator {

        private final int minY;
        private final int maxY;

        @Override
        public Chunk generate(Vec3i location) {
            Chunk chunk = Chunk.create(location);

            int randomY = FastMath.nextRandomInt(minY, maxY);
            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
            for (int x = 0; x < chunkSize.x; x++) {
                for (int y = 0; y < chunkSize.y; y++) {
                    for (int z = 0; z < chunkSize.z; z++) {
                        if (y <= randomY) {
                            chunk.addBlock(x, y, z, Blocks.GRASS);
                        }
                    }
                }
            }

            return chunk;
        }
    }
}
