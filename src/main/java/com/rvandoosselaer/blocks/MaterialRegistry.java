package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A registry for storing and retrieving materials based on the block type.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class MaterialRegistry {

    private enum TextureType {
        DIFFUSE, NORMAL, PARALLAX;
    }

    public static final String GRASS = "grass";
    public static final String DIRT = "dirt";
    public static final String SAND = "sand";
    public static final String WOOD = "wood";
    public static final String STONE = "stone";
    public static final String STONEBRICK = "stonebrick";
    public static final String WATER = "water";
    public static final String CUBE = "cube"; //TODO: remove
    public static final String PYRAMID = "pyramid"; //TODO: remove
    public static final String PLANKS_OAK_ONE_THIRD = "planks-oak-one-third";
    public static final String PLANKS_OAK_TWO_THIRD = "planks-oak-two-third";
    public static final String WEDGE = "wedge"; //TODO: remove

    public static final String DEFAULT_BLOCK_MATERIAL = "Blocks/Materials/default-block.j3m";
    public static final String WATER_MATERIAL = "Blocks/Materials/water.j3m";

    private final Map<String, Material> registry = new HashMap<>();
    private final AssetManager assetManager;
    @Getter
    private BlocksTheme theme;
    @Getter
    private BlocksTheme defaultTheme = new BlocksTheme("Default", "Blocks/Themes/default/blocks/");

    public MaterialRegistry(@NonNull AssetManager assetManager) {
        this(assetManager, null);
    }

    public MaterialRegistry(@NonNull AssetManager assetManager, BlocksTheme theme) {
        this.assetManager = assetManager;
        this.theme = theme;

        registerDefaultMaterials();
    }

    public Material register(@NonNull String type, @NonNull String materialPath) {
        return register(type, load(materialPath));
    }

    public Material register(@NonNull String type, @NonNull Material material) {
        Material m = material.clone();
        //TODO: fetch object holding 3 textures of the same theme
        // TextureWrapper {
        // texture diffuse
        // optional<texture> normal
        // optional<texture> parallax
        // }
        m.setTexture("DiffuseMap", getTextureOrDefault(type, TextureType.DIFFUSE, true));
        Texture normalMapTexture = getTextureOrDefault(type, TextureType.NORMAL, false);
        if (normalMapTexture != null) {
            m.setTexture("NormalMap", normalMapTexture);
        }
        Texture parallaxMapTexture = getTextureOrDefault(type, TextureType.PARALLAX, false);
        if (parallaxMapTexture != null) {
            m.setTexture("ParallaxMap", parallaxMapTexture);
        }
        registry.put(type, m);
        if (log.isTraceEnabled()) {
            log.trace("Registered type {} -> {}", type, material);
        }
        return material;
    }

    public Material get(String type) {
        Material material = registry.get(type);
        if (material == null) {
            log.warn("No material found for type {}", type);
        }
        return material;
    }

    public boolean usingTheme() {
        return theme != null;
    }

    public void clear() {
        registry.clear();
    }

    public void registerDefaultMaterials() {
        register(GRASS, DEFAULT_BLOCK_MATERIAL);
        register(SAND, DEFAULT_BLOCK_MATERIAL);
        register(DIRT, DEFAULT_BLOCK_MATERIAL);
        register(WOOD, DEFAULT_BLOCK_MATERIAL);
        register(STONE, DEFAULT_BLOCK_MATERIAL);
        register(STONEBRICK, DEFAULT_BLOCK_MATERIAL);
        register(WATER, WATER_MATERIAL);
        register(CUBE, DEFAULT_BLOCK_MATERIAL);
        register(PYRAMID, DEFAULT_BLOCK_MATERIAL);
        register(PLANKS_OAK_ONE_THIRD, DEFAULT_BLOCK_MATERIAL);
        register(PLANKS_OAK_TWO_THIRD, DEFAULT_BLOCK_MATERIAL);
        register(WEDGE, DEFAULT_BLOCK_MATERIAL);
    }

    public void setTheme(BlocksTheme theme) {
        this.theme = theme;
        clear();
        registerDefaultMaterials();
    }

    public void setDefaultTheme(@NonNull BlocksTheme defaultTheme) {
        this.defaultTheme = defaultTheme;
        clear();
        registerDefaultMaterials();
    }

    private Material load(String materialPath) {
        return assetManager.loadMaterial(materialPath);
    }

    /**
     * Retrieve the texture for the block in the current theme. When there isn't a theme specified, the default theme
     * is used. When no texture is found, null is returned.
     *
     * @param type          block type (grass, rock, ...)
     * @param textureType   kind of texture (diffuse, normal, parallax)
     * @param failOnMissing throw an {@link AssetNotFoundException} when the texture is not found.
     * @return the texture or null
     */
    private Texture getTextureOrDefault(String type, TextureType textureType, boolean failOnMissing) {
        Optional<Texture> texture;
        if (usingTheme()) {
            texture = getTexture(type, textureType, theme);
            if (texture.isPresent()) {
                return texture.get();
            }
        }

        texture = getTexture(type, textureType, defaultTheme);
        if (failOnMissing && !texture.isPresent()) {
            throw new AssetNotFoundException("Texture " + getTexturePath(type, textureType, defaultTheme) + " not found!");
        }

        return texture.orElse(null);
    }

    /**
     * @param type        block type (grass, rock, ...)
     * @param textureType kind of texture (diffuse, normal, parallax)
     * @param theme
     * @return an optional of the texture
     */
    private Optional<Texture> getTexture(String type, TextureType textureType, BlocksTheme theme) {
        String texture = getTexturePath(type, textureType, theme);
        try {
            return Optional.of(assetManager.loadTexture(new TextureKey(texture)));
        } catch (AssetNotFoundException e) {
            log.warn("Texture {} not found in theme {}", texture, theme);
        }

        return Optional.empty();
    }

    /**
     * @param type        block type (grass, rock, ...)
     * @param textureType kind of texture (diffuse, normal, parallax)
     * @param theme       current theme
     * @return the full path to the texture, used by the assetmanager to load the texture
     */
    private static String getTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return Paths.get(theme.getPath(), getTextureFilename(type, textureType)).toString();
    }

    /**
     * @param type        block type (grass, rock, ...)
     * @param textureType kind of texture (diffuse, normal, parallax)
     * @return the filename of the texture
     */
    private static String getTextureFilename(String type, TextureType textureType) {
        String fileExtension = ".png";
        switch (textureType) {
            case NORMAL:
                return type + "-normal" + fileExtension;
            case PARALLAX:
                return type + "-parallax" + fileExtension;
            default:
                return type + fileExtension;
        }
    }

}
