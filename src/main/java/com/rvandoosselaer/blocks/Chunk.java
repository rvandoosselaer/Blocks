package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.simsilica.mathd.Vec3i;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * A chunk holds an array of {@link Block} elements. Blocks can be retrieved, added or removed using the appropriate
 * methods.
 * Each time the data structure of the chunk changes (when blocks are added or removed), the {@link #update()} method
 * should be called to reevaluate the {@link #isFull()} and {@link #isEmpty()} flags.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Chunk {

    // a one dimensional array is quicker to lookup blocks then a 3n array
    @Setter(AccessLevel.PRIVATE)
    private Block[] blocks;
    @Setter(AccessLevel.PRIVATE)
    private Vec3i location;
    private Vector3f worldLocation;
    @ToString.Include
    private boolean empty;
    @ToString.Include
    private boolean full;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Node node;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Mesh collisionMesh;

    private Chunk() {
    }

    public static Chunk create(@NonNull Vec3i location) {
        Chunk chunk = new Chunk();
        chunk.setLocation(location);
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        chunk.setBlocks(new Block[chunkSize.x * chunkSize.y * chunkSize.z]);

        return chunk;
    }

    /**
     * Add a block to this chunk. If there was already a block at this location, it will be overwritten.
     *
     * @param location local coordinate in the chunk
     * @param block    the block to add
     */
    public void addBlock(@NonNull Vec3i location, Block block) {
        addBlock(location.x, location.y, location.z, block);
    }

    /**
     * Add a block to this chunk. If there was already a block at this location, it will be overwritten.
     *
     * @param x     local x coordinate in the chunk
     * @param y     local y coordinate in the chunk
     * @param z     local z coordinate in the chunk
     * @param block the block to add
     */
    public void addBlock(int x, int y, int z, Block block) {
        if (isInsideChunk(x, y, z)) {
            this.blocks[calculateIndex(x, y, z)] = block;
        } else {
            log.warn("Block location ({}, {}, {}) is outside of the chunk boundaries!", x, y, z);
        }
    }

    /**
     * Retrieve the block at the given block coordinate in this chunk.
     *
     * @param location local coordinate in the chunk
     * @return block or null
     */
    public Block getBlock(@NonNull Vec3i location) {
        return getBlock(location.x, location.y, location.z);
    }

    /**
     * Retrieve the block at the given block coordinate in this chunk.
     *
     * @param x local x coordinate
     * @param y local y coordinate
     * @param z local z coordinate
     * @return block or null
     */
    public Block getBlock(int x, int y, int z) {
        if (isInsideChunk(x, y, z)) {
            return this.blocks[calculateIndex(x, y, z)];
        }

        log.warn("Block location ({}, {}, {}) is outside of the chunk boundaries!", x, y, z);
        return null;
    }

    /**
     * Removes and returns the block at the given coordinate in this chunk.
     *
     * @param location local coordinate in the chunk
     * @return the removed block or null
     */
    public Block removeBlock(@NonNull Vec3i location) {
        return removeBlock(location.x, location.y, location.z);
    }

    /**
     * Removes and returns the block at the given coordinate in this chunk.
     *
     * @param x local x coordinate
     * @param y local y coordinate
     * @param z local z coordinate
     * @return the removed block or null
     */
    public Block removeBlock(int x, int y, int z) {
        Block block = getBlock(x, y, z);
        if (block != null) {
            addBlock(x, y, z, null);
        }

        return block;
    }

    //TODO: remove?
    /**
     * Creates the node of the chunk with the given {@link MeshGenerationStrategy}.
     *
     * @param strategy mesh generation strategy to use for constructing the node
     * @return the generated chunk node
     */
    public Node createNode(MeshGenerationStrategy strategy) {
        setNode(strategy.generateNode(this));
        return getNode();
    }

    //TODO: remove?
    /**
     * Creates the collision mesh of the chunk with the given {@link MeshGenerationStrategy}.
     *
     * @param strategy mesh generation strategy to use for creating the collision mesh
     * @return the generated collision mesh
     */
    public Mesh createCollisionMesh(MeshGenerationStrategy strategy) {
        setCollisionMesh(strategy.generateCollisionMesh(this));
        return getCollisionMesh();
    }

    /**
     * Updates the {@link #isEmpty()} and {@link #isFull()} values. This should be called whenever the block data has
     * changed.
     */
    public void update() {
        long start = System.nanoTime();
        boolean empty = true;
        boolean full = true;

        for (Block block : blocks) {
            if (block == null && full) {
                full = false;
            }
            if (block != null && empty) {
                empty = false;
            }

            if (!empty && !full) {
                // break out of the loop
                break;
            }
        }

        this.empty = empty;
        this.full = full;

        if (log.isTraceEnabled()) {
            log.trace("Updating chunk values took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }
    }

    /**
     * Calculates the local coordinate of the block inside this chunk, based on the world location of the block.
     *
     * @param blockWorldLocation block location in the world
     * @return the local block coordinate
     */
    public Vec3i toLocalCoordinate(@NonNull Vec3i blockWorldLocation) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        Vec3i localCoord = new Vec3i(blockWorldLocation.x - (location.x * chunkSize.x), blockWorldLocation.y - (location.y * chunkSize.y), blockWorldLocation.z - (location.z * chunkSize.z));
        if (localCoord.x < 0 || localCoord.x >= chunkSize.x || localCoord.y < 0 || localCoord.y >= chunkSize.y || localCoord.z < 0 || localCoord.z >= chunkSize.z) {
            log.warn("Block world location {} is not part of this chunk {}!", blockWorldLocation, this);
            return null;
        }

        return localCoord;
    }

    /**
     * Calculates the world location of the chunk.
     *
     * @return the world location of the chunk
     */
    public Vector3f getWorldLocation() {
        if (worldLocation == null) {
            Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
            float blockScale = BlocksConfig.getInstance().getBlockScale();

            worldLocation = location.toVector3f().multLocal(chunkSize.toVector3f()).multLocal(blockScale);
            // the chunk at (1, 0, 1) should be positioned at (1 * chunkSize.x, 0 * chunkSize.y, 1 * chunkSize.z)
            // we also add an offset to the chunk location to compensate for the block extends. A block positioned at
            // (0, 0, 0) will have it's bounding box center at (0, blockScale / 2, 0) so the block on the x-axis and z-axis
            // will go from -blockScale / 2 to blockScale / 2.
            worldLocation.addLocal(blockScale * 0.5f, 0, blockScale * 0.5f);
        }
        return worldLocation;
    }

    /**
     * Returns the neighbouring block at the given direction.
     *
     * @param location  block coordinate
     * @param direction neighbour direction
     * @return the neighbouring block or null
     */
    protected Block getNeighbour(@NonNull Vec3i location, @NonNull Direction direction) {
        Vec3i blockLocation = location.add(direction.getPosition());

        return isInsideChunk(blockLocation.x, blockLocation.y, blockLocation.z) ?
                getBlock(blockLocation.x, blockLocation.y, blockLocation.z) : null;
    }

    /**
     * Checks if the face of the block is visible and thus should be rendered.
     * A block is visible when:
     * - the adjacent block in the given direction is not set
     * - the adjacent block in the given direction is transparent and the current block is not transparent
     * - the adjacent block isn't a cube
     *
     * @param location  block coordinate
     * @param direction of the face
     * @return true if the face is visible
     */
    protected boolean isFaceVisible(@NonNull Vec3i location, @NonNull Direction direction) {
        Block neighbour = getNeighbour(location, direction);
        return neighbour == null || (neighbour.isTransparent() && !getBlock(location).isTransparent()) || !Shape.CUBE.equals(neighbour.getShape());
    }

    /**
     * Checks if the given block coordinate is inside the chunk.
     *
     * @param x coordinate of the block in this chunk
     * @param y coordinate of the block in this chunk
     * @param z coordinate of the block in this chunk
     * @return true if the coordinate of the block is inside the chunk, false otherwise.
     */
    private static boolean isInsideChunk(int x, int y, int z) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        return x >= 0 && x < chunkSize.x && y >= 0 && y <= chunkSize.y && z >= 0 && z <= chunkSize.z;
    }

    /**
     * Calculate the index in the block array for the given block coordinate.
     *
     * @param x block coordinate
     * @param y block coordinate
     * @param z block coordinate
     * @return the block array index for the block coordinate.
     */
    private static int calculateIndex(int x, int y, int z) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        return z + (y * chunkSize.z) + (x * chunkSize.y * chunkSize.z);
    }

}
