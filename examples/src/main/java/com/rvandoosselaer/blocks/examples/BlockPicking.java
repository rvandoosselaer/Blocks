package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.rvandoosselaer.blocks.*;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An application that fills a chunk with random blocks. The information of the clicked block is printed in the GUI.
 *
 * @author rvandoosselaer
 */
public class BlockPicking extends SimpleApplication implements ActionListener {

    private Chunk chunk;
    private Geometry blockPlaceholder;
    private Label clickedBlock;
    private Block hoveredBlock;

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        BlockPicking blockPicking = new BlockPicking();
        blockPicking.start();
    }

    @Override
    public void simpleInitApp() {
        // setup lemur and load the default style
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
                    chunk.addBlock(x, y, z, getRandomBlock());
                }
            }
        }
        chunk.update();

        ChunkMeshGenerator chunkMeshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(chunkMeshGenerator);

        rootNode.attachChild(chunk.getNode());

        createCrossHair();
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);

        blockPlaceholder = new Geometry("placeholder", new Box(0.505f, 0.505f, 0.505f));
        Material blockPlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blockPlaceholderMaterial.setColor("Color", new ColorRGBA(1, 1, 1, 0.2f));
        blockPlaceholderMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        blockPlaceholder.setMaterial(blockPlaceholderMaterial);
        blockPlaceholder.setQueueBucket(RenderQueue.Bucket.Transparent);

        createClickedBlockInformationPanel();

        addLights(rootNode);

        setupPostProcessing();

        inputManager.addMapping("selectBlock", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "selectBlock");

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 10f, 20));
        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        CollisionResult result = getCollisionResult();

        hoveredBlock = result != null ? getHoveredBlock(result) : null;

        updatePlaceholder(result);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("selectBlock".equals(name) && isPressed) {
            updateClickedBlock();
        }
    }

    private void updatePlaceholder(CollisionResult result) {
        if (result != null) {
            Vec3i clickedBlockLocation = BlocksManager.getPickedBlockLocation(result.getContactPoint(), result.getContactNormal(), false);
            blockPlaceholder.setLocalTranslation(clickedBlockLocation.toVector3f().addLocal(0.5f, 0.5f, 0.5f));

            if (blockPlaceholder.getParent() == null) {
                rootNode.attachChild(blockPlaceholder);
            }
        } else {
            blockPlaceholder.removeFromParent();
        }
    }

    private Block getHoveredBlock(CollisionResult result) {
        Vec3i clickedBlockLocation = BlocksManager.getPickedBlockLocation(result.getContactPoint(), result.getContactNormal(), false);

        return chunk.getBlock(clickedBlockLocation);
    }

    private CollisionResult getCollisionResult() {
        CollisionResults collisionResults = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());

        chunk.getNode().collideWith(ray, collisionResults);

        return collisionResults.size() > 0 ? collisionResults.getClosestCollision() : null;
    }

    private Block getRandomBlock() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        List<Block> blocks = new ArrayList<>(blockRegistry.getAll());
        int random = FastMath.nextRandomInt(0, blocks.size() - 1);
        return blocks.get(random);
    }

    private void createCrossHair() {
        Label label = new Label("+");
        label.setColor(ColorRGBA.White);

        Camera cam = getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();
        label.setLocalTranslation((width / 2) - (label.getPreferredSize().getX() / 2), (height / 2) + (label.getPreferredSize().getY() / 2), label.getLocalTranslation().getZ());

        guiNode.attachChild(label);
    }

    private void createClickedBlockInformationPanel() {
        Container infoContainer = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.None, FillMode.None));
        infoContainer.addChild(new Label("Block: "));
        clickedBlock = infoContainer.addChild(new Label(""));
        ((Label) infoContainer.getChild(0)).setColor(ColorRGBA.White);
        ((Label) infoContainer.getChild(1)).setColor(ColorRGBA.White);

        Camera cam = getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();

        infoContainer.setLocalTranslation(width * 0.05f, height * 0.95f, 1);

        guiNode.attachChild(infoContainer);
    }

    private void updateClickedBlock() {
        if (hoveredBlock != null) {
            clickedBlock.setText(String.format("name: %s, shape: %s, type: %s, solid: %s, transparent: %s", hoveredBlock.getName(), hoveredBlock.getShape(), hoveredBlock.getType(), hoveredBlock.isSolid(), hoveredBlock.isTransparent()));
        } else {
            clickedBlock.setText("");
        }
    }

    protected void setupPostProcessing() {
        FilterPostProcessor fpp = new FilterPostProcessor(getAssetManager());
        getViewPort().addProcessor(fpp);

        // check sampling
        int samples = getContext().getSettings().getSamples();
        boolean aa = samples != 0;
        if (aa) {
            fpp.setNumSamples(samples);
        }

        // shadow filter
        DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(assetManager, 1024, 4);
        shadowFilter.setLight((DirectionalLight) rootNode.getLocalLightList().get(1));
        shadowFilter.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        shadowFilter.setEdgesThickness(2);
        shadowFilter.setShadowIntensity(0.75f);
        shadowFilter.setLambda(0.65f);
        shadowFilter.setShadowZExtend(75);
        shadowFilter.setEnabled(true);
        fpp.addFilter(shadowFilter);

        // SSAO
        SSAOFilter ssaoFilter = new SSAOFilter();
        ssaoFilter.setEnabled(false);
        fpp.addFilter(ssaoFilter);

        // setup FXAA if regular AA is off
        if (!aa) {
            FXAAFilter fxaaFilter = new FXAAFilter();
            fxaaFilter.setEnabled(true);
            fpp.addFilter(fxaaFilter);
        }
    }

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
    }
}
