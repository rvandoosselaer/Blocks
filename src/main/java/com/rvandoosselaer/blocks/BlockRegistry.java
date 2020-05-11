package com.rvandoosselaer.blocks;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
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

        registerMossyCobbleStoneBlocks();

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

        registerMossyStoneBrickBlocks();

        registerWaterBlocks();
    }

    private void registerWaterBlocks() {
        register(Block.builder().name(BlockIds.WATER).shape(ShapeIds.CUBE).type(TypeIds.WATER).usingMultipleImages(false).solid(false).transparent(true).build());
        register(Block.builder().name(BlockIds.WATER_STILL).shape(ShapeIds.CUBE).type(TypeIds.WATER_STILL).usingMultipleImages(false).solid(false).transparent(true).build());
    }

    private void registerMossyStoneBrickBlocks() {
        register(Block.create(BlockIds.MOSSY_STONE_BRICKS, TypeIds.MOSSY_STONE_BRICKS, false));
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_SLAB).shape(ShapeIds.SLAB).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_STONE_BRICK_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.MOSSY_STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
    }
    
    private void registerStoneBrickBlocks() {
        register(Block.create(BlockIds.STONE_BRICKS, TypeIds.STONE_BRICKS, false));
        register(Block.builder().name(BlockIds.STONE_BRICKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_SLAB).shape(ShapeIds.SLAB).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.STONE_BRICK_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.STONE_BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerSpruceBlocks() {
        register(Block.create(BlockIds.SPRUCE_LOG, TypeIds.SPRUCE_LOG, true));
        register(Block.builder().name(BlockIds.SPRUCE_LOG_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_SLAB).shape(ShapeIds.SLAB).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_LOG_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.SPRUCE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());

        register(Block.create(BlockIds.SPRUCE_PLANKS, TypeIds.SPRUCE_PLANKS, false));
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_SLAB).shape(ShapeIds.SLAB).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_PLATE).shape(ShapeIds.PLATE).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SPRUCE_PLANKS_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.SPRUCE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());

        register(Block.builder().name(BlockIds.SPRUCE_LEAVES).shape(ShapeIds.CUBE).type(TypeIds.SPRUCE_LEAVES).usingMultipleImages(false).solid(true).transparent(true).build());
    }

    private void registerSnowBlocks() {
        register(Block.create(BlockIds.SNOW, TypeIds.SNOW, false));
        register(Block.builder().name(BlockIds.SNOW_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.SNOW).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SNOW_SLAB).shape(ShapeIds.SLAB).type(TypeIds.SNOW).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SNOW_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.SNOW).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerSandBlocks() {
        register(Block.create(BlockIds.SAND, TypeIds.SAND, false));
        register(Block.builder().name(BlockIds.SAND_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.SAND).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SAND_SLAB).shape(ShapeIds.SLAB).type(TypeIds.SAND).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.SAND_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.SAND).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerOakBlocks() {
        register(Block.create(BlockIds.OAK_LOG, TypeIds.OAK_LOG, true));
        register(Block.builder().name(BlockIds.OAK_LOG_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_SLAB).shape(ShapeIds.SLAB).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_LOG_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.OAK_LOG).usingMultipleImages(true).solid(true).transparent(false).build());

        register(Block.create(BlockIds.OAK_PLANKS, TypeIds.OAK_PLANKS, false));
        register(Block.builder().name(BlockIds.OAK_PLANKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_SLAB).shape(ShapeIds.SLAB).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_PLATE).shape(ShapeIds.PLATE).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.OAK_PLANKS_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.OAK_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());

        register(Block.builder().name(BlockIds.OAK_LEAVES).shape(ShapeIds.CUBE).type(TypeIds.OAK_LEAVES).usingMultipleImages(false).solid(true).transparent(true).build());
    }

    private void registerRockBlocks() {
        register(Block.create(BlockIds.ROCK, TypeIds.ROCK, false));
        register(Block.builder().name(BlockIds.ROCK_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_SLAB).shape(ShapeIds.SLAB).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.ROCK_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.ROCK).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerPalmTreeBlocks() {
        register(Block.create(BlockIds.PALM_TREE_LOG, TypeIds.PALM_TREE_LOG, true));
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_SLAB).shape(ShapeIds.SLAB).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_LOG_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.PALM_TREE_LOG).usingMultipleImages(true).solid(true).transparent(false).build());

        register(Block.create(BlockIds.PALM_TREE_PLANKS, TypeIds.PALM_TREE_PLANKS, false));
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_SLAB).shape(ShapeIds.SLAB).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_PLATE).shape(ShapeIds.PLATE).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.PALM_TREE_PLANKS_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.PALM_TREE_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());

        register(Block.builder().name(BlockIds.PALM_TREE_LEAVES).shape(ShapeIds.CUBE).type(TypeIds.PALM_TREE_LEAVES).usingMultipleImages(false).solid(true).transparent(true).build());
    }

    private void registerGrassBlocks() {
        register(Block.create(BlockIds.GRASS, TypeIds.GRASS, true));
        register(Block.create(BlockIds.GRASS_SNOW, TypeIds.GRASS_SNOW, true));
        register(Block.builder().name(BlockIds.GRASS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.GRASS).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.GRASS_SNOW_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.GRASS_SNOW).usingMultipleImages(true).solid(true).transparent(false).build());
    }

    private void registerGravelBlocks() {
        register(Block.create(BlockIds.GRAVEL, TypeIds.GRAVEL, false));
        register(Block.builder().name(BlockIds.GRAVEL_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.GRAVEL).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.GRAVEL_SLAB).shape(ShapeIds.SLAB).type(TypeIds.GRAVEL).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.GRAVEL_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.GRAVEL).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerDirtBlocks() {
        register(Block.create(BlockIds.DIRT, TypeIds.DIRT, false));
        register(Block.builder().name(BlockIds.DIRT_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.DIRT).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.DIRT_SLAB).shape(ShapeIds.SLAB).type(TypeIds.DIRT).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.DIRT_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.DIRT).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerMossyCobbleStoneBlocks() {
        register(Block.create(BlockIds.MOSSY_COBBLESTONE, TypeIds.MOSSY_COBBLESTONE, false));
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_SLAB).shape(ShapeIds.SLAB).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.MOSSY_COBBLESTONE_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.MOSSY_COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
    }
    
    private void registerCobbleStoneBlocks() {
        register(Block.create(BlockIds.COBBLESTONE, TypeIds.COBBLESTONE, false));
        register(Block.builder().name(BlockIds.COBBLESTONE_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_SLAB).shape(ShapeIds.SLAB).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.COBBLESTONE_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.COBBLESTONE).usingMultipleImages(false).solid(true).transparent(false).build());
    }
    
    private void registerBrickBlocks() {
        register(Block.create(BlockIds.BRICKS, TypeIds.BRICKS, false));
        register(Block.builder().name(BlockIds.BRICKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_SLAB).shape(ShapeIds.SLAB).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BRICK_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.BRICKS).usingMultipleImages(false).solid(true).transparent(false).build());
    }

    private void registerBirchBlocks() {
        register(Block.create(BlockIds.BIRCH_LOG, TypeIds.BIRCH_LOG, true));
        register(Block.builder().name(BlockIds.BIRCH_LOG_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_SLAB).shape(ShapeIds.SLAB).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_LOG_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.BIRCH_LOG).usingMultipleImages(true).solid(true).transparent(false).build());

        register(Block.create(BlockIds.BIRCH_PLANKS, TypeIds.BIRCH_PLANKS, false));
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_ROUNDED).shape(ShapeIds.ROUNDED_CUBE).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_SLAB).shape(ShapeIds.SLAB).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_DOUBLE_SLAB).shape(ShapeIds.DOUBLE_SLAB).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_PLATE).shape(ShapeIds.PLATE).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_BACK).shape(ShapeIds.STAIRS_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INNER_CORNER_BACK).shape(ShapeIds.STAIRS_INNER_CORNER_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_OUTER_CORNER_BACK).shape(ShapeIds.STAIRS_OUTER_CORNER_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INVERTED_BACK).shape(ShapeIds.STAIRS_INVERTED_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_FRONT).shape(ShapeIds.STAIRS_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INNER_CORNER_FRONT).shape(ShapeIds.STAIRS_INNER_CORNER_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_OUTER_CORNER_FRONT).shape(ShapeIds.STAIRS_OUTER_CORNER_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INVERTED_FRONT).shape(ShapeIds.STAIRS_INVERTED_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_LEFT).shape(ShapeIds.STAIRS_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INNER_CORNER_LEFT).shape(ShapeIds.STAIRS_INNER_CORNER_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_OUTER_CORNER_LEFT).shape(ShapeIds.STAIRS_OUTER_CORNER_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INVERTED_LEFT).shape(ShapeIds.STAIRS_INVERTED_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_RIGHT).shape(ShapeIds.STAIRS_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INNER_CORNER_RIGHT).shape(ShapeIds.STAIRS_INNER_CORNER_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_OUTER_CORNER_RIGHT).shape(ShapeIds.STAIRS_OUTER_CORNER_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_STAIRS_INVERTED_RIGHT).shape(ShapeIds.STAIRS_INVERTED_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_BACK).shape(ShapeIds.WEDGE_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_INVERTED_BACK).shape(ShapeIds.WEDGE_INVERTED_BACK).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_FRONT).shape(ShapeIds.WEDGE_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_INVERTED_FRONT).shape(ShapeIds.WEDGE_INVERTED_FRONT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_LEFT).shape(ShapeIds.WEDGE_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_INVERTED_LEFT).shape(ShapeIds.WEDGE_INVERTED_LEFT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_RIGHT).shape(ShapeIds.WEDGE_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_WEDGE_INVERTED_RIGHT).shape(ShapeIds.WEDGE_INVERTED_RIGHT).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_PYRAMID).shape(ShapeIds.PYRAMID).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());
        register(Block.builder().name(BlockIds.BIRCH_PLANKS_PYRAMID_INVERTED).shape(ShapeIds.PYRAMID_INVERTED).type(TypeIds.BIRCH_PLANKS).usingMultipleImages(false).solid(true).transparent(false).build());

        register(Block.builder().name(BlockIds.BIRCH_LEAVES).shape(ShapeIds.CUBE).type(TypeIds.BIRCH_LEAVES).usingMultipleImages(false).solid(true).transparent(true).build());
    }

}
