package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.filters.FluidFilter;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application that renders a coastline scene to test the FluidDepthFilter.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author: rvandoosselaer
 */
public class FluidFilterTest extends SimpleApplication {

    private Chunk chunk;
    private FluidFilter fluidFilter;
    private VersionedReference<Double> depthReference;
    private Label depthValue;
    private VersionedReference<Double> shorelineReference;
    private Label shorelineValue;
    private VersionedReference<Double> distStrengthXReference;
    private Label distStrengthXValue;
    private VersionedReference<Double> distStrengthYReference;
    private Label distStrengthYValue;
    private Label distAmplitudeXValue;
    private VersionedReference<Double> distAmplitudeXReference;
    private Label distAmplitudeYValue;
    private VersionedReference<Double> distAmplitudeYReference;
    private Label distortionSpeedValue;
    private VersionedReference<Double> distSpeedReference;
    private Label reflectionValue;
    private VersionedReference<Double> reflectionReference;
    private Label waterHeightValue;
    private VersionedReference<Double> waterHeightReference;

    public static void main(String[] args) {
        LogAdapter.initialize();

        FluidFilterTest fluidFilterTest = new FluidFilterTest();
        AppSettings settings = new AppSettings(false);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        fluidFilterTest.setSettings(settings);
        fluidFilterTest.start();
    }

    public FluidFilterTest() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new WireframeState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState());
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        chunk = createChunk();
        chunk.update();

        ChunkMeshGenerator chunkMeshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(chunkMeshGenerator);

        rootNode.attachChild(chunk.getNode());

        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        rootNode.attachChild(sky);

        flyCam.setDragToRotate(true);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(-4.6468897f, 13.349939f, 33.132538f));
        cam.setRotation(new Quaternion(0.042314883f, 0.9045431f, -0.092438444f, 0.41408485f));
    }

    @Override
    public void simpleUpdate(float tpf) {
        PostProcessingState postProcessingState = getStateManager().getState(PostProcessingState.class);
        if (postProcessingState.getFilterPostProcessor() != null && postProcessingState.getFilterPostProcessor().getFilter(FluidFilter.class) == null) {
            fluidFilter = new FluidFilter();
            fluidFilter.setFadeDepth(8);
            fluidFilter.setWaterHeight(7);
            fluidFilter.setReflectionMapSize(1024);
            fluidFilter.addFluidGeometry((Geometry) chunk.getNode().getChild(TypeIds.WATER));
            postProcessingState.getFilterPostProcessor().addFilter(fluidFilter);

            guiNode.attachChild(createFilterPanel());
        }

        if (depthReference.update()) {
            fluidFilter.setFadeDepth(depthReference.get().floatValue());
            depthValue.setText(String.format("%.1f", depthReference.get()));
        }

        if (shorelineReference.update()) {
            fluidFilter.setShorelineSize(shorelineReference.get().floatValue());
            shorelineValue.setText(String.format("%.1f", shorelineReference.get()));
        }

        if (distStrengthXReference.update()) {
            fluidFilter.setDistortionStrengthX(distStrengthXReference.get().floatValue());
            distStrengthXValue.setText(String.format("%.4f", distStrengthXReference.get()));
        }

        if (distStrengthYReference.update()) {
            fluidFilter.setDistortionStrengthY(distStrengthYReference.get().floatValue());
            distStrengthYValue.setText(String.format("%.4f", distStrengthYReference.get()));
        }

        if (distAmplitudeXReference.update()) {
            fluidFilter.setDistortionAmplitudeX(distAmplitudeXReference.get().floatValue());
            distAmplitudeXValue.setText(String.format("%.0f", distAmplitudeXReference.get()));
        }

        if (distAmplitudeYReference.update()) {
            fluidFilter.setDistortionAmplitudeY(distAmplitudeYReference.get().floatValue());
            distAmplitudeYValue.setText(String.format("%.0f", distAmplitudeYReference.get()));
        }

        if (distSpeedReference.update()) {
            fluidFilter.setDistortionSpeed(distSpeedReference.get().floatValue());
            distortionSpeedValue.setText(String.format("%.1f", distSpeedReference.get()));
        }

        if (reflectionReference.update()) {
            fluidFilter.setReflectionStrength(reflectionReference.get().floatValue());
            reflectionValue.setText(String.format("%.2f", reflectionReference.get()));
        }

        if (waterHeightReference.update()) {
            fluidFilter.setWaterHeight(waterHeightReference.get().floatValue());
            waterHeightValue.setText(String.format("%.1f", waterHeightReference.get()));
        }

    }

    private Chunk createChunk() {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        Block sand = blockRegistry.get(BlockIds.SAND);
        Block water = blockRegistry.get(BlockIds.WATER);
        Block grass = blockRegistry.get(BlockIds.GRASS);
        Block trunk = blockRegistry.get(BlockIds.OAK_LOG);
        Block leaves = blockRegistry.get(BlockIds.OAK_LEAVES);

        Vector3f center = new Vector3f(15, 1, 15);
        float radius = 9;
        Chunk chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < chunkSize.y; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if ((x == 0 || z == 0 || x == 31 || z == 31) && y < 7) {
                        chunk.addBlock(x, y, z, sand);
                    } else if (y == 0) {
                        chunk.addBlock(x, y, z, sand);
                    } else {
                        Vector3f location = new Vector3f(x, y, z);
                        if (location.distance(center) <= radius) {
                            if (y < 8) {
                                chunk.addBlock(x, y, z, sand);
                            } else if (y == 8) {
                                chunk.addBlock(x, y, z, grass);
                            }
                        } else if (y < 7) {
                            chunk.addBlock(x, y, z, water);
                        }
                    }
                }
            }
        }

        // create a tree
        Vec3i treeLocation = new Vec3i(15, 9, 15);
        chunk.addBlock(treeLocation, trunk);
        chunk.addBlock(treeLocation.addLocal(0, 1, 0), trunk);
        chunk.addBlock(treeLocation.addLocal(0, 1, 0), trunk);

        int canopyRadius = 3;
        Vec3i canopyCenter = treeLocation.addLocal(0, canopyRadius, 0);
        for (int x = canopyCenter.x - canopyRadius; x <= canopyCenter.x + canopyRadius; x++) {
            for (int y = canopyCenter.y - canopyRadius; y <= canopyCenter.y + canopyRadius; y++) {
                for (int z = canopyCenter.z - canopyRadius; z <= canopyCenter.z + canopyRadius; z++) {
                    Vector3f location = new Vector3f(x, y, z);
                    float distance = location.distance(canopyCenter.toVector3f());
                    if (distance <= canopyRadius && y > canopyCenter.y - canopyRadius) {
                        chunk.addBlock(x, y, z, leaves);
                    }
                }
            }
        }

        return chunk;
    }

    private Container createFilterPanel() {
        Container container = new Container(new SpringGridLayout(Axis.Y, Axis.X));
        Label title = container.addChild(new Label("FluidFilter", new ElementId("title")));
        DragHandler dragHandler = new DragHandler(input -> container);
        CursorEventControl.addListenersToSpatial(title, dragHandler);

        Container fade = container.addChild(createRow());
        Checkbox fadeCheckBox = fade.addChild(new Checkbox("Enable depth fading"));
        fadeCheckBox.setChecked(fluidFilter.isEnableFading());
        fadeCheckBox.addClickCommands(button -> fluidFilter.setEnableFading(fadeCheckBox.isChecked()));

        Container depthRow = container.addChild(createRow());
        depthRow.addChild(new Label("Fade depth: "));
        depthValue = depthRow.addChild(new Label(Float.toString(fluidFilter.getFadeDepth())));
        Slider depth = depthRow.addChild(createSlider(0.1f, 0, 100, fluidFilter.getFadeDepth()));
        depthReference = depth.getModel().createReference();

        Container shorelineRow = container.addChild(createRow());
        shorelineRow.addChild(new Label("Shoreline size: "));
        shorelineValue = shorelineRow.addChild(new Label(Float.toString(fluidFilter.getShorelineSize())));
        Slider shoreline = shorelineRow.addChild(createSlider(0.1f, 0, 20, fluidFilter.getShorelineSize()));
        shorelineReference = shoreline.getModel().createReference();

        Container distortion = container.addChild(createRow());
        Checkbox distortionCheckBox = distortion.addChild(new Checkbox("Enable distortion"));
        distortionCheckBox.setChecked(fluidFilter.isDistortion());
        distortionCheckBox.addClickCommands(button -> fluidFilter.setDistortion(distortionCheckBox.isChecked()));

        Container distortionStrengthX = container.addChild(createRow());
        distortionStrengthX.addChild(new Label("Strength x: "));
        distStrengthXValue = distortionStrengthX.addChild(new Label(Float.toString(fluidFilter.getDistortionStrengthX())));
        Slider distStrengthX = distortionStrengthX.addChild(createSlider(0.0001f, 0, 0.01f, fluidFilter.getDistortionStrengthX()));
        distStrengthXReference = distStrengthX.getModel().createReference();

        Container distortionStrengthY = container.addChild(createRow());
        distortionStrengthY.addChild(new Label("Strength y: "));
        distStrengthYValue = distortionStrengthY.addChild(new Label(Float.toString(fluidFilter.getDistortionStrengthY())));
        Slider distStrengthY = distortionStrengthY.addChild(createSlider(0.0001f, 0, 0.01f, fluidFilter.getDistortionStrengthY()));
        distStrengthYReference = distStrengthY.getModel().createReference();

        Container distortionOffsetX = container.addChild(createRow());
        distortionOffsetX.addChild(new Label("Amplitude x: "));
        distAmplitudeXValue = distortionOffsetX.addChild(new Label(Float.toString(fluidFilter.getDistortionAmplitudeX())));
        Slider distOffsetX = distortionOffsetX.addChild(createSlider(1f, 0, 50f, fluidFilter.getDistortionAmplitudeX()));
        distAmplitudeXReference = distOffsetX.getModel().createReference();

        Container distortionAmplitudeY = container.addChild(createRow());
        distortionAmplitudeY.addChild(new Label("Amplitude y: "));
        distAmplitudeYValue = distortionAmplitudeY.addChild(new Label(Float.toString(fluidFilter.getDistortionAmplitudeY())));
        Slider distOffsetY = distortionAmplitudeY.addChild(createSlider(1f, 0, 50f, fluidFilter.getDistortionAmplitudeY()));
        distAmplitudeYReference = distOffsetY.getModel().createReference();

        Container distortionSpeed = container.addChild(createRow());
        distortionSpeed.addChild(new Label("Speed: "));
        distortionSpeedValue = distortionSpeed.addChild(new Label(Float.toString(fluidFilter.getDistortionSpeed())));
        Slider distSpeed = distortionSpeed.addChild(createSlider(0.1f, 0f, 10f, fluidFilter.getDistortionSpeed()));
        distSpeedReference = distSpeed.getModel().createReference();

        Container reflectionStrength = container.addChild(createRow());
        reflectionStrength.addChild(new Label("Reflection: "));
        reflectionValue = reflectionStrength.addChild(new Label(Float.toString(fluidFilter.getReflectionStrength())));
        Slider reflection = reflectionStrength.addChild(createSlider(0.01f, 0, 1, fluidFilter.getReflectionStrength()));
        reflectionReference = reflection.getModel().createReference();

        Container waterHeight = container.addChild(createRow());
        waterHeight.addChild(new Label("Fluid height: "));
        waterHeightValue = waterHeight.addChild(new Label(Float.toString(fluidFilter.getWaterHeight())));
        Slider waterHeightSlider = waterHeight.addChild(createSlider(0.1f, 0, 10, fluidFilter.getWaterHeight()));
        waterHeightReference = waterHeightSlider.getModel().createReference();

        Container reflectionTextureSize = container.addChild(createRow());
        reflectionTextureSize.addChild(new Label("Reflection texture size: "));
        Label reflectionTextureSizeValue = reflectionTextureSize.addChild(new Label(Float.toString(fluidFilter.getReflectionMapSize())));
        Button minus = reflectionTextureSize.addChild(new Button(" - "));
        minus.addClickCommands(source -> {
            fluidFilter.setReflectionMapSize((int) (fluidFilter.getReflectionMapSize() * 0.5));
            reflectionTextureSizeValue.setText(Float.toString(fluidFilter.getReflectionMapSize()));
        });
        Button add = reflectionTextureSize.addChild(new Button(" + "));
        add.addClickCommands(source -> {
            fluidFilter.setReflectionMapSize((int) (fluidFilter.getReflectionMapSize() * 2));
            reflectionTextureSizeValue.setText(Float.toString(fluidFilter.getReflectionMapSize()));
        });

        container.setLocalTranslation(0, cam.getHeight(), 99);

        return container;
    }

    private Container createRow() {
        return new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Even, FillMode.Even));
    }

    private Slider createSlider(float delta, float min, float max, float value) {
        Slider slider = new Slider(Axis.X);
        slider.setDelta(delta);
        slider.getModel().setMinimum(min);
        slider.getModel().setMaximum(max);
        slider.getModel().setValue(value);

        return slider;
    }

}
