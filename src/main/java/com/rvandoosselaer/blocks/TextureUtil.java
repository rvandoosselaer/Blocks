package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetKey;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class that combines textures into 1 texture. It's used to combine the textures of all faces into one
 * texture. With the method #getUv(Direction) the uv coordinates of a texture for a direction can be found.
 * The UV coordinates of the texture of a face can be
 * retrieved by Direction. The returned {@link TextureLocation} object has a min(x,y) and max(x,y) field.
 * The {@link #getTexture()} and {@link #convertImageToAwt(Image)} methods are copied from {@link jme3tools.optimize.TextureAtlas}.
 */
@Slf4j
public class TextureUtil {

    private final Map<AssetKey<?>, TextureLocation> textureLocations = new HashMap<>();
    private final Map<Direction, AssetKey<?>> directions = new HashMap<>();
    private final Map<AssetKey<?>, Texture> textures = new HashMap<>();

    private int currentX = 0;
    private int currentY = 0;
    private Texture texture;

    public TextureUtil() {
    }

    // set the texture on all directions
    public TextureUtil(Texture texture) {
        Arrays.stream(Direction.values())
                .forEach(direction -> addTexture(direction, texture));
    }

    //TODO:
    // - check if we need to clean up the maps: don't want to hold on to all the textures
    public TextureCoordinates getTextureCoordinates(Direction direction) {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("Trying to calculate texture coordinates before the texture is created.");
        }

        AssetKey<?> assetKey = directions.get(direction);
        TextureLocation textureLocation = textureLocations.get(assetKey);

        float minX = textureLocation.min.x / (float) width;
        float maxX = textureLocation.max.x / (float) width;
        float minY = textureLocation.min.y / (float) height;
        float maxY = textureLocation.max.y / (float) height;

        return new TextureCoordinates(minX, maxX, minY, maxY);
    }

    public Function<Direction, TextureCoordinates> createTextureCoordinatesFunction() {
        return this::getTextureCoordinates;
    }

    public void addTexture(Direction direction, Texture texture) {
        //TODO:
        // - check size of texture
        // - check colorspace of texture
        log.trace("Adding {} at direction {} to atlas.", texture.getKey(), direction);
        if (textureLocations.containsKey(texture.getKey())) {
            log.trace("AssetKey[{}] already present in atlas.", texture.getKey());
            directions.put(direction, texture.getKey());
            return;
        }

        textures.put(texture.getKey(), texture);
        directions.put(direction, texture.getKey());
        textureLocations.put(texture.getKey(), createTextureLocation(texture));
        // reset the texture, it should be regenerated
        this.texture = null;
    }

    public Texture getTexture() {
        if (texture != null) {
            return texture;
        }

        // if there is only 1 texture, no need to recreate it
        if (textures.size() == 1) {
            texture = textures.values().iterator().next();
            return texture;
        }

        int textureWidth = calculateTextureWidth();
        int textureHeight = calculateTextureHeight();
        byte[] image = new byte[textureWidth * textureHeight * 4];

        for (Map.Entry<AssetKey<?>, Texture> textureEntry : textures.entrySet()) {
            AssetKey<?> assetKey = textureEntry.getKey();
            Texture texture = textureEntry.getValue();
            Image source = texture.getImage();
            int x = textureLocations.get(assetKey).min.x;
            int y = textureLocations.get(assetKey).min.y;

            //TODO: all buffers?
            ByteBuffer sourceData = source.getData(0);
            int height = source.getHeight();
            int width = source.getWidth();
            Image newImage = null;
            for (int yPos = 0; yPos < height; yPos++) {
                for (int xPos = 0; xPos < width; xPos++) {
                    int i = ((xPos + x) + (yPos + y) * textureWidth) * 4;
                    if (source.getFormat() == Format.ABGR8) {
                        int j = (xPos + yPos * width) * 4;
                        image[i] = sourceData.get(j); //a
                        image[i + 1] = sourceData.get(j + 1); //b
                        image[i + 2] = sourceData.get(j + 2); //g
                        image[i + 3] = sourceData.get(j + 3); //r
                    } else if (source.getFormat() == Format.BGR8) {
                        int j = (xPos + yPos * width) * 3;
                        image[i] = 1; //a
                        image[i + 1] = sourceData.get(j); //b
                        image[i + 2] = sourceData.get(j + 1); //g
                        image[i + 3] = sourceData.get(j + 2); //r
                    } else if (source.getFormat() == Format.RGB8) {
                        int j = (xPos + yPos * width) * 3;
                        image[i] = 1; //a
                        image[i + 1] = sourceData.get(j + 2); //b
                        image[i + 2] = sourceData.get(j + 1); //g
                        image[i + 3] = sourceData.get(j); //r
                    } else if (source.getFormat() == Format.RGBA8) {
                        int j = (xPos + yPos * width) * 4;
                        image[i] = sourceData.get(j + 3); //a
                        image[i + 1] = sourceData.get(j + 2); //b
                        image[i + 2] = sourceData.get(j + 1); //g
                        image[i + 3] = sourceData.get(j); //r
                    } else if (source.getFormat() == Format.Luminance8) {
                        int j = (xPos + yPos * width) * 1;
                        image[i] = 1; //a
                        image[i + 1] = sourceData.get(j); //b
                        image[i + 2] = sourceData.get(j); //g
                        image[i + 3] = sourceData.get(j); //r
                    } else if (source.getFormat() == Format.Luminance8Alpha8) {
                        int j = (xPos + yPos * width) * 2;
                        image[i] = sourceData.get(j + 1); //a
                        image[i + 1] = sourceData.get(j); //b
                        image[i + 2] = sourceData.get(j); //g
                        image[i + 3] = sourceData.get(j); //r
                    } else {
                        //ImageToAwt conversion
                        if (newImage == null) {
                            newImage = convertImageToAwt(source);
                            if (newImage != null) {
                                source = newImage;
                                sourceData = source.getData(0);
                                int j = (xPos + yPos * width) * 4;
                                image[i] = sourceData.get(j); //a
                                image[i + 1] = sourceData.get(j + 1); //b
                                image[i + 2] = sourceData.get(j + 2); //g
                                image[i + 3] = sourceData.get(j + 3); //r
                            } else {
                                throw new UnsupportedOperationException("Cannot draw or convert textures with format " + source.getFormat());
                            }
                        } else {
                            throw new UnsupportedOperationException("Cannot draw textures with format " + source.getFormat());
                        }
                    }
                }
            }
        }

        //TODO check if color space shouldn't be sRGB
        texture = new Texture2D(new Image(Format.ABGR8, textureWidth, textureHeight, BufferUtils.createByteBuffer(image), null, ColorSpace.Linear));
        texture.setMagFilter(Texture.MagFilter.Bilinear);
        texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        texture.setWrap(Texture.WrapMode.EdgeClamp);
        return texture;
    }

    private int getWidth() {
        return texture != null ? texture.getImage().getWidth() : 0;
    }

    private int getHeight() {
        return texture != null ? texture.getImage().getHeight() : 0;
    }

    private int calculateTextureWidth() {
        return textures.values().stream()
                .findFirst()
                .map(texture -> texture.getImage().getWidth())
                .orElse(0);
    }

    private int calculateTextureHeight() {
        return textures.values().stream()
                .mapToInt(texture -> texture.getImage().getHeight())
                .sum();
    }

    private Image convertImageToAwt(Image source) {
        //use awt dependent classes without actual dependency via reflection
        try {
            Class clazz = Class.forName("jme3tools.converters.ImageToAwt");
            if (clazz == null) {
                return null;
            }
            Image newImage = new Image(Format.ABGR8, source.getWidth(), source.getHeight(), BufferUtils.createByteBuffer(source.getWidth() * source.getHeight() * 4), null, ColorSpace.Linear);
            clazz.getMethod("convert", Image.class, Image.class).invoke(clazz.newInstance(), source, newImage);
            return newImage;
        } catch (InstantiationException
                 | IllegalAccessException
                 | IllegalArgumentException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | SecurityException
                 | ClassNotFoundException ex) {
        }
        return null;
    }

    private TextureLocation createTextureLocation(Texture texture) {
        int imageWidth = texture.getImage().getWidth();
        int imageHeight = texture.getImage().getHeight();

//        if (currentY >= height) {
//            throw new IllegalStateException(String.format("Invalid size specified for texture: %dx%d. Image could not be added as the texture is already full", width, height));
//        }

        Vector2i min = new Vector2i(currentX, currentY);
        // the x coordinates remains the same, we are adding the images in height
        currentY += imageHeight;
        Vector2i max = new Vector2i(imageWidth, currentY);

        return new TextureLocation(min, max);
    }

    private static class TextureLocation {

        private final Vector2i min;
        private final Vector2i max;

        public TextureLocation(Vector2i min, Vector2i max) {
            this.min = min;
            this.max = max;
        }

        public Vector2i getMin() {
            return min;
        }

        public Vector2i getMax() {
            return max;
        }
    }

    private static class Vector2i {

        private final int x;
        private final int y;

        public Vector2i(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

}
