package com.rvandoosselaer.blocks.examples;

import com.jme3.app.*;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.rvandoosselaer.blocks.*;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application where you can add and remove blocks. Place a block using the left mouse button, remove a block using
 * the right mouse button.
 *
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class BlockBuilder extends SimpleApplication implements ActionListener {

    private Node chunkNode;
    private Geometry addPlaceholder;
    private Geometry removePlaceholder;
    private BlocksManager blocksManager;
    private BlockRegistry blockRegistry;

    public static void main(String[] args) {
        LogAdapter.initialize();

        BlockBuilder blockBuilder = new BlockBuilder();
        blockBuilder.start();
    }

    public BlockBuilder() {
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

        BlocksConfig.getInstance().setGridSize(new Vec3i(3, 1, 3));
        blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        blocksManager = BlocksManager.builder()
                .meshGenerationPoolSize(1)
                .chunkGenerationPoolSize(1)
                .chunkGenerator(new FlatTerrainGenerator(blockRegistry.get(Block.GRASS)))
                .build();

        chunkNode = new Node("chunk-node");

        stateManager.attachAll(new BlocksManagerState(blocksManager), new ChunkPagerState(chunkNode, blocksManager));

        hideCursor();
        createCrossHair();
        createBlockPointers();

        inputManager.addMapping("add-block", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("remove-block", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "add-block", "remove-block");

        rootNode.attachChild(chunkNode);

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 10f, 20));
        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        CollisionResult result = getCollisionResult();

        updatePlaceholders(result);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("add-block".equals(name) && isPressed) {
            addBlock();
        } else if ("remove-block".equals(name) && isPressed) {
            removeBlock();
        }
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

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private void createBlockPointers() {
        Material removePlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        removePlaceholderMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        removePlaceholderMaterial.setColor("Color", new ColorRGBA(1, 0, 0, 0.2f));

        removePlaceholder = new Geometry("remove-placeholder", new Box(0.505f, 0.505f, 0.505f));
        removePlaceholder.setMaterial(removePlaceholderMaterial);
        removePlaceholder.setQueueBucket(RenderQueue.Bucket.Transparent);

        Material addPlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        addPlaceholderMaterial.setColor("Color", ColorRGBA.Yellow);

        addPlaceholder = new Geometry("add-placeholder", new WireBox(0.5f, 0.5f, 0.5f));
        addPlaceholder.setMaterial(addPlaceholderMaterial);
        addPlaceholder.setQueueBucket(RenderQueue.Bucket.Transparent);
    }

    private CollisionResult getCollisionResult() {
        CollisionResults collisionResults = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());

        chunkNode.collideWith(ray, collisionResults);

        return collisionResults.size() > 0 ? collisionResults.getClosestCollision() : null;
    }

    private void updatePlaceholders(CollisionResult result) {
        if (result != null) {
            Vec3i pointingLocation = BlocksManager.getPickedBlockLocation(result.getContactPoint(), result.getContactNormal(), false);
            removePlaceholder.setLocalTranslation(pointingLocation.toVector3f().addLocal(0.5f, 0.5f, 0.5f));
            if (removePlaceholder.getParent() == null) {
                rootNode.attachChild(removePlaceholder);
            }

            Vec3i placingLocation = BlocksManager.getPickedBlockLocation(result.getContactPoint(), result.getContactNormal(), true);
            addPlaceholder.setLocalTranslation(placingLocation.toVector3f().addLocal(0.5f, 0.5f, 0.5f));
            if (addPlaceholder.getParent() == null) {
                rootNode.attachChild(addPlaceholder);
            }
        } else {
            addPlaceholder.removeFromParent();
            removePlaceholder.removeFromParent();
        }
    }

    private void addBlock() {
        blocksManager.addBlock(new Vec3i(addPlaceholder.getWorldTranslation()), blockRegistry.get(Block.GRASS));
    }

    private void removeBlock() {
        blocksManager.removeBlock(new Vec3i(removePlaceholder.getWorldTranslation()));
    }

}
