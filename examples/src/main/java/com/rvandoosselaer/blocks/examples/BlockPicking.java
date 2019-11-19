package com.rvandoosselaer.blocks.examples;

import com.jme3.app.*;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.WireBox;
import com.rvandoosselaer.blocks.*;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

import java.util.ArrayList;
import java.util.List;

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
        LogAdapter.initialize();

        BlockPicking blockPicking = new BlockPicking();
        blockPicking.start();
    }

    public BlockPicking() {
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

        chunk = Chunk.createAt(new Vec3i());
        for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
            for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
                chunk.addBlock(x, 0, z, getRandomBlock());
            }
        }
        chunk.update();

        ChunkMeshGenerator chunkMeshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(chunkMeshGenerator);

        rootNode.attachChild(chunk.getNode());

        hideCursor();
        createCrossHair();
        createBlockPointer();

        createClickedBlockInformationPanel();

        inputManager.addMapping("selectBlock", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "selectBlock");

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(0, 10f, 20));
        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
    }

    private void createBlockPointer() {
        blockPlaceholder = new Geometry("placeholder", new WireBox(0.505f, 0.505f, 0.505f));
        Material blockPlaceholderMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blockPlaceholderMaterial.setColor("Color", ColorRGBA.Yellow);
        blockPlaceholder.setMaterial(blockPlaceholderMaterial);
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

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

    private void createClickedBlockInformationPanel() {
        Container infoContainer = new Container(new SpringGridLayout(Axis.X, Axis.Y, FillMode.None, FillMode.None));
        infoContainer.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 0.9f)));
        infoContainer.addChild(new Label("Block: "));
        clickedBlock = infoContainer.addChild(new Label(""));

        Camera cam = getCamera();
        infoContainer.setLocalTranslation(0, cam.getHeight(), 1);

        guiNode.attachChild(infoContainer);
    }

    private void updateClickedBlock() {
        if (hoveredBlock != null) {
            clickedBlock.setText(String.format("name: %s, shape: %s, type: %s, solid: %s, transparent: %s", hoveredBlock.getName(), hoveredBlock.getShape(), hoveredBlock.getType(), hoveredBlock.isSolid(), hoveredBlock.isTransparent()));
        } else {
            clickedBlock.setText("");
        }
    }

}
