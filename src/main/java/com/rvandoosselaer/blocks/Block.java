package com.rvandoosselaer.blocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The basic building block of the Blocks framework. A block has a name and some properties describing the look and feel
 * of the block in the resulting chunk node.
 * The shape of the block defines the form (vertices, normals, tangents, UV coordinates, ...)  of the block, the type
 * defines the look (material, images, ...).
 *
 * @author rvandoosselaer
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Block {

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;
    private String shape;
    private String type;
    /**
     * Flag indicating if the shape of the block should use multiple images in the texture.
     * When set to true, the {@link Shape} implementation should take care for the correct UV mapping.
     */
    private boolean usingMultipleImages;
    /**
     * Flag indicating if the block is transparent. Adjacent transparent blocks don't render the shared face between
     * them. When a non-transparent block is next to a transparent block, the face of the non-transparent
     * block next to the transparent block is rendered.
     * Given blocks A and B are placed next to each other: |  A  ||  B  |
     * If A is transparent and B is transparent, the faces between A and B (right face for A, left face for B) are not
     * rendered.
     * If A is non-transparent and B is transparent, the right face of A is rendered, the left face of B is not rendered.
     */
    private boolean transparent;
    /**
     * Flag indicating if the block will be part of the collision mesh of the chunk.
     */
    private boolean solid;

    /**
     * Creates a cube shape, non transparent, solid block using a single image.
     *
     * @param name
     * @param type
     * @return block
     */
    public static Block create(String name, String type) {
        return Block.builder()
                .name(name)
                .shape(ShapeIds.CUBE)
                .type(type)
                .usingMultipleImages(false)
                .transparent(false)
                .solid(true)
                .build();
    }

    /**
     * Creates a cube shape, non transparent, solid block.
     *
     * @param name
     * @param type
     * @return block
     */
    public static Block create(String name, String type, boolean multipleImages) {
        return Block.builder()
                .name(name)
                .shape(ShapeIds.CUBE)
                .type(type)
                .usingMultipleImages(multipleImages)
                .transparent(false)
                .solid(true)
                .build();
    }

}
