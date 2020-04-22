package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.filters.FluidDepthFilter;
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
    private FluidDepthFilter fluidDepthFilter;
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

        BlocksConfig.getInstance().setChunkSize(new Vec3i(15, 7, 15));

        chunk = createChunk(BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.WATER));
        chunk.update();

        ChunkMeshGenerator chunkMeshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(chunkMeshGenerator);

        rootNode.attachChild(chunk.getNode());

        flyCam.setDragToRotate(true);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        cam.setLocation(new Vector3f(-15.381589f, 24.353018f, 30.988268f));
        cam.lookAt(new Vector3f(chunkSize.x * 0.5f, 0, chunkSize.z * 0.5f), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        PostProcessingState postProcessingState = getStateManager().getState(PostProcessingState.class);
        if (postProcessingState.getFilterPostProcessor() != null && postProcessingState.getFilterPostProcessor().getFilter(FluidDepthFilter.class) == null) {
            fluidDepthFilter = new FluidDepthFilter();
            fluidDepthFilter.addFluidGeometry((Geometry) chunk.getNode().getChild(TypeIds.WATER));
            postProcessingState.getFilterPostProcessor().addFilter(fluidDepthFilter);

            guiNode.attachChild(createFilterPanel());
        }

        if (depthReference.update()) {
            fluidDepthFilter.setFadeDepth(depthReference.get().floatValue());
            depthValue.setText(String.format("%.1f", depthReference.get()));
        }

        if (shorelineReference.update()) {
            fluidDepthFilter.setShorelineSize(shorelineReference.get().floatValue());
            shorelineValue.setText(String.format("%.1f", shorelineReference.get()));
        }

        if (distStrengthXReference.update()) {
            fluidDepthFilter.setDistortionStrengthX(distStrengthXReference.get().floatValue());
            distStrengthXValue.setText(String.format("%.3f", distStrengthXReference.get()));
        }

        if (distStrengthYReference.update()) {
            fluidDepthFilter.setDistortionStrengthY(distStrengthYReference.get().floatValue());
            distStrengthYValue.setText(String.format("%.3f", distStrengthYReference.get()));
        }

        if (distAmplitudeXReference.update()) {
            fluidDepthFilter.setDistortionAmplitudeX(distAmplitudeXReference.get().floatValue());
            distAmplitudeXValue.setText(String.format("%.0f", distAmplitudeXReference.get()));
        }

        if (distAmplitudeYReference.update()) {
            fluidDepthFilter.setDistortionAmplitudeY(distAmplitudeYReference.get().floatValue());
            distAmplitudeYValue.setText(String.format("%.0f", distAmplitudeYReference.get()));
        }

        if (distSpeedReference.update()) {
            fluidDepthFilter.setDistortionSpeed(distSpeedReference.get().floatValue());
            distortionSpeedValue.setText(String.format("%.1f", distSpeedReference.get()));
        }
    }

    private Chunk createChunk(Block waterDepthBlock) {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < chunkSize.y; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y > 0) {
                        if (x == 0 || x == 1 || x == chunkSize.x - 2 || x == chunkSize.x - 1 ||
                                z == 0 || z == 1 || z == chunkSize.z - 2 || z == chunkSize.z - 1) {
                            chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
                        } else {
                            if (x == 7 && z == 7) {
                                chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
                            } else {
                                chunk.addBlock(x, y, z, waterDepthBlock);
                            }
                        }
                    } else {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.SAND));
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
        depthValue = depthRow.addChild(new Label(Float.toString(fluidDepthFilter.getFadeDepth())));
        Slider depth = depthRow.addChild(createSlider(0.1f, 0, 20, fluidDepthFilter.getFadeDepth()));
        depthReference = depth.getModel().createReference();

        Container shorelineRow = container.addChild(createRow());
        shorelineRow.addChild(new Label("Shoreline size: "));
        shorelineValue = shorelineRow.addChild(new Label(Float.toString(fluidDepthFilter.getShorelineSize())));
        Slider shoreline = shorelineRow.addChild(createSlider(0.1f, 0, 20, fluidDepthFilter.getShorelineSize()));
        shorelineReference = shoreline.getModel().createReference();

        Checkbox distortion = container.addChild(new Checkbox("Distortion"));
        distortion.setChecked(fluidDepthFilter.isDistortion());
        distortion.addClickCommands(button -> fluidDepthFilter.setDistortion(distortion.isChecked()));

        Container distortionStrengthX = container.addChild(createRow());
        distortionStrengthX.addChild(new Label("Strength x: "));
        distStrengthXValue = distortionStrengthX.addChild(new Label(Float.toString(fluidDepthFilter.getDistortionStrengthX())));
        Slider distStrengthX = distortionStrengthX.addChild(createSlider(0.001f, 0, 0.1f, fluidDepthFilter.getDistortionStrengthX()));
        distStrengthXReference = distStrengthX.getModel().createReference();

        Container distortionStrengthY = container.addChild(createRow());
        distortionStrengthY.addChild(new Label("Strength y: "));
        distStrengthYValue = distortionStrengthY.addChild(new Label(Float.toString(fluidDepthFilter.getDistortionStrengthY())));
        Slider distStrengthY = distortionStrengthY.addChild(createSlider(0.001f, 0, 0.1f, fluidDepthFilter.getDistortionStrengthY()));
        distStrengthYReference = distStrengthY.getModel().createReference();

        Container distortionOffsetX = container.addChild(createRow());
        distortionOffsetX.addChild(new Label("Amplitude x: "));
        distAmplitudeXValue = distortionOffsetX.addChild(new Label(Float.toString(fluidDepthFilter.getDistortionAmplitudeX())));
        Slider distOffsetX = distortionOffsetX.addChild(createSlider(1f, 0, 50f, fluidDepthFilter.getDistortionAmplitudeX()));
        distAmplitudeXReference = distOffsetX.getModel().createReference();

        Container distortionAmplitudeY = container.addChild(createRow());
        distortionAmplitudeY.addChild(new Label("Amplitude y: "));
        distAmplitudeYValue = distortionAmplitudeY.addChild(new Label(Float.toString(fluidDepthFilter.getDistortionAmplitudeY())));
        Slider distOffsetY = distortionAmplitudeY.addChild(createSlider(1f, 0, 50f, fluidDepthFilter.getDistortionAmplitudeY()));
        distAmplitudeYReference = distOffsetY.getModel().createReference();

        Container distortionSpeed = container.addChild(createRow());
        distortionSpeed.addChild(new Label("Speed: "));
        distortionSpeedValue = distortionSpeed.addChild(new Label(Float.toString(fluidDepthFilter.getDistortionSpeed())));
        Slider distSpeed = distortionSpeed.addChild(createSlider(0.1f, 0f, 10f, fluidDepthFilter.getDistortionSpeed()));
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
