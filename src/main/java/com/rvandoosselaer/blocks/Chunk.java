package com.rvandoosselaer.blocks;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.simsilica.mathd.Vec3i;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * A chunk holds an array of {@link Block} elements. Blocks can be retrieved, added or removed using the appropriate
 * methods.
 * Each time the data structure of the chunk changes (when blocks are added or removed), the {@link #update()} method
 * should be called to reevaluate the {@code isFull()} and {@code isEmpty()} flags.
 * Make sure to call the {@link #cleanup()} method to properly dispose of the chunk.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Chunk {

    // a one dimensional array is quicker to lookup blocks then a 3n array
    @Setter
    private Block[] blocks;
    @ToString.Include
    private Vec3i location;
    private Vector3f worldLocation;
    @ToString.Include
    private boolean empty;
    @ToString.Include
    private boolean full;
    @Setter
    private Node node;
    @Setter
    private Mesh collisionMesh;
    @Setter
    private static BiFunction<Block, Block, Boolean> faceVisibleFunction = new DefaultFaceVisibleFunction();
    @Setter
    private ChunkResolver chunkResolver;

    public Chunk(@NonNull Vec3i location) {
        this.location = location;
//        setLocation(location);
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
//        setBlocks(new Block[chunkSize.x * chunkSize.y * chunkSize.z]);
        this.blocks = new Block[chunkSize.x * chunkSize.y * chunkSize.z];
        update();
    }

    public static Chunk createAt(@NonNull Vec3i location) {
        return new Chunk(location);
    }

    /**
     * Add a block to this chunk. If there was already a block at this location, it will be overwritten.
     *
     * @param location local coordinate in the chunk
     * @param block    the block to add
     * @return the previous block at the location or null
     */
    public Block addBlock(@NonNull Vec3i location, Block block) {
        return addBlock(location.x, location.y, location.z, block);
    }

    /**
     * Add a block to this chunk. If there was already a block at this location, it will be replaced.
     *
     * @param x     local x coordinate in the chunk
     * @param y     local y coordinate in the chunk
     * @param z     local z coordinate in the chunk
     * @param block the block to add
     * @return the previous block at the location or null
     */
    public Block addBlock(int x, int y, int z, Block block) {
        if (isInsideChunk(x, y, z)) {
            int index = calculateIndex(x, y, z);
            Block previous = blocks[index];
            blocks[index] = block;
            if (log.isTraceEnabled()) {
                log.trace("Added {} at ({}, {}, {}) to {}", block, x, y, z, this);
            }
            return previous;
        }
        log.warn("Block location ({}, {}, {}) is outside of the chunk boundaries!", x, y, z);
        return null;
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
        if (isInsideChunk(x, y, z)) {
            int index = calculateIndex(x, y, z);
            Block block = blocks[index];
            blocks[index] = null;
            if (log.isTraceEnabled()) {
                log.trace("Removed {} at ({}, {}, {}) from {}", block, x, y, z, this);
            }
            return block;
        }

        log.warn("block location ({}, {}, {}) is outside the chunk boundaries!", x, y, z);
        return null;
    }

    /**
     * Creates and returns the node of the chunk with the given {@link ChunkMeshGenerator}.
     *
     * @param strategy mesh generation strategy to use for constructing the node
     * @return the generated chunk node
     */
    public Node createNode(ChunkMeshGenerator strategy) {
        setNode(strategy.createNode(this));
        return getNode();
    }

    /**
     * Creates and returns the collision mesh of the chunk with the given {@link ChunkMeshGenerator}.
     *
     * @param strategy mesh generation strategy to use for creating the collision mesh
     * @return the generated collision mesh
     */
    public Mesh createCollisionMesh(ChunkMeshGenerator strategy) {
        setCollisionMesh(strategy.createCollisionMesh(this));
        return getCollisionMesh();
    }

    /**
     * Updates the {@code isEmpty()} and {@code isFull()} values. This should be called whenever the block data has
     * changed.
     */
    public void update() {
//        long start = System.nanoTime();
        Instant start = Instant.now();
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
//            log.trace("Updating {} values took {}ms", this, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            log.trace("Updating {} values took {}ms", this, Duration.between(start, Instant.now()).toMillis());
        }

    }

    public void cleanup() {
        this.blocks = null;
        this.node = null;
        this.collisionMesh = null;
        this.location = null;
        this.worldLocation = null;
        this.chunkResolver = null;
    }

    /**
     * Calculates the local coordinate of the block inside this chunk, based on the world location of the block.
     *
     * @param blockWorldLocation block location in the world
     * @return the local block coordinate
     */
    public Vec3i toLocalLocation(@NonNull Vec3i blockWorldLocation) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        Vec3i localCoord = new Vec3i(blockWorldLocation.x - (location.x * chunkSize.x), blockWorldLocation.y - (location.y * chunkSize.y), blockWorldLocation.z - (location.z * chunkSize.z));
        if (!isInsideChunk(localCoord.x, localCoord.y, localCoord.z)) {
            log.warn("Block world location {} is not part of this chunk {}!", blockWorldLocation, this);
            return null;
        }

        return localCoord;
    }

    /**
     * checks if the block location in the world is part of this chunk.
     *
     * @param blockWorldLocation block location in the world
     * @return true if this chunk contains the location, false otherwise
     */
    public boolean containsLocation(Vec3i blockWorldLocation) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        Vec3i localCoord = new Vec3i(blockWorldLocation.x - (location.x * chunkSize.x), blockWorldLocation.y - (location.y * chunkSize.y), blockWorldLocation.z - (location.z * chunkSize.z));

        return isInsideChunk(localCoord.x, localCoord.y, localCoord.z);
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
            // (0, 0, 0) will have it's bounding box center at (0, 0, 0) so the block on the x-axis and z-axis
            // will go from -blockScale / 2 to blockScale / 2.
            worldLocation.addLocal(blockScale * 0.5f, blockScale * 0.5f, blockScale * 0.5f);
        }
        return worldLocation;
    }

    /**
     * Returns the neighbouring block at the given direction. When a {@link ChunkResolver} is set, the block from a
     * neighbouring chunk is retrieved.
     *
     * @param location  block coordinate
     * @param direction neighbour direction
     * @return the neighbouring block or null
     */
    public Block getNeighbour(@NonNull Vec3i location, @NonNull Direction direction) {
        Vec3i blockLocation = location.add(direction.getVector());

        if (isInsideChunk(blockLocation.x, blockLocation.y, blockLocation.z)) {
            return getBlock(blockLocation.x, blockLocation.y, blockLocation.z);
        }

        if (hasChunkResolver()) {
            Vec3i chunkLocation = calculateNeighbourChunkLocation(blockLocation);
            Vec3i neighbourBlockLocation = calculateNeighbourChunkBlockLocation(blockLocation);

            return chunkResolver.get(chunkLocation)
                    .map(chunk -> chunk.getBlock(neighbourBlockLocation.x, neighbourBlockLocation.y, neighbourBlockLocation.z))
                    .orElse(null);
        }

        return null;
    }

    /**
     * Checks if the face of the block is visible using the faceVisibleFunction and thus should be rendered.
     *
     * @param location  block coordinate
     * @param direction of the face
     * @return true if the face is visible
     */
    public boolean isFaceVisible(@NonNull Vec3i location, @NonNull Direction direction) {
        Block block = getBlock(location);
        Block neighbour = getNeighbour(location, direction);

        return faceVisibleFunction.apply(block, neighbour);
    }

    private boolean hasChunkResolver() {
        return chunkResolver != null;
    }

    private Vec3i calculateNeighbourChunkLocation(Vec3i blockLocation) {
        Vec3i chunkLocation = new Vec3i(getLocation());
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        if (blockLocation.x < 0) {
            chunkLocation.addLocal(-1, 0, 0);
        }
        if (blockLocation.x >= chunkSize.x) {
            chunkLocation.addLocal(1, 0, 0);
        }
        if (blockLocation.y < 0) {
            chunkLocation.addLocal(0, -1, 0);
        }
        if (blockLocation.y >= chunkSize.y) {
            chunkLocation.addLocal(0, 1, 0);
        }
        if (blockLocation.z < 0) {
            chunkLocation.addLocal(0, 0, -1);
        }
        if (blockLocation.z >= chunkSize.z) {
            chunkLocation.addLocal(0, 0, 1);
        }
        return chunkLocation;
    }

    private Vec3i calculateNeighbourChunkBlockLocation(Vec3i blockLocation) {
        Vec3i toReturn = new Vec3i(blockLocation);
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        if (blockLocation.x < 0) {
            toReturn.x = chunkSize.x - 1;
        }
        if (blockLocation.x >= chunkSize.x) {
            toReturn.x = 0;
        }
        if (blockLocation.y < 0) {
            toReturn.y = chunkSize.y - 1;
        }
        if (blockLocation.y >= chunkSize.y) {
            toReturn.y = 0;
        }
        if (blockLocation.z < 0) {
            toReturn.z = chunkSize.z - 1;
        }
        if (blockLocation.z >= chunkSize.z) {
            toReturn.z = 0;
        }
        return toReturn;
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
        return x >= 0 && x < chunkSize.x && y >= 0 && y < chunkSize.y && z >= 0 && z < chunkSize.z;
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

    /**
     * A function that checks if the shared face between the 2 adjacent blocks should be visible (rendered).
     * The first block argument is the block that checks if it's face should be rendered, the second block argument is
     * the neighbouring block on that direction.
     * <p>
     * The default behaviour states that the face of a block is visible when:
     * - the neighbour block is not set
     * - the neighbour block is transparent and the asking block is not transparent
     * - the neighbour block are leaves and the asking block are also leaves
     * - neighbour block is not a cube
     */
    private static class DefaultFaceVisibleFunction implements BiFunction<Block, Block, Boolean> {

        private final Set<String> cubeShapes = new HashSet<>();

        public DefaultFaceVisibleFunction() {
            cubeShapes.add(ShapeIds.CUBE);
            cubeShapes.add(ShapeIds.CUBE_DOWN);
            cubeShapes.add(ShapeIds.CUBE_NORTH);
            cubeShapes.add(ShapeIds.CUBE_EAST);
            cubeShapes.add(ShapeIds.CUBE_SOUTH);
            cubeShapes.add(ShapeIds.CUBE_WEST);
            cubeShapes.add(ShapeIds.SQUARE_CUBOID_NINE_TENTHS);
        }

        @Override
        public Boolean apply(Block block, Block neighbour) {
            if (neighbour == null) {
                return true;
            }
            if (neighbour.isTransparent() && !block.isTransparent()) {
                return true;
            }
            if (block.getName().contains("leaves") && neighbour.getName().contains("leaves")) {
                return true;
            }
            if (!cubeShapes.contains(neighbour.getShape())) {
                return true;
            }
            return false;
        }

    }

}
