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

    public BlockRegistry() {
        registerDefaultBlocks();
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
        registerBlocks(TypeIds.WINDOW, true, true, false);
    }

    private void registerWaterBlocks() {
        Block water = new Block(BlockIds.getName(TypeIds.WATER, ShapeIds.CUBE), ShapeIds.CUBE, TypeIds.WATER, false, true, false);
        Block waterStill = new Block(BlockIds.getName(TypeIds.WATER_STILL, ShapeIds.CUBE), ShapeIds.CUBE, TypeIds.WATER_STILL, false, true, false);
        register(water, waterStill);
    }

    private void registerStoneBrickBlocks() {
        registerBlocks(TypeIds.STONE_BRICKS, true, false, false);
        registerBlocks(TypeIds.MOSSY_STONE_BRICKS, true, false, false);
    }

    private void registerSpruceBlocks() {
        registerBlocks(TypeIds.SPRUCE_LOG, true, false, true);
        registerBlocks(TypeIds.SPRUCE_PLANKS, true, false, false);
        registerBlocks(TypeIds.SPRUCE_LEAVES, true, true, false);
    }

    private void registerSnowBlocks() {
        registerBlocks(TypeIds.SNOW, true, false, false);
    }

    private void registerSandBlocks() {
        registerBlocks(TypeIds.SAND, true, false, false);
    }

    private void registerOakBlocks() {
        registerBlocks(TypeIds.OAK_LOG, true, false, true);
        registerBlocks(TypeIds.OAK_PLANKS, true, false, false);
        registerBlocks(TypeIds.OAK_LEAVES, true, true, false);
    }

    private void registerRockBlocks() {
        registerBlocks(TypeIds.ROCK, true, false, false);
    }

    private void registerPalmTreeBlocks() {
        registerBlocks(TypeIds.PALM_TREE_LOG, true, false, true);
        registerBlocks(TypeIds.PALM_TREE_PLANKS, true, false, false);
        registerBlocks(TypeIds.PALM_TREE_LEAVES, true, true, false);
    }

    private void registerGrassBlocks() {
        registerBlocks(TypeIds.GRASS, true, false, true);
        registerBlocks(TypeIds.GRASS_SNOW, true, false, true);
    }

    private void registerGravelBlocks() {
        registerBlocks(TypeIds.GRAVEL, true, false, false);
    }

    private void registerDirtBlocks() {
        registerBlocks(TypeIds.DIRT, true, false, false);
    }

    private void registerCobbleStoneBlocks() {
        registerBlocks(TypeIds.COBBLESTONE, true, false, false);
        registerBlocks(TypeIds.MOSSY_COBBLESTONE, true, false, false);
    }

    private void registerBrickBlocks() {
        registerBlocks(TypeIds.BRICKS, true, false, false);
    }

    private void registerBirchBlocks() {
        registerBlocks(TypeIds.BIRCH_LOG, true, false, true);
        registerBlocks(TypeIds.BIRCH_PLANKS, true, false, false);
        registerBlocks(TypeIds.BIRCH_LEAVES, true, true, false);
    }

    private void registerBlocks(String type, boolean solid, boolean transparent, boolean multiTexture) {
        BlockDefinition blockDefinition = new BlockDefinition(type, solid, transparent, multiTexture)
                .addShapes(ShapeIds.ALL_CUBES)
                .addShapes(ShapeIds.ALL_PYRAMIDS)
                .addShapes(ShapeIds.ALL_WEDGES)
                .addShapes(ShapeIds.ALL_POLES)
                .addShapes(ShapeIds.ALL_ROUNDED_CUBES)
                .addShapes(ShapeIds.ALL_SLABS)
                .addShapes(ShapeIds.ALL_DOUBLE_SLABS)
                .addShapes(ShapeIds.ALL_PLATES)
                .addShapes(ShapeIds.ALL_SQUARES)
                .addShapes(ShapeIds.ALL_STAIRS);
        register(BlockFactory.create(blockDefinition));
    }

}
