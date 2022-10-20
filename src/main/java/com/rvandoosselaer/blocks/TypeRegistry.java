package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static final BlocksTheme FAITHFUL_THEME = new BlocksTheme("Faithful", "Blocks/Themes/faithful/");

    private enum TextureType {
        DIFFUSE, NORMAL, PARALLAX, OVERLAY;
    }

    private final ConcurrentMap<String, Type> registry = new ConcurrentHashMap<>();
    private final AssetManager assetManager;
    @Getter
    private BlocksTheme theme;
    @Getter
    private BlocksTheme defaultTheme = new BlocksTheme("Soartex Fanver", "Blocks/Themes/default/");

    /**
     * Will register default materials
     */
    public TypeRegistry(@NonNull AssetManager assetManager) {
        this(assetManager, null, true);
    }

    /**
     * Will register default materials
     */
    public TypeRegistry(@NonNull AssetManager assetManager, BlocksTheme theme) {
        this(assetManager, theme, true);
    }

    public TypeRegistry(@NonNull AssetManager assetManager, BlocksTheme theme, boolean registerDefaultMaterials) {
        this.assetManager = assetManager;
        this.theme = theme;

        if (registerDefaultMaterials) {
            registerDefaultMaterials();
        }
    }

    public Type register(@NonNull String name) {
        return register(name, getType(name));
    }

    public Type register(@NonNull String name, @NonNull Type type) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(String.format("Invalid type name %s specified.", name));
        }

        registry.put(name, type);
        if (log.isTraceEnabled()) {
            log.trace("Registered type {} -> {}", name, type);
        }
        return type;
    }

    public Type get(String name) {
        Type type = registry.get(name);
        if (type == null) {
            log.warn("No type found for name {}", name);
        }
        return type;
    }

    public boolean remove(@NonNull String name) {
        if (registry.containsKey(name)) {
            Type type = registry.remove(name);
            if (log.isTraceEnabled()) {
                log.trace("Removed type {} -> {}", name, type);
            }
            return true;
        }
        return false;
    }

    public boolean usingTheme() {
        return theme != null;
    }

    public void clear() {
        registry.clear();
    }

    public Collection<String> getAll() {
        return Collections.unmodifiableCollection(registry.keySet());
    }

    public void registerDefaultMaterials() {
        register(TypeIds.BIRCH_LOG);
        register(TypeIds.BIRCH_PLANKS);
        register(TypeIds.BRICKS);
        register(TypeIds.COBBLESTONE);
        register(TypeIds.MOSSY_COBBLESTONE);
        register(TypeIds.DIRT);
        register(TypeIds.GRAVEL);
        register(TypeIds.GRASS);
        register(TypeIds.GRASS_SNOW);
        register(TypeIds.PALM_TREE_LOG);
        register(TypeIds.PALM_TREE_PLANKS);
        register(TypeIds.ROCK);
        register(TypeIds.OAK_LOG);
        register(TypeIds.OAK_PLANKS);
        register(TypeIds.SAND);
        register(TypeIds.SNOW);
        register(TypeIds.SPRUCE_LOG);
        register(TypeIds.SPRUCE_PLANKS);
        register(TypeIds.STONE_BRICKS);
        register(TypeIds.MOSSY_STONE_BRICKS);
        register(TypeIds.WATER);
        register(TypeIds.WATER_STILL);
        register(TypeIds.BIRCH_LEAVES);
        register(TypeIds.PALM_TREE_LEAVES);
        register(TypeIds.OAK_LEAVES);
        register(TypeIds.SPRUCE_LEAVES);
        register(TypeIds.WINDOW);
    }

    public void setTheme(BlocksTheme theme) {
        if (log.isDebugEnabled()) {
            log.debug("Setting {}", theme);
        }
        this.theme = theme;
        reload();
    }

    public void setDefaultTheme(@NonNull BlocksTheme defaultTheme) {
        if (log.isDebugEnabled()) {
            log.debug("Setting {} as default theme", theme);
        }
        this.defaultTheme = defaultTheme;
        reload();
    }

    /**
     * Retrieves a material for the block type. When a material file isn't found, the default material will be used and
     * the textures found in the theme or default theme folder will be added to the material.
     *
     * @param name block type (grass, rock, ...)
     * @return the material of the block type
     */
    private Type getType(String name) {
        if (usingTheme()) {
            Optional<Material> optionalThemeMaterial = loadMaterial(name, theme);
            if (optionalThemeMaterial.isPresent()) {
                return new Type(name, optionalThemeMaterial.get());
            }

            Optional<TextureUtilWrapper> optionalThemeTextures = getTextures(name, theme);
            if (optionalThemeTextures.isPresent()) {
                Material material = setTexturesOnMaterial(optionalThemeTextures.get(), load(DEFAULT_BLOCK_MATERIAL));
                return new Type(name, material, optionalThemeTextures.get().diffuseMap.createTextureCoordinatesFunction());
            }
        }

        Optional<Material> optionalDefaultThemeMaterial = loadMaterial(name, defaultTheme);
        if (optionalDefaultThemeMaterial.isPresent()) {
            return new Type(name, optionalDefaultThemeMaterial.get());
        }

        Optional<TextureUtilWrapper> optionalDefaultThemeTextures = getTextures(name, defaultTheme);
        if (!optionalDefaultThemeTextures.isPresent()) {
            throw new AssetNotFoundException("Texture " + getTexturePath(name, TextureType.DIFFUSE, defaultTheme) + " not found!");
        }

        Material material = setTexturesOnMaterial(optionalDefaultThemeTextures.get(), load(DEFAULT_BLOCK_MATERIAL));
        return new Type(name, material, optionalDefaultThemeTextures.get().diffuseMap.createTextureCoordinatesFunction());
    }

    /**
     * Load the material in the given theme.
     *
     * @param name  block type
     * @param theme
     * @return an optional of the material
     */
    private Optional<Material> loadMaterial(String name, BlocksTheme theme) {
        String materialPath = getMaterialPath(name, theme);
        try {
            if (log.isTraceEnabled()) {
                log.trace("Loading material {}", materialPath);
            }
            return of(assetManager.loadMaterial(materialPath));
        } catch (AssetNotFoundException e) {
            if (log.isTraceEnabled()) {
                log.trace("Material {} not found in theme {}", materialPath, theme);
            }
        }

        return empty();
    }

    private Material setTexturesOnMaterial(TextureUtilWrapper textureUtilWrapper, Material material) {
        material.setTexture("DiffuseMap", textureUtilWrapper.getDiffuseMap().getTexture());
        material.setTexture("NormalMap", textureUtilWrapper.getNormalMap().map(TextureUtil::getTexture).orElse(null));
        material.setTexture("ParallaxMap", textureUtilWrapper.getParallaxMap().map(TextureUtil::getTexture).orElse(null));
        material.setTexture("OverlayMap", textureUtilWrapper.getOverlayMap().map(TextureUtil::getTexture).orElse(null));

        return material;
    }

    /**
     * Reload all the materials in the registry.
     */
    private void reload() {
        registry.keySet().forEach(this::register);
    }

    private Material load(String materialPath) {
        if (log.isTraceEnabled()) {
            log.trace("Loading material {}", materialPath);
        }
        return assetManager.loadMaterial(materialPath);
    }

    /**
     * Retrieves the textures for the block type in the given theme.
     *
     * @param type
     * @param theme
     * @return an optional of the textures
     */
    private Optional<TextureUtilWrapper> getTextures(String type, BlocksTheme theme) {
        Optional<TextureUtil> diffuseMap = getTexture(type, TextureType.DIFFUSE, theme);
        // map the value if present, or return an empty optional
        return diffuseMap.map(textureUtil -> new TextureUtilWrapper(textureUtil,
                getTexture(type, TextureType.NORMAL, theme),
                getTexture(type, TextureType.PARALLAX, theme),
                getTexture(type, TextureType.OVERLAY, theme)));
    }

    /**
     * Load the default texture like 'dirt.png'. If this texture isn't found, try to load the specific textures
     * (dirt_up.png, dirt_down.png, ...) and combine them into one.
     *
     * @param type        block type (grass, rock, ...)
     * @param textureType kind of texture (diffuse, normal, parallax)
     * @param theme
     * @return an optional of the texture
     */
    private Optional<TextureUtil> getTexture(String type, TextureType textureType, BlocksTheme theme) {
        Optional<Texture> defaultTexture = loadTexture(new TextureKey(getDefaultTexturePath(type, textureType, theme)));
        Optional<Texture> upTexture = loadTexture(new TextureKey(getUpTexturePath(type, textureType, theme)));
        Optional<Texture> downTexture = loadTexture(new TextureKey(getDownTexturePath(type, textureType, theme)));
        Optional<Texture> northTexture = loadTexture(new TextureKey(getNorthTexturePath(type, textureType, theme)));
        Optional<Texture> eastTexture = loadTexture(new TextureKey(getEastTexturePath(type, textureType, theme)));
        Optional<Texture> southTexture = loadTexture(new TextureKey(getSouthTexturePath(type, textureType, theme)));
        Optional<Texture> westTexture = loadTexture(new TextureKey(getWestTexturePath(type, textureType, theme)));

        // check sizes match
        List<Integer> textureWidthList = Stream.of(defaultTexture, upTexture, downTexture, northTexture, eastTexture, southTexture, westTexture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(texture -> texture.getImage().getWidth()).collect(Collectors.toList());
        boolean widthEqual = assertValuesAreEqual(textureWidthList);
        List<Integer> textureHeightList = Stream.of(defaultTexture, upTexture, downTexture, northTexture, eastTexture, southTexture, westTexture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(texture -> texture.getImage().getHeight()).collect(Collectors.toList());
        boolean heightEqual = assertValuesAreEqual(textureHeightList);

        if (!widthEqual || !heightEqual) {
            throw new IllegalArgumentException(String.format("Textures of type %s have different sizes! The widths and heights of the textures should be equal.", type));
        }

        // check color space matches
        List<ColorSpace> colorSpaceList = Stream.of(defaultTexture, upTexture, downTexture, northTexture, eastTexture, southTexture, westTexture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(texture -> texture.getImage().getColorSpace())
                .collect(Collectors.toList());
        boolean colorSpacesEqual = assertColorSpacesAreEqual(colorSpaceList);
        if (!colorSpacesEqual) {
            throw new IllegalArgumentException(String.format("Textures of type %s have different colorspaces! Colorspaces of the textures should be equal.", type));
        }

        boolean nonePresent = Stream.of(defaultTexture, upTexture, downTexture, northTexture, eastTexture, southTexture, westTexture)
                .noneMatch(Optional::isPresent);
        if (nonePresent) {
            return Optional.empty();
        }
        boolean missingDefault = Stream.of(upTexture, downTexture, northTexture, eastTexture, southTexture, westTexture)
                .filter(Optional::isPresent)
                .count() < 6 && !defaultTexture.isPresent();
        if (missingDefault) {
            log.warn("No default texture is set for type {} .", type);
        }

        TextureUtil textureUtil = defaultTexture.map(TextureUtil::new).orElse(new TextureUtil());
        upTexture.ifPresent(texture -> textureUtil.addTexture(Direction.UP, texture));
        downTexture.ifPresent(texture -> textureUtil.addTexture(Direction.DOWN, texture));
        northTexture.ifPresent(texture -> textureUtil.addTexture(Direction.NORTH, texture));
        eastTexture.ifPresent(texture -> textureUtil.addTexture(Direction.EAST, texture));
        southTexture.ifPresent(texture -> textureUtil.addTexture(Direction.SOUTH, texture));
        westTexture.ifPresent(texture -> textureUtil.addTexture(Direction.WEST, texture));

        return Optional.of(textureUtil);
    }

    private Optional<Texture> loadTexture(TextureKey textureKey) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Loading {}", textureKey);
            }
            return Optional.of(assetManager.loadTexture(textureKey));
        } catch (AssetNotFoundException e) {
            if (log.isTraceEnabled()) {
                log.trace("Texture {} not found", textureKey);
            }
        }

        return Optional.empty();
    }

    private static boolean assertValuesAreEqual(List<Integer> values) {
        if (values.isEmpty()) {
            return true;
        }

        int checkValue = values.get(0);
        for (int i : values) {
            if (i != checkValue) {
                return false;
            }
        }

        return true;
    }

    private static boolean assertColorSpacesAreEqual(List<ColorSpace> colorSpaces) {
        if (colorSpaces.isEmpty()) {
            return true;
        }

        ColorSpace colorSpace = colorSpaces.get(0);
        for (ColorSpace c : colorSpaces) {
            if (colorSpace != c) {
                return false;
            }
        }

        return true;
    }

    private String getDefaultTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type, textureType, theme);
    }

    private String getUpTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_up", textureType, theme);
    }

    private String getDownTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_down", textureType, theme);
    }

    private String getNorthTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_north", textureType, theme);
    }

    private String getEastTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_east", textureType, theme);
    }

    private String getSouthTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_south", textureType, theme);
    }

    private String getWestTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        return getTexturePath(type + "_west", textureType, theme);
    }

    /**
     * @param type        block type (grass, rock, ...)
     * @param textureType kind of texture (diffuse, normal, parallax)
     * @param theme       current theme
     * @return the full path to the texture, used by the assetmanager to load the texture
     */
    private static String getTexturePath(String type, TextureType textureType, BlocksTheme theme) {
        String path = Paths.get(theme.getPath(), getTextureFilename(type, textureType)).toString();
        // this should be interpreted as a java path. It will look for this texture in the classpath. When this code is
        // executed on windows, it will use backward slashes when constructing the path, and the texture will not be
        // properly resolved.
        path = path.replace('\\', '/');
        return path;
    }

    private static String getMaterialPath(String type, BlocksTheme theme) {
        String path = Paths.get(theme.getPath(), getMaterialFilename(type)).toString();
        // this should be interpreted as a java path. It will look for this texture in the classpath. When this code is
        // executed on windows, it will use backward slashes when constructing the path, and the texture will not be
        // properly resolved.
        path = path.replace('\\', '/');
        return path;
    }

    private static String getMaterialFilename(String type) {
        String fileExtension = ".j3m";
        return type + fileExtension;
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
            case OVERLAY:
                return type + "-overlay" + fileExtension;
            default:
                return type + fileExtension;
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class TextureUtilWrapper {

        private final TextureUtil diffuseMap;
        private final Optional<TextureUtil> normalMap;
        private final Optional<TextureUtil> parallaxMap;
        private final Optional<TextureUtil> overlayMap;

    }

}
