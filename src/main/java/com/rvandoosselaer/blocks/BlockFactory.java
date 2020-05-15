package com.rvandoosselaer.blocks;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * A factory class that produces blocks from a BlockDefinition.
 *
 * @author: rvandoosselaer
 */
public class BlockFactory {

    public static Collection<Block> create(BlockDefinition blockDefinition) {
        Collection<Block> blocks = new HashSet<>();
        for (String shape : blockDefinition.getShapes()) {
            Block block = Block.builder()
                    .name(getName(shape, blockDefinition.getType()))
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

    private static String getName(String shape, String type) {
        return ShapeIds.CUBE.equals(shape) ? type : type + "-" + shape;
    }

    @Getter
    @Setter
    public static class BlockDefinition {

        private String type;
        private Collection<String> shapes = new HashSet<>();
        private boolean transparent;
        private boolean solid;
        private boolean multiTexture;

        @Builder
        public BlockDefinition(String type, boolean transparent, boolean solid, boolean multiTexture) {
            this.type = type;
            this.transparent = transparent;
            this.solid = solid;
            this.multiTexture = multiTexture;
        }

        public BlockDefinition addShape(String shape) {
            shapes.add(shape);
            return this;
        }

        public BlockDefinition addShapes(String... shapes) {
            Arrays.stream(shapes).forEach(this::addShape);
            return this;
        }

    }
}
