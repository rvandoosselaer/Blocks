package com.rvandoosselaer.blocks;

import com.jme3.math.FastMath;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChunkTest {

    @Test
    public void testIndexLookup() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(3, 3, 3));

        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, Blocks.GRASS);
        chunk.addBlock(2, 1, 1, Blocks.GRASS);
        chunk.addBlock(2, 2, 2, Blocks.GRASS);

        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(0, 0, 0));
        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(2, 1, 1));
        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(2, 2, 2));

        BlocksConfig.getInstance().setChunkSize(new Vec3i(32, 32, 32));

        chunk = Chunk.create(new Vec3i());
        Vec3i blockLocation = new Vec3i(FastMath.nextRandomInt(0, 31), FastMath.nextRandomInt(0, 31), FastMath.nextRandomInt(0, 31));
        chunk.addBlock(blockLocation.x, blockLocation.y, blockLocation.z, Blocks.GRASS);

        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(blockLocation.x, blockLocation.y, blockLocation.z));

        BlocksConfig.getInstance().setChunkSize(new Vec3i(15, 1, 15));
        chunk = Chunk.create(new Vec3i());
        blockLocation = new Vec3i(10, 0, 10);
        chunk.addBlock(blockLocation.x, blockLocation.y, blockLocation.z, Blocks.GRASS);

        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(blockLocation.x, blockLocation.y, blockLocation.z));

        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 128, 32));
        chunk = Chunk.create(new Vec3i());
        blockLocation = new Vec3i(6, 79, 3);
        chunk.addBlock(blockLocation.x, blockLocation.y, blockLocation.z, Blocks.GRASS);

        Assertions.assertEquals(Blocks.GRASS, chunk.getBlock(6, 79, 3));
    }

    @Test
    public void testLocalCoordinateCalculation() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(3, 3, 3));

        Chunk chunk = Chunk.create(new Vec3i());

        Vec3i worldLocation = new Vec3i(7, 4, 9);
        Vec3i localCoordinate = chunk.toLocalLocation(worldLocation);

        Assertions.assertNull(localCoordinate);

        chunk = Chunk.create(new Vec3i(2, 1, 3));
        localCoordinate = chunk.toLocalLocation(worldLocation);

        Assertions.assertEquals(new Vec3i(1, 1, 0), localCoordinate);
    }

    @Test
    public void testUpdate() {
        // default state
        BlocksConfig.getInstance().setChunkSize(new Vec3i(9, 9, 9));
        Chunk chunk = Chunk.create(new Vec3i());
        Assertions.assertTrue(chunk.isEmpty());
        Assertions.assertFalse(chunk.isFull());

        // not empty, not full
        chunk.addBlock(0, 0, 0, Blocks.GRASS);
        chunk.update();

        Assertions.assertFalse(chunk.isEmpty());
        Assertions.assertFalse(chunk.isFull());

        // full chunk
        for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
            for (int y = 0; y < BlocksConfig.getInstance().getChunkSize().y; y++) {
                for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
                    chunk.addBlock(x, y, z, Blocks.GRASS);
                }
            }
        }
        chunk.update();

        Assertions.assertFalse(chunk.isEmpty());
        Assertions.assertTrue(chunk.isFull());
    }

}
