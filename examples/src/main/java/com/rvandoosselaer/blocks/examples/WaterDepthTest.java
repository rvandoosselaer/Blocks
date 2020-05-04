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
import com.jme3.system.AppSettings;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.filters.FluidFilter;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
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
public class WaterDepthTest extends SimpleApplication {

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

    public static void main(String[] args) {
        LogAdapter.initialize();

        WaterDepthTest waterDepthTest = new WaterDepthTest();
        AppSettings settings = new AppSettings(false);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        waterDepthTest.setSettings(settings);
        waterDepthTest.start();
    }

    public WaterDepthTest() {
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

        flyCam.setDragToRotate(true);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(-1.3989371f, 13.492569f, 30.279642f));
        cam.setRotation(new Quaternion(0.08451398f, 0.8909429f, -0.18557356f, 0.40575933f));
    }

    @Override
    public void simpleUpdate(float tpf) {
        PostProcessingState postProcessingState = getStateManager().getState(PostProcessingState.class);
        if (postProcessingState.getFilterPostProcessor() != null && postProcessingState.getFilterPostProcessor().getFilter(FluidFilter.class) == null) {
            fluidFilter = new FluidFilter();
            fluidFilter.setFadeDepth(8);
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
    }

    private Chunk createChunk() {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        Block sand = blockRegistry.get(BlockIds.SAND);
        Block water = blockRegistry.get(BlockIds.WATER);
        Block grass = blockRegistry.get(BlockIds.GRASS);

        Vector3f center = new Vector3f(15, 1, 15);
        float radius = 9;
        Chunk chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < chunkSize.y; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y == 0) {
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
        return chunk;
    }

    private Container createFilterPanel() {
        Container container = new Container(new SpringGridLayout(Axis.Y, Axis.X));
        container.addChild(new Label("FluidDepthFilter", new ElementId("title")));

        Container depthRow = container.addChild(createRow());
        depthRow.addChild(new Label("Fade depth: "));
        depthValue = depthRow.addChild(new Label(Float.toString(fluidFilter.getFadeDepth())));
        Slider depth = depthRow.addChild(createSlider(0.1f, 0, 20, fluidFilter.getFadeDepth()));
        depthReference = depth.getModel().createReference();

        Container shorelineRow = container.addChild(createRow());
        shorelineRow.addChild(new Label("Shoreline size: "));
        shorelineValue = shorelineRow.addChild(new Label(Float.toString(fluidFilter.getShorelineSize())));
        Slider shoreline = shorelineRow.addChild(createSlider(0.1f, 0, 20, fluidFilter.getShorelineSize()));
        shorelineReference = shoreline.getModel().createReference();

        Checkbox distortion = container.addChild(new Checkbox("Distortion"));
        distortion.setChecked(fluidFilter.isDistortion());
        distortion.addClickCommands(button -> fluidFilter.setDistortion(distortion.isChecked()));

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

        container.setLocalTranslation(0, cam.getHeight(), 99);

        return container;
    }

    private Container createRow() {
        return new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.Last, FillMode.Even));
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
