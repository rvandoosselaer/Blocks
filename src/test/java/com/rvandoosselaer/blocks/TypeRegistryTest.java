package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: rvandoosselaer
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TypeRegistryTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @AfterEach
    public void reset() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        typeRegistry.clear();
        typeRegistry.setTheme(null);
        typeRegistry.registerDefaultMaterials();
    }

    @Test
    @Order(1)
    public void testTheme() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        assertFalse(typeRegistry.usingTheme());
        BlocksTheme blocksTheme = new BlocksTheme("my-new-theme", "/my-new-theme");
        typeRegistry.setTheme(blocksTheme);
        assertTrue(typeRegistry.usingTheme());
    }

    @Test
    @Order(2)
    public void testRegisterType() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        Material newMaterial = new Material(BlocksConfig.getInstance().getAssetManager(), "/Common/MatDefs/Misc/Unshaded.j3md");
        typeRegistry.register(TypeIds.GRASS, new Type(TypeIds.GRASS, newMaterial));

        assertEquals(typeRegistry.get(TypeIds.GRASS).getMaterial(), newMaterial);
    }

    @Test
    @Order(3)
    public void testRegisterTypeUsingTheme() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        Material defaultMaterial = typeRegistry.get(TypeIds.GRASS).getMaterial();

        BlocksTheme blocksTheme = new BlocksTheme("my-new-theme", "/my-new-theme");
        typeRegistry.setTheme(blocksTheme);

        Material newMaterial = typeRegistry.get(TypeIds.GRASS).getMaterial();
        assertNotEquals(defaultMaterial.getName(), newMaterial.getName());
    }

    @Test
    @Order(4)
    public void testFallbackTypeInDefaultTheme() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        Material defaultMaterial = typeRegistry.get(TypeIds.DIRT).getMaterial();

        BlocksTheme blocksTheme = new BlocksTheme("my-new-theme", "/my-new-theme");
        typeRegistry.setTheme(blocksTheme);

        assertEquals(defaultMaterial.getName(), typeRegistry.get(TypeIds.DIRT).getMaterial().getName());
    }

    @Test
    @Order(5)
    public void testRegisterTypeWithImage() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        Material defaultSandMaterial = typeRegistry.get(TypeIds.SAND).getMaterial();
        Material defaultRockMaterial = typeRegistry.get(TypeIds.ROCK).getMaterial();
        Texture sandDiffuseTexture = defaultSandMaterial.getTextureParam("DiffuseMap").getTextureValue();
        Texture rockDiffuseTexture = defaultRockMaterial.getTextureParam("DiffuseMap").getTextureValue();

        BlocksTheme blocksTheme = new BlocksTheme("my-new-theme", "/my-new-theme");
        typeRegistry.setTheme(blocksTheme);

        Material newSandMaterial = typeRegistry.get(TypeIds.SAND).getMaterial();
        Material newRockMaterial = typeRegistry.get(TypeIds.ROCK).getMaterial();
        Texture newSandDiffuseTexture = newSandMaterial.getTextureParam("DiffuseMap").getTextureValue();
        Texture newRockDiffuseTexture = newRockMaterial.getTextureParam("DiffuseMap").getTextureValue();

        assertNotEquals(sandDiffuseTexture.getName(), newSandDiffuseTexture.getName());
        assertEquals(rockDiffuseTexture.getName(), newRockDiffuseTexture.getName());
    }

    @Test
    @Order(6)
    public void testRegisterTypeWithMultipleImages() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();

        BlocksTheme blocksTheme = new BlocksTheme("my-new-theme", "/my-new-theme");
        typeRegistry.setTheme(blocksTheme);

        typeRegistry.register("custom");

        Texture up = BlocksConfig.getInstance().getAssetManager().loadTexture("/my-new-theme/custom_up.png");
        Texture side = BlocksConfig.getInstance().getAssetManager().loadTexture("/my-new-theme/custom.png");
        Texture down = BlocksConfig.getInstance().getAssetManager().loadTexture("/my-new-theme/custom_down.png");

        int w = up.getImage().getWidth();
        int h = up.getImage().getHeight() + side.getImage().getHeight() + down.getImage().getHeight();

        Material material = typeRegistry.get("custom").getMaterial();
        Texture diffuseMap = material.getTextureParam("DiffuseMap").getTextureValue();

        assertEquals(w, diffuseMap.getImage().getWidth());
        assertEquals(h, diffuseMap.getImage().getHeight());

        Texture overlayMap = material.getTextureParam("OverlayMap").getTextureValue();
        assertNotNull(overlayMap);
    }

    @Test
    @Order(7)
    public void testClear() {
        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        int size = typeRegistry.getAll().size();
        assertNotEquals(0, size);

        typeRegistry.clear();
        size = typeRegistry.getAll().size();
        assertEquals(0, size);
    }

}
