package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: rvandoosselaer
 */
public class ChunkManagerTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testLocationCalculation() {
        BlocksConfig.getInstance().setChunkSize(new Vec3i(16, 16, 16));

        Vector3f location = new Vector3f(0, 0, 0);
        Vec3i chunkLocation = ChunkManager.getLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(13, 10, 5);
        chunkLocation = ChunkManager.getLocation(location);

        assertEquals(new Vec3i(0, 0, 0), chunkLocation);

        location = new Vector3f(-5, 3, -9);
        chunkLocation = ChunkManager.getLocation(location);

        assertEquals(new Vec3i(-1, 0, -1), chunkLocation);

        location = new Vector3f(16, 15, 2);
        chunkLocation = ChunkManager.getLocation(location);

        assertEquals(new Vec3i(1, 0, 0), chunkLocation);

        location = new Vector3f(16, 32, 2);
        chunkLocation = ChunkManager.getLocation(location);

        assertEquals(new Vec3i(1, 2, 0), chunkLocation);
    }

}
