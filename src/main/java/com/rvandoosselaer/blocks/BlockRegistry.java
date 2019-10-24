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

    public Block register(@NonNull String name, @NonNull Block block) {
        registry.put(name, block);
        if (log.isTraceEnabled()) {
            log.trace("Registered block {} -> {}", name, block);
        }
        return block;
    }

    public Block get(@NonNull String block) {
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
        // grass
        register(Block.builder()
                .name("grass")
                .shape("cube")
                .type("grass")
                .usingMultipleImages(true)
                .transparent(false)
                .solid(true)
                .build());
        // sand
        register(Block.builder()
                .name("sand")
                .shape("cube")
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
        // stonebrick merlon
        register(Block.builder()
                .name("stonebrick-merlon")
                .shape("pyramid")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // stonebrick wedge-front
        register(Block.builder()
                .name("stonebrick-wedge-front")
                .shape("wedge-front")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // stonebrick wedge-right
        register(Block.builder()
                .name("stonebrick-wedge-right")
                .shape("wedge-right")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // stonebrick wedge-back
        register(Block.builder()
                .name("stonebrick-wedge-back")
                .shape("wedge-back")
                .type("stonebrick")
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build());
        // stonebrick wedge-left
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
