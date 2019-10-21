package com.rvandoosselaer.blocks;

/**
 * Holds the information of a block element.
 *
 * @author rvandoosselaer
 */
//TODO: move to class
public interface Block {

    /**
     * The name of the block
     *
     * @return name
     */
    String getName();

    /**
     * The shape of the block
     *
     * @return shape
     */
    String getShape();

    /**
     * The type of the block
     *
     * @return type
     */
    String getType();

    /**
     * Flag indicating if the shape of the block should use multiple images in the texture.
     * When set to true, the {@link Shape} implementation should take care for the correct UV mapping.
     *
     * @return true when multiple images in the texture are used
     */
    boolean isUsingMultipleImages();

    /**
     * Flag indicating if the block is transparent. Adjacent transparent blocks don't render the shared face between
     * them. When a non-transparent block is next to a transparent block, the face of the non-transparent
     * block next to the transparent block is rendered.
     * | --- || --- |
     * |  A  ||  B  |
     * | --- || --- |
     * If A is transparent and B is transparent, the faces between A and B (right face for A, left face for B) are not
     * rendered.
     * If A is non-transparent and B is transparent, the right face of A is rendered, the left face of B is not rendered.
     *
     * @return true if the block is transparent
     */
    boolean isTransparent();

    /**
     * Flag indicating if the block will be part of the collision mesh of the chunk. Most solid blocks will be
     * collidable, i.e. you can stand on top of them or bump into them.
     *
     * @return true if block is collidable
     */
    boolean isCollidable();

}
