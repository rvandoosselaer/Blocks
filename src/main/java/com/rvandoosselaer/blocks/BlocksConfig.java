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
    private Vec3i grid;
    private Vec3i physicsGrid;
    private ShapeRegistry shapeRegistry;
    private BlockRegistry blockRegistry;
    private TypeRegistry typeRegistry;
    private ChunkMeshGenerator chunkMeshGenerator;

    private BlocksConfig(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public static void initialize(AssetManager assetManager) {
        initialize(assetManager, true);
    }

    public static void initialize(AssetManager assetManager, boolean registerDefaults) {
        if (log.isTraceEnabled()) {
            log.trace("Initialize {}", BlocksConfig.class.getSimpleName());
        }
        instance = new BlocksConfig(assetManager);
        instance.setChunkSize(new Vec3i(32, 32, 32));
        instance.setBlockScale(1f);
        instance.setGrid(new Vec3i(9, 5, 9));
        instance.setPhysicsGrid(new Vec3i(5, 3, 5));
        instance.setShapeRegistry(new ShapeRegistry(registerDefaults));
        instance.setBlockRegistry(new BlockRegistry(registerDefaults));
        instance.setTypeRegistry(new TypeRegistry(assetManager, null, registerDefaults));
        instance.setChunkMeshGenerator(new FacesMeshGenerator());
    }

    public static BlocksConfig getInstance() {
        return instance;
    }

    public void setChunkSize(@NonNull Vec3i chunkSize) {
        assertChunkSize(chunkSize);
        this.chunkSize = chunkSize;
    }

    public void setBlockScale(float blockScale) {
        if (blockScale <= 0) {
            throw new IllegalArgumentException("Invalid block scale specified: " + blockScale + ".");
        }
        this.blockScale = blockScale;
    }

    public void setGrid(@NonNull Vec3i grid) {
        assertGrid(grid);
        this.grid = grid;
    }

    public void setPhysicsGrid(@NonNull Vec3i physicsGrid) {
        assertGrid(physicsGrid);
        this.physicsGrid = physicsGrid;
    }

    private static void assertChunkSize(@NonNull Vec3i chunkSize) {
        if (chunkSize.x <= 0 || chunkSize.y <= 0 || chunkSize.z <= 0) {
            throw new IllegalArgumentException("Invalid chunk size specified: " + chunkSize + ".");
        }
    }

    private static void assertGrid(@NonNull Vec3i physicsGrid) {
        assertGridHasUnevenValues(physicsGrid);
        assertGridHasPositiveValues(physicsGrid);
    }

    private static void assertGridHasUnevenValues(@NonNull Vec3i grid) {
        if ((grid.x - 1) % 2 != 0 || (grid.y - 1) % 2 != 0 || (grid.z - 1) % 2 != 0) {
            throw new IllegalArgumentException("Invalid grid size specified: " + grid + ". Grid values should be a power of 2 + 1.");
        }
    }

    private static void assertGridHasPositiveValues(@NonNull Vec3i grid) {
        if (grid.x < 1 || grid.y < 1 || grid.z < 1) {
            throw new IllegalArgumentException("Invalid grid size specified: " + grid + ". Grid values should be greater then 0.");
        }
    }

}
