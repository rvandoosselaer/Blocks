package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * The main configuration object of Blocks.
 *
 * @author rvandoosselaer
 */
@Getter
@Setter
@ToString
public class BlocksConfig {

    private static final BlocksConfig instance = new BlocksConfig();

    private Vec3i chunkSize = new Vec3i(32, 32, 32);
    private float blockScale = 1f;
    private Vec3i gridSize = new Vec3i(9, 5, 9);
    private Vec3i physicsGridSize = new Vec3i(3, 1, 3);

    private BlocksConfig() {
    }

    public static BlocksConfig getInstance() {
        return instance;
    }

    public void setChunkSize(@NonNull Vec3i chunkSize) {
        if (chunkSize.x <= 0 || chunkSize.y <= 0 || chunkSize.z <= 0) {
            throw new IllegalArgumentException("Invalid chunk size specified: " + chunkSize + ".");
        }
        this.chunkSize = chunkSize;
    }

    public void setBlockScale(float blockScale) {
        if (blockScale <= 0) {
            throw new IllegalArgumentException("Invalid block scale specified: " + blockScale + ".");
        }
        this.blockScale = blockScale;
    }

    public void setGridSize(@NonNull Vec3i gridSize) {
        if ((gridSize.x - 1) % 2 != 0 || (gridSize.y - 1) % 2 != 0 || (gridSize.z - 1) % 2 != 0) {
            throw new IllegalArgumentException("Invalid grid size specified: " + gridSize + ". GridSize values should be a power of 2 + 1.");
        }
        this.gridSize = gridSize;
    }

    public void setPhysicsGridSize(@NonNull Vec3i physicsGridSize) {
        if ((physicsGridSize.x - 1) % 2 != 0 || (physicsGridSize.y - 1) % 2 != 0 || (physicsGridSize.z - 1) % 2 != 0) {
            throw new IllegalArgumentException("Invalid grid size specified: " + physicsGridSize + ". PhysicsGridSize values should be a power of 2 + 1.");
        }
        this.physicsGridSize = physicsGridSize;
    }

}
