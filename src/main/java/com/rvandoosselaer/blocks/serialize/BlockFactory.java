package com.rvandoosselaer.blocks.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * A factory class that produces blocks from a BlockDefinition.
 *
 * @author: rvandoosselaer
 */
@Slf4j
public class BlockFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public static Collection<Block> create(@NonNull BlockDefinition blockDefinition) {
        Collection<Block> blocks = new HashSet<>();
        for (String shape : blockDefinition.getShapes()) {
            Block block = Block.builder()
                    .name(BlockIds.getName(blockDefinition.getType(), shape))
                    .type(blockDefinition.getType())
                    .shape(shape)
                    .solid(blockDefinition.isSolid())
                    .transparent(blockDefinition.isTransparent())
                    .usingMultipleImages(blockDefinition.isMultiTexture())
                    .build();
            blocks.add(block);
        }

        return blocks;
    }

    public static Collection<Block> create(@NonNull Collection<BlockDefinition> blockDefinitions) {
        Collection<Block> blocks = null;
        for (BlockDefinition blockDefinition : blockDefinitions) {
            if (blocks == null) {
                blocks = create(blockDefinition);
            } else {
                blocks.addAll(create(blockDefinition));
            }
        }
        return blocks;
    }

    public static Collection<Block> create(InputStream inputStream) {
        try {
            Collection<BlockDefinition> blockDefinitions = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(Collection.class, BlockDefinition.class));
            if (log.isTraceEnabled()) {
                log.trace("Loaded {} block definitions from inputstream.", blockDefinitions.size());
            }
            return create(blockDefinitions);
        } catch (IOException e) {
            log.error("Unable to read inputstream. Error: {}", e.getMessage(), e);
        }

        return Collections.emptySet();
    }

}
