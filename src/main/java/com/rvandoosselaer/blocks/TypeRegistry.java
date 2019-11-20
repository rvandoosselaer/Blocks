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
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * A thread safe register for block types. The register is used so only one instance of a type is used throughout the
 * Blocks framework.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class TypeRegistry {

    public static final String DEFAULT_BLOCK_MATERIAL = "Blocks/Materials/default-block.j3m";
    public static final String WATER_MATERIAL = "Blocks/Materials/water.j3m";
    public static final String WATER_STILL_MATERIAL = "Blocks/Materials/water-still.j3m";
    public static final BlocksTheme FAITHFUL_THEME = new BlocksTheme("Faithful", "Blocks/Themes/faithful/");

    private enum TextureType {
        DIFFUSE, NORMAL, PARALLAX;
    }

    private final ConcurrentMap<String, Material> registry = new ConcurrentHashMap<>();
    private final AssetManager assetManager;
    @Getter
    private BlocksTheme theme;
    @Getter
    private BlocksTheme defaultTheme = new BlocksTheme("Soartex Fanver", "Blocks/Themes/default/");

    public TypeRegistry(@NonNull AssetManager assetManager) {
        this(assetManager, null);
    }

    public TypeRegistry(@NonNull AssetManager assetManager, BlocksTheme theme) {
        this.assetManager = assetManager;
        this.theme = theme;

        registerDefaultMaterials();
    }

    public Material register(@NonNull String name, @NonNull String materialPath) {
        return register(name, load(materialPath));
    }

    public Material register(@NonNull String name, @NonNull Material material) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Invalid type name " + name + " specified.");
        }

        Material m = setTextures(name, material.clone());

        registry.put(name, m);
        if (log.isTraceEnabled()) {
            log.trace("Registered type {} -> {}", name, material);
        }
        return material;
    }

    public Material get(String name) {
        Material material = registry.get(name);
        if (material == null) {
            log.warn("No type found for name {}", name);
        }
        return material;
    }

    public boolean usingTheme() {
        return theme != null;
    }

    public void clear() {
        registry.clear();
    }

    public Collection<String> getAll() {
        return registry.keySet();
    }

    public void registerDefaultMaterials() {
        register(TypeIds.BIRCH_LOG, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.BIRCH_PLANKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.BRICKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.COBBLESTONE, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.MOSSY_COBBLESTONE, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.DIRT, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.GRAVEL, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.GRASS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.GRASS_SNOW, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.PALM_TREE_LOG, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.PALM_TREE_PLANKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.ROCK, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.OAK_LOG, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.OAK_PLANKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.SAND, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.SNOW, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.SPRUCE_LOG, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.SPRUCE_PLANKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.STONE_BRICKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.MOSSY_STONE_BRICKS, DEFAULT_BLOCK_MATERIAL);
        register(TypeIds.WATER, WATER_MATERIAL);
        register(TypeIds.WATER_STILL, WATER_STILL_MATERIAL);
    }

    public void setTheme(BlocksTheme theme) {
        this.theme = theme;
        reload();
    }

    public void setDefaultTheme(@NonNull BlocksTheme defaultTheme) {
        this.defaultTheme = defaultTheme;
        reload();
    }

    /**
     * Set the textures on the material based on the current theme or default theme.
     *
     * @param type     block type
     * @param material associated with the block type
     * @return material with updated textures
     */
    private Material setTextures(String type, Material material) {
        TexturesWrapper textures = getTexturesOrDefault(type);
        material.setTexture("DiffuseMap", textures.getDiffuseMap());
        material.setTexture("NormalMap", textures.getNormalMap().orElse(null));
        material.setTexture("ParallaxMap", textures.getParallaxMap().orElse(null));

        return material;
    }

    /**
     * Reload all the materials in the registry.
     */
    private void reload() {
        registry.keySet().forEach(type -> setTextures(type, get(type)));
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
            return of(assetManager.loadTexture(new TextureKey(texture)));
        } catch (AssetNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("Texture {} not found in theme {}", texture, theme);
            }
        }

        return empty();
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
    private static class TexturesWrapper {

        private final Texture diffuseMap;
        private final Optional<Texture> normalMap;
        private final Optional<Texture> parallaxMap;

    }

}
