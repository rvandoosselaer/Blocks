package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.TypeRegistry;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultCheckboxModel;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.mathd.Vec3i;
import lombok.extern.slf4j.Slf4j;

/**
 * An application that renders a single block.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
@Slf4j
public class PBRBlock extends SimpleApplication {

    private int frame = 0;
    private Chunk chunk;
    private Material waterMaterial;
    private VersionedReference<Double> metallicValue;
    private Label metallicLabel;
    private VersionedReference<Double> roughnessValue;
    private Label roughnessLabel;
    private VersionedReference<Double> parallaxHeightValue;
    private Label parallaxHeightLabel;
    private VersionedReference<Boolean> steepParallaxValue;
    private Texture baseColorMapValue;
    private VersionedReference<Boolean> baseColorMapCheckBoxRef;
    private Texture normalMapValue;
    private VersionedReference<Boolean> normalMapCheckBoxRef;
    private Texture roughnessMapValue;
    private VersionedReference<Boolean> roughnessMapCheckBoxRef;
    private Texture parallaxMapValue;
    private VersionedReference<Boolean> parallaxMapCheckBoxRef;
    private Texture emissiveMapValue;
    private VersionedReference<Boolean> emissiveMapCheckBoxRef;

    public static void main(String[] args) {
        PBRBlock pbrBlock = new PBRBlock();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        pbrBlock.setSettings(settings);
        pbrBlock.start();
    }

    public PBRBlock() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState(),
                new EnvironmentCamera(256, new Vector3f(0, 3f, 0)));
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        renderManager.setPreferredLightMode(TechniqueDef.LightMode.SinglePassAndImageBased);
        renderManager.setSinglePassLightBatchSize(3);

        BlocksConfig.initialize(assetManager);

        TypeRegistry typeRegistry = BlocksConfig.getInstance().getTypeRegistry();
        typeRegistry.register(TypeIds.WATER, assetManager.loadMaterial("water-pbr/water.j3m"));
        waterMaterial = typeRegistry.get(TypeIds.WATER);

        chunk = createChunk();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Path.hdr", SkyFactory.EnvMapType.EquirectMap));

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);

        cam.setLocation(new Vector3f(36, 7, -5));
        cam.lookAt(new Vector3f(16, 1, 16), Vector3f.UNIT_Y);

        flyCam.setDragToRotate(true);

        Container materialDebug = createMaterialDebugContainer();
        materialDebug.setLocalTranslation(0, getContext().getSettings().getHeight(), 1);
        guiNode.attachChild(materialDebug);
    }

    @Override
    public void simpleUpdate(float tpf) {
        frame++;

        if (frame == 2) {
            LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode, new JobProgressAdapter<LightProbe>() {
                @Override
                public void done(LightProbe result) {
                    log.info("Environment map rendered");
                    enqueue(() -> {
                        result.getArea().setRadius(100);
                        rootNode.addLight(result);
                        rootNode.attachChild(chunk.getNode());

                        FilterPostProcessor fpp = getStateManager().getState(PostProcessingState.class).getFilterPostProcessor();
                        fpp.addFilter(new BloomFilter(BloomFilter.GlowMode.Scene));
                        fpp.addFilter(new ToneMapFilter(Vector3f.UNIT_XYZ.mult(4.0f)));
                    });
                }
            });
        }

        if (metallicValue.needsUpdate()) {
            float newMetallicValue = metallicValue.get().floatValue();
            metallicLabel.setText(String.format("%.2f", newMetallicValue));
            waterMaterial.setFloat("Metallic", newMetallicValue);
        }
        if (roughnessValue.needsUpdate()) {
            float newRoughnessValue = roughnessValue.get().floatValue();
            roughnessLabel.setText(String.format("%.2f", newRoughnessValue));
            waterMaterial.setFloat("Roughness", newRoughnessValue);
        }
        if (parallaxHeightValue.needsUpdate()) {
            float newParallaxHeightValue = parallaxHeightValue.get().floatValue();
            parallaxHeightLabel.setText(String.format("%.3f", newParallaxHeightValue));
            waterMaterial.setFloat("ParallaxHeight", newParallaxHeightValue);
        }
        if (steepParallaxValue.needsUpdate()) {
            waterMaterial.setBoolean("SteepParallax", steepParallaxValue.get());
        }
        if (baseColorMapCheckBoxRef.needsUpdate()) {
            waterMaterial.setTexture("BaseColorMap", baseColorMapCheckBoxRef.get() ? baseColorMapValue : null);
        }
        if (normalMapCheckBoxRef.needsUpdate()) {
            waterMaterial.setTexture("NormalMap", normalMapCheckBoxRef.get() ? normalMapValue : null);
        }
        if (roughnessMapCheckBoxRef.needsUpdate()) {
            waterMaterial.setTexture("RoughnessMap", roughnessMapCheckBoxRef.get() ? roughnessMapValue : null);
        }
        if (parallaxMapCheckBoxRef.needsUpdate()) {
            waterMaterial.setTexture("ParallaxMap", parallaxMapCheckBoxRef.get() ? parallaxMapValue : null);
        }
        if (emissiveMapCheckBoxRef.needsUpdate()) {
            waterMaterial.setTexture("EmissiveMap", emissiveMapCheckBoxRef.get() ? emissiveMapValue : null);
        }
    }

    private Chunk createChunk() {
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        BlocksConfig.getInstance().setChunkSize(new Vec3i(32, 7, 32));

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 32; z++) {
                    if (y == 0) {
                        chunk.addBlock(x, y, z, blockRegistry.get(TypeIds.DIRT));
                    } else {
                        Block block = x > 3 && x < 28 && z > 3 && z < 28 ? blockRegistry.get(TypeIds.WATER) : blockRegistry.get(TypeIds.GRASS);
                        chunk.addBlock(x, y, z, block);
                    }
                }
            }
        }

        chunk.addBlock(16, 6, 16, blockRegistry.get(TypeIds.WATER));

        chunk.update();
        return chunk;
    }

    private Container createMaterialDebugContainer() {
        Container container = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Even));
        container.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 0.9f)));
        container.addChild(new Label("PBR Material debug", new ElementId(Container.ELEMENT_ID).child("title")));

        // metallic
        float currentMetallicValue = waterMaterial.getParamValue("Metallic");
        Container metallic = container.addChild(createRow());
        metallic.addChild(new Label("Metallic"));
        Slider metallicSlider = metallic.addChild(createSlider(currentMetallicValue, 0.05));
        metallicValue = metallicSlider.getModel().createReference();
        metallicLabel = metallic.addChild(new Label(String.format("%.2f", currentMetallicValue)));

        // roughness
        float currentRoughnessValue = waterMaterial.getParamValue("Roughness");
        Container roughness = container.addChild(createRow());
        roughness.addChild(new Label("Roughness"));
        Slider roughnessSlider = roughness.addChild(createSlider(currentRoughnessValue, 0.05));
        roughnessValue = roughnessSlider.getModel().createReference();
        roughnessLabel = roughness.addChild(new Label(String.format("%.2f", currentRoughnessValue)));

        // parallax height
        float currentParallaxHeightValue = waterMaterial.getParamValue("ParallaxHeight");
        Container parallaxHeight = container.addChild(createRow());
        parallaxHeight.addChild(new Label("ParallaxHeight"));
        Slider parallaxHeightSlider = parallaxHeight.addChild(createSlider(currentParallaxHeightValue, 0.005));
        parallaxHeightValue = parallaxHeightSlider.getModel().createReference();
        parallaxHeightLabel = parallaxHeight.addChild(new Label(String.format("%.3f", currentParallaxHeightValue)));

        // steep parallax
        boolean currentSteepParallaxValue = waterMaterial.getParamValue("SteepParallax");
        Container steepParallax = container.addChild(createRow());
        Checkbox steepParallaxCheckBox = steepParallax.addChild(new Checkbox("Steep Parallax", new DefaultCheckboxModel(currentSteepParallaxValue)));
        steepParallaxValue = steepParallaxCheckBox.getModel().createReference();

        // basecolor map
        baseColorMapValue = waterMaterial.getParamValue("BaseColorMap");
        Container baseColorMap = container.addChild(createRow());
        Checkbox baseColorMapCheckBox = baseColorMap.addChild(new Checkbox("BaseColorMap", new DefaultCheckboxModel(baseColorMapValue != null)));
        baseColorMapCheckBoxRef = baseColorMapCheckBox.getModel().createReference();

        // normal map
        normalMapValue = waterMaterial.getParamValue("NormalMap");
        Container normalMap = container.addChild(createRow());
        Checkbox normalMapCheckBox = normalMap.addChild(new Checkbox("NormalMap", new DefaultCheckboxModel(normalMapValue != null)));
        normalMapCheckBoxRef = normalMapCheckBox.getModel().createReference();

        // roughness map
        roughnessMapValue = waterMaterial.getParamValue("RoughnessMap");
        Container roughnessMap = container.addChild(createRow());
        Checkbox roughnessMapCheckBox = roughnessMap.addChild(new Checkbox("RoughnessMap", new DefaultCheckboxModel(roughnessMapValue != null)));
        roughnessMapCheckBoxRef = roughnessMapCheckBox.getModel().createReference();

        // parallax map
        parallaxMapValue = waterMaterial.getParamValue("ParallaxMap");
        Container parallaxMap = container.addChild(createRow());
        Checkbox parallaxMapCheckBox = parallaxMap.addChild(new Checkbox("ParallaxMap", new DefaultCheckboxModel(parallaxMapValue != null)));
        parallaxMapCheckBoxRef = parallaxMapCheckBox.getModel().createReference();

        // emissive map
        emissiveMapValue = waterMaterial.getParamValue("EmissiveMap");
        Container emissiveMap = container.addChild(createRow());
        Checkbox emissiveMapCheckBox = emissiveMap.addChild(new Checkbox("EmissiveMap", new DefaultCheckboxModel(emissiveMapValue != null)));
        emissiveMapCheckBoxRef = emissiveMapCheckBox.getModel().createReference();

        return container;
    }

    private Container createRow() {
        return new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Even, FillMode.Even));
    }

    private Slider createSlider(float value, double delta) {
        Slider slider = new Slider(new DefaultRangedValueModel(0, 1, value), Axis.X);
        slider.setDelta(delta);
        return slider;
    }


}
