package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * A registry for storing and retrieving materials based on the block type.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class MaterialRegistry {

    private static final String THEME_BLOCKS_TEXTURE_FOLDER = "blocks/";
    private static final String TEXTURE_EXTENSION = ".png";

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
    private final BlocksTheme theme;
    private final BlocksTheme defaultTheme = BlocksTheme.builder()
            .name("Default")
            .description("The default Blocks theme")
            .author("Remy Van Doosselaer")
            .version("0.1")
            .path("Blocks/Themes/default-theme/")
            .build();

    public MaterialRegistry(AssetManager assetManager) {
        this(assetManager, null);
    }

    public MaterialRegistry(AssetManager assetManager, BlocksTheme theme) {
        this.assetManager = assetManager;
        this.theme = theme;

        registerDefaultMaterials();

        if (theme == null) {
            // we are not using a theme, skip the themes folder locator setup
            return;
        }

        String themesFolder = BlocksConfig.getInstance().getThemesFolder();
        if (themesFolder == null) {
            throw new IllegalArgumentException("Themes folder not specified in BlocksConfig! Unable to load theme: " + theme);
        } else {
            log.debug("Registering themes folder {} with the asset manager.", themesFolder);
            this.assetManager.registerLocator(Paths.get(themesFolder).toAbsolutePath().toString(), FileLocator.class);
        }
    }

    public Material register(String type, String materialPath) {
        return register(type, load(materialPath));
    }

    public Material register(String type, Material material) {
        Material m = material.clone();
        m.setTexture("DiffuseMap", getDiffuseMapTexture(type));
        Texture normalMapTexture = getNormalMapTexture(type);
        if (normalMapTexture != null) {
            m.setTexture("NormalMap", normalMapTexture);
        }
        Texture parallaxMapTexture = getParallaxMapTexture(type);
        if (parallaxMapTexture != null) {
            m.setTexture("ParallaxMap", parallaxMapTexture);
        }
        registry.put(type, m);
        if (log.isTraceEnabled()) {
            log.trace("Registered {} -> {}", type, material);
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

    public Material load(String materialPath) {
        return assetManager.loadMaterial(materialPath);
    }

    public boolean hasTheme() {
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

    private Texture getDiffuseMapTexture(String type) {
        if (hasTheme()) {
            String texture = getDiffuseMapTexturePath(theme, type);
            try {
                return assetManager.loadTexture(new TextureKey(texture));
            } catch (AssetNotFoundException e) {
                log.warn("Texture {} not found in theme {}", texture, theme);
            }
        }

        return assetManager.loadTexture(new TextureKey(getDiffuseMapTexturePath(defaultTheme, type)));
    }

    private Texture getNormalMapTexture(String type) {
        if (hasTheme()) {
            String texture = getNormalMapTexturePath(theme, type);
            try {
                return assetManager.loadTexture(new TextureKey(texture));
            } catch (AssetNotFoundException e) {
                log.warn("Texture {} not found in theme {}", texture, theme);
            }
        }

        String texture = getNormalMapTexturePath(defaultTheme, type);
        try {
            return assetManager.loadTexture(new TextureKey(texture));
        } catch (AssetNotFoundException e) {
            log.warn("Texture {} not found in default theme.", texture);
            return null;
        }
    }

    private Texture getParallaxMapTexture(String type) {
        if (hasTheme()) {
            String texture = getParallaxMapTexturePath(theme, type);
            try {
                return assetManager.loadTexture(new TextureKey(texture));
            } catch (AssetNotFoundException e) {
                log.warn("Texture {} not found in theme {}", texture, theme);
            }
        }

        String texture = getParallaxMapTexturePath(defaultTheme, type);
        try {
            return assetManager.loadTexture(new TextureKey(texture));
        } catch (AssetNotFoundException e) {
            log.warn("Texture {} not found in default theme.", texture);
            return null;
        }
    }

    private String getDiffuseMapTexturePath(BlocksTheme theme, String type) {
        return Paths.get(theme.getPath(), THEME_BLOCKS_TEXTURE_FOLDER, type + TEXTURE_EXTENSION).toString();
    }

    private String getNormalMapTexturePath(BlocksTheme theme, String type) {
        return Paths.get(theme.getPath(), THEME_BLOCKS_TEXTURE_FOLDER, type + "-normal" + TEXTURE_EXTENSION).toString();
    }

    private String getParallaxMapTexturePath(BlocksTheme theme, String type) {
        return Paths.get(theme.getPath(), THEME_BLOCKS_TEXTURE_FOLDER, type + "-parallax" + TEXTURE_EXTENSION).toString();
    }

}
