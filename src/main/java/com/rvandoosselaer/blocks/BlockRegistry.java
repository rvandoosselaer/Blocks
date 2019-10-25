package com.rvandoosselaer.blocks;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread safe register for blocks.
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
            throw new IllegalArgumentException("Invalid name " + name + " specified.");
        }

        registry.put(name, block);
        if (log.isTraceEnabled()) {
            log.trace("Registered block {} -> {}", name, block);
        }
        return block;
    }

    public Block get(@NonNull String block) {
        if (Block.EMPTY.equals(block)) {
            return null;
        }

        Block b = registry.get(block);
        if (b == null) {
            log.warn("No block registered with name {}", block);
        }
        return b;
    }

    public void clear() {
        registry.clear();
    }

    public Collection<Block> getAll() {
        return registry.values();
    }

    public void registerDefaultBlocks() {
        // cubes
        register(Block.create("grass", "grass", true));
        register(Block.create("dirt", "dirt"));
        register(Block.create("stone", "stone"));
        register(Block.create("stonebrick", "stonebrick"));
        register(Block.create("oak", "oak", true));
        register(Block.create("sand", "sand"));
        // slabs
        register(Block.builder()
                .name("grass-slab")
                .shape(Shape.SLAB)
                .type("grass")
                .usingMultipleImages(true)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("dirt-slab")
                .shape(Shape.SLAB)
                .type("dirt")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("stone-slab")
                .shape(Shape.SLAB)
                .type("stone")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("stonebrick-slab")
                .shape(Shape.SLAB)
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("oak-slab")
                .shape(Shape.SLAB)
                .type("oak")
                .usingMultipleImages(true)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("sand-slab")
                .shape(Shape.SLAB)
                .type("sand")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // double slabs
        register(Block.builder()
                .name("double-grass-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("grass")
                .usingMultipleImages(true)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("double-dirt-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("dirt")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("double-stone-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("stone")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("double-stonebrick-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("double-oak-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("oak")
                .usingMultipleImages(true)
                .transparent(false)
                .solid(true)
                .build());
        register(Block.builder()
                .name("double-sand-slab")
                .shape(Shape.DOUBLE_SLAB)
                .type("sand")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // water
        register(Block.builder()
                .name("water")
                .shape("cube")
                .type("water")
                .usingMultipleImages(false)
                .transparent(true)
                .solid(false)
                .build());
        register(Block.builder()
                .name("water-still")
                .shape("cube")
                .type("water-still")
                .usingMultipleImages(false)
                .transparent(true)
                .solid(false)
                .build());
        // merlon
        register(Block.builder()
                .name("stonebrick-merlon")
                .shape("pyramid")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // wedge-front
        register(Block.builder()
                .name("stonebrick-wedge-front")
                .shape("wedge-front")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // wedge-right
        register(Block.builder()
                .name("stonebrick-wedge-right")
                .shape("wedge-right")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // wedge-back
        register(Block.builder()
                .name("stonebrick-wedge-back")
                .shape("wedge-back")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // wedge-left
        register(Block.builder()
                .name("stonebrick-wedge-left")
                .shape("wedge-left")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
    }

}
