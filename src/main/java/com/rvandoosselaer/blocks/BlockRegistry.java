package com.rvandoosselaer.blocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.rvandoosselaer.blocks.serialize.BlockDTO;
import com.rvandoosselaer.blocks.serialize.BlockDefinition;
import com.rvandoosselaer.blocks.serialize.BlockFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread safe register for blocks. The register is used so only one instance of a block is used throughout the Blocks
 * framework.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class BlockRegistry {

    private final ConcurrentMap<String, Block> registry = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    /**
     * Will register default blocks
     */
    public BlockRegistry() {
        this(true);
    }

    public BlockRegistry(boolean registerDefaultBlocks) {
        if (registerDefaultBlocks) {
            registerDefaultBlocks();
        }
    }

    public Block register(@NonNull Block block) {
        return register(block.getName(), block);
    }

    public Block register(@NonNull String name, Block block) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Invalid block name " + name + " specified.");
        }

        registry.put(name, block);
        if (log.isTraceEnabled()) {
            log.trace("Registered block {} -> {}", name, block);
        }
        return block;
    }

    public void register(@NonNull Block... blocks) {
        Arrays.stream(blocks).forEach(this::register);
    }

    public void register(@NonNull Collection<Block> collection) {
        collection.forEach(this::register);
    }

    public boolean remove(@NonNull Block block) {
        return remove(block.getName());
    }

    public boolean remove(@NonNull String name) {
        if (registry.containsKey(name)) {
            Block block = registry.remove(name);
            if (log.isTraceEnabled()) {
                log.trace("Removed block {} -> {}", name, block);
            }
            return true;
        }
        return false;
    }

    public Block get(@NonNull String name) {
        if (BlockIds.NONE.equals(name)) {
            return null;
        }

        Block b = registry.get(name);
        if (b == null) {
            log.warn("No block registered with name {}", name);
        }
        return b;
    }

    public void clear() {
        registry.clear();
    }

    public Collection<Block> getAll() {
        return Collections.unmodifiableCollection(registry.values());
    }

    public void registerDefaultBlocks() {
        registerBirchBlocks();
        registerBrickBlocks();
        registerCobbleStoneBlocks();
        registerDirtBlocks();
        registerGravelBlocks();
        registerGrassBlocks();
        registerPalmTreeBlocks();
        registerRockBlocks();
        registerOakBlocks();
        registerSandBlocks();
        registerSnowBlocks();
        registerSpruceBlocks();
        registerStoneBrickBlocks();
        registerWaterBlocks();
        registerWindowBlocks();
    }

    public void load(InputStream inputStream) {
        try {
            List<BlockDTO> blocks = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, BlockDTO.class));
            if (log.isTraceEnabled()) {
                log.trace("Loaded {} blocks from inputstream.", blocks.size());
            }
            blocks.forEach(blockDTO -> register(Block.createFrom(blockDTO)));
        } catch (IOException e) {
            log.error("Unable to read inputstream. Error: {}", e.getMessage(), e);
        }
    }

    private void registerWindowBlocks() {
        BlockDefinition windowDef = new BlockDefinition(TypeIds.WINDOW, true, true, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_SQUARES);
        register(BlockFactory.create(windowDef));
    }

    private void registerWaterBlocks() {
        Block water = new Block(BlockIds.WATER, ShapeIds.SQUARE_CUBOID_NINE_TENTHS, TypeIds.WATER, false, true, false);
        Block waterStill = new Block(BlockIds.WATER_STILL, ShapeIds.SQUARE_CUBOID_NINE_TENTHS, TypeIds.WATER_STILL, false, true, false);
        register(water, waterStill);
    }

    private void registerStoneBrickBlocks() {
        BlockDefinition stoneBrickDef = new BlockDefinition(TypeIds.STONE_BRICKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(stoneBrickDef));

        BlockDefinition mossyStoneBrickDef = new BlockDefinition(TypeIds.STONE_BRICKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(mossyStoneBrickDef));
    }

    private void registerSpruceBlocks() {
        BlockDefinition spruceLogDef = new BlockDefinition(TypeIds.SPRUCE_LOG, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES);
        register(BlockFactory.create(spruceLogDef));

        BlockDefinition sprucePlankDef = new BlockDefinition(TypeIds.SPRUCE_PLANKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(sprucePlankDef));

        Block spruceLeaves = new Block(BlockIds.SPRUCE_LEAVES, ShapeIds.CUBE, TypeIds.SPRUCE_LEAVES, false, true, true);
        register(spruceLeaves);
    }

    private void registerSnowBlocks() {
        BlockDefinition snowDef = new BlockDefinition(TypeIds.SNOW, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(snowDef));
    }

    private void registerSandBlocks() {
        BlockDefinition sandDef = new BlockDefinition(TypeIds.SAND, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(sandDef));
    }

    private void registerOakBlocks() {
        BlockDefinition oakLogDef = new BlockDefinition(TypeIds.OAK_LOG, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES);
        register(BlockFactory.create(oakLogDef));

        BlockDefinition oakPlankDef = new BlockDefinition(TypeIds.OAK_PLANKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(oakPlankDef));

        Block oakLeaves = new Block(BlockIds.OAK_LEAVES, ShapeIds.CUBE, TypeIds.OAK_LEAVES, false, true, true);
        register(oakLeaves);
    }

    private void registerRockBlocks() {
        BlockDefinition rockDef = new BlockDefinition(TypeIds.ROCK, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(rockDef));
    }

    private void registerPalmTreeBlocks() {
        BlockDefinition palmTreeLogDef = new BlockDefinition(TypeIds.PALM_TREE_LOG, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES);
        register(BlockFactory.create(palmTreeLogDef));

        BlockDefinition palmTreePlankDef = new BlockDefinition(TypeIds.PALM_TREE_PLANKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(palmTreePlankDef));

        Block palmTreeLeaves = new Block(BlockIds.PALM_TREE_LEAVES, ShapeIds.CUBE, TypeIds.PALM_TREE_LEAVES, false, true, true);
        register(palmTreeLeaves);
    }

    private void registerGrassBlocks() {
        BlockDefinition grassDef = new BlockDefinition(TypeIds.GRASS, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS);
        register(BlockFactory.create(grassDef));

        BlockDefinition snowedGrassDef = new BlockDefinition(TypeIds.GRASS_SNOW, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS);
        register(BlockFactory.create(snowedGrassDef));
    }

    private void registerGravelBlocks() {
        BlockDefinition gravelDef = new BlockDefinition(TypeIds.GRAVEL, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(gravelDef));
    }

    private void registerDirtBlocks() {
        BlockDefinition dirtDef = new BlockDefinition(TypeIds.DIRT, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(dirtDef));
    }

    private void registerCobbleStoneBlocks() {
        BlockDefinition cobbleStoneDef = new BlockDefinition(TypeIds.COBBLESTONE, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(cobbleStoneDef));

        BlockDefinition mossyCobbleStoneDef = new BlockDefinition(TypeIds.MOSSY_COBBLESTONE, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(mossyCobbleStoneDef));
    }

    private void registerBrickBlocks() {
        BlockDefinition brickDef = new BlockDefinition(TypeIds.BRICKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(brickDef));
    }

    private void registerBirchBlocks() {
        BlockDefinition birchLogDef = new BlockDefinition(TypeIds.BIRCH_LOG, true, false, true)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES);
        register(BlockFactory.create(birchLogDef));

        BlockDefinition birchPlankDef = new BlockDefinition(TypeIds.BIRCH_PLANKS, true, false, false)
                .addShapes(ShapeIds.CUBE)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ROUNDED_CUBE)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(birchPlankDef));

        Block birchLeaves = new Block(BlockIds.BIRCH_LEAVES, ShapeIds.CUBE, TypeIds.BIRCH_LEAVES, false, true, true);
        register(birchLeaves);
    }

}
