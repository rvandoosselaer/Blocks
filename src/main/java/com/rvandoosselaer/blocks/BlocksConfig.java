package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The main configuration object of Blocks.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@Setter
@ToString
public class BlocksConfig {

    private static BlocksConfig instance;

    private final AssetManager assetManager;

    private Vec3i chunkSize;
    private float blockScale;
    private Vec3i gridSize;
    private Vec3i physicsGridSize;
    private ShapeRegistry shapeRegistry;
    private BlockRegistry blockRegistry;
    private TypeRegistry typeRegistry;
    private ChunkMeshGenerator chunkMeshGenerator;

    private BlocksConfig(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public static void initialize(AssetManager assetManager) {
        if (log.isTraceEnabled()) {
            log.trace("Initialize {}", BlocksConfig.class.getSimpleName());
        }
        instance = new BlocksConfig(assetManager);
        instance.setChunkSize(new Vec3i(32, 32, 32));
        instance.setBlockScale(1f);
        instance.setGridSize(new Vec3i(9, 5, 9));
        instance.setPhysicsGridSize(new Vec3i(5, 3, 5));
        instance.setShapeRegistry(new ShapeRegistry());
        instance.setBlockRegistry(new BlockRegistry());
        instance.setTypeRegistry(new TypeRegistry(assetManager));
        instance.setChunkMeshGenerator(new FacesMeshGenerator(instance.getShapeRegistry(), instance.getTypeRegistry()));
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
