package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;

/**
 * A {@link ChunkGenerator} implementation that creates a flat looking terrain at a given y value.
 *
 * @author rvandoosselaer
 */
@RequiredArgsConstructor
public class FlatTerrainGenerator implements ChunkGenerator {

    /**
     * the y value (inclusive) of the highest blocks
     */
    private int y = 0;
    private final Block block;

    public FlatTerrainGenerator(int y, Block block) {
        if (y < 0 || y >= BlocksConfig.getInstance().getChunkSize().y) {
            throw new IllegalArgumentException("Invalid parameters specified! [y=" + y + "]");
        }
        this.y = y;
        this.block = block;
    }

    @Override
    public Chunk generate(Vec3i location) {
        Chunk chunk = Chunk.createAt(location);

        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        for (int x = 0; x < chunkSize.x; x++) {
            for (int i = 0; i <= y; i++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    chunk.addBlock(x, i, z, block);
                }
            }
        }

        return chunk;
    }

}
