package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A registry for storing and retrieving materials based on the block type.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class TypeRegistry {

    public static final String DEFAULT_BLOCK_MATERIAL = "Blocks/Materials/default-block.j3m";
    public static final String WATER_MATERIAL = "Blocks/Materials/water.j3m";

    private enum TextureType {
        DIFFUSE, NORMAL, PARALLAX;
    }

    private final ConcurrentMap<String, Material> registry = new ConcurrentHashMap<>();
    private final AssetManager assetManager;
    @Getter
    private BlocksTheme theme;
    @Getter
    private BlocksTheme defaultTheme = new BlocksTheme("Default", "Blocks/Themes/default/blocks/");

    public TypeRegistry(@NonNull AssetManager assetManager) {
        this(assetManager, null);
    }

    public TypeRegistry(@NonNull AssetManager assetManager, BlocksTheme theme) {
        this.assetManager = assetManager;
        this.theme = theme;

        registerDefaultMaterials();
    }

    public Material register(@NonNull String type, @NonNull String materialPath) {
        return register(type, load(materialPath));
    }

    public Material register(@NonNull String type, @NonNull Material material) {
        if (type.isEmpty()) {
            throw new IllegalArgumentException("Invalid type " + type + " specified.");
        }

        Material m = material.clone();

        TexturesWrapper textures = getTexturesOrDefault(type);
        m.setTexture("DiffuseMap", textures.getDiffuseMap());
        textures.getNormalMap().ifPresent(texture -> m.setTexture("NormalMap", texture));
        textures.getParallaxMap().ifPresent(texture -> m.setTexture("ParallaxMap", texture));

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
        register("grass", DEFAULT_BLOCK_MATERIAL);
        register("sand", DEFAULT_BLOCK_MATERIAL);
        register("dirt", DEFAULT_BLOCK_MATERIAL);
        register("oak", DEFAULT_BLOCK_MATERIAL);
        register("stone", DEFAULT_BLOCK_MATERIAL);
        register("stonebrick", DEFAULT_BLOCK_MATERIAL);
        register("water", WATER_MATERIAL);
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
     * Retrieves the textures for the block type in the current theme. When a theme isn't specified, the default theme
     * is used.
     *
     * @param type block type (grass, rock, ...)
     * @return a TextureWrapper object holding the different textures of the block
     */
    private TexturesWrapper getTexturesOrDefault(String type) {
        if (usingTheme()) {
            Optional<Texture> diffuseMap = getTexture(type, TextureType.DIFFUSE, theme);
            if (diffuseMap.isPresent()) {
                return new TexturesWrapper(diffuseMap.get(), getTexture(type, TextureType.NORMAL, theme), getTexture(type, TextureType.PARALLAX, theme));
            }
        }

        Optional<Texture> diffuseMap = getTexture(type, TextureType.DIFFUSE, defaultTheme);
        if (!diffuseMap.isPresent()) {
            throw new AssetNotFoundException("Texture " + getTexturePath(type, TextureType.DIFFUSE, defaultTheme) + " not found!");
        }
        return new TexturesWrapper(diffuseMap.get(), getTexture(type, TextureType.NORMAL, defaultTheme), getTexture(type, TextureType.PARALLAX, defaultTheme));
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
            if (log.isDebugEnabled()) {
                log.debug("Texture {} not found in theme {}", texture, theme);
            }
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

    @Getter
    @RequiredArgsConstructor
    private class TexturesWrapper {

        private final Texture diffuseMap;
        private final Optional<Texture> normalMap;
        private final Optional<Texture> parallaxMap;

    }

}
