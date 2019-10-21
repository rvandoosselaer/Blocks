package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlocksManagerTest {

    @Test
    public void testChunkLocationCalculation() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 16, 16));

        Vector3f location = new Vector3f(0, 0, 0);
        Vec3i chunkLocation = BlocksManager.getChunkLocation(location);

        Assertions.assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(13, 10, 5);
        chunkLocation = BlocksManager.getChunkLocation(location);

        Assertions.assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(-5, 3, -9);
        chunkLocation = BlocksManager.getChunkLocation(location);

        Assertions.assertEquals(new Vec3i(-1, 0, -1), chunkLocation);

        location = new Vector3f(16, 15, 2);
        chunkLocation = BlocksManager.getChunkLocation(location);

        Assertions.assertEquals(new Vec3i(1, 0, 0), chunkLocation);
    }

    @Test
    public void testPickedBlockLocation() {
        BlocksConfig.getInstance().setBlockScale(1);

        BlocksManager blocksManager = new BlocksManager();

        Vector3f block = new Vector3f(1.3f, 0.99999f, -2.84f);
        Vec3i blockLocation = blocksManager.getPickedBlockLocation(block, Vector3f.UNIT_Y, false);

        Assertions.assertEquals(new Vec3i(1, 0, -3), blockLocation);

        block = new Vector3f(-13.140036f, 15.920046f, -15.0f);
        blockLocation = blocksManager.getPickedBlockLocation(block, new Vector3f(0, 0, -1), false);

        Assertions.assertEquals(new Vec3i(-14, 15, -15), blockLocation);

        block = new Vector3f(-15.554672f, 15.649327f, -13.999999f);
        blockLocation = blocksManager.getPickedBlockLocation(block, new Vector3f(0, 0, -1), true);

        Assertions.assertEquals(new Vec3i(-16, 15, -15), blockLocation);

        block = new Vector3f(1.5f, 1.0012f, 0.9997f);
        blockLocation = blocksManager.getPickedBlockLocation(block, new Vector3f(0, -1, 0), true);

        Assertions.assertEquals(new Vec3i(1, 0, 0), blockLocation);
    }

}
