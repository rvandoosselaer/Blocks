package com.rvandoosselaer.blocks;

import lombok.*;

/**
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

    public static final String EMPTY = "";

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;
    private String shape;
    private String type;
    private boolean usingMultipleImages;
    private boolean transparent;
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
                .shape("cube")
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
                .shape("cube")
                .type(type)
                .usingMultipleImages(multipleImages)
                .transparent(false)
                .solid(true)
                .build();
    }

    /**
     * The name of the block
     *
     * @return name
     */
    //String getName();

    /**
     * The shape of the block
     *
     * @return shape
     */
    //String getShape();

    /**
     * The type of the block
     *
     * @return type
     */
    //String getType();

    /**
     * Flag indicating if the shape of the block should use multiple images in the texture.
     * When set to true, the {@link Shape} implementation should take care for the correct UV mapping.
     *
     * @return true when multiple images in the texture are used
     */
    //boolean isUsingMultipleImages();

    /**
     * Flag indicating if the block is transparent. Adjacent transparent blocks don't render the shared face between
     * them. When a non-transparent block is next to a transparent block, the face of the non-transparent
     * block next to the transparent block is rendered.
     * Given blocks A and B are placed next to each other: |  A  ||  B  |
     * If A is transparent and B is transparent, the faces between A and B (right face for A, left face for B) are not
     * rendered.
     * If A is non-transparent and B is transparent, the right face of A is rendered, the left face of B is not rendered.
     *
     * @return true if the block is transparent
     */
    //boolean isTransparent();

    /**
     * Flag indicating if the block will be part of the collision mesh of the chunk.
     *
     * @return true if block is solid
     */
    //boolean isSolid();

}
