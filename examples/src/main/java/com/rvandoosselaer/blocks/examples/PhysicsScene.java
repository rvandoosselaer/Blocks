package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.rvandoosselaer.blocks.BlockIds;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.rvandoosselaer.blocks.TypeIds;
import com.rvandoosselaer.blocks.filters.FluidFilter;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application where you can shoot a ball using the space bar.
 * <p>
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class PhysicsScene extends SimpleApplication implements ActionListener {

    private Material ballMaterial;
    private Chunk chunk;
    private boolean chunkAttached = false;

    public static void main(String[] args) {
        LogAdapter.initialize();

        PhysicsScene physicsScene = new PhysicsScene();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        physicsScene.setSettings(settings);
        physicsScene.start();
    }

    public PhysicsScene() {
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

        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y == 0) {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.GRASS));
                    } else if ((y == 1 || y == 2) && ((x == 10 && z >= 10 && z <= 20) || (x == 20 && z >= 10 && z <= 20) || (z == 10 && x >= 10 && x <= 20) || (z == 20 && x >= 10 && x <= 20))) {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.STONE_BRICKS));
                    } else if ((y == 1 || y == 2) && (x > 10 && x < 20 && z > 10 && z < 20)) {
                        chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.WATER));
                    } else if (y == 1) {
                        int random = FastMath.nextRandomInt(0, 9);
                        if (random == 0) {
                            chunk.addBlock(x, y, z, blockRegistry.get(BlockIds.DIRT));
                        }
                    }
                }
            }
        }
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        meshGenerator.createAndSetNodeAndCollisionMesh(chunk);

        stateManager.attach(new BulletAppState());

        hideCursor();
        createCrossHair();

        inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "shoot");

        rootNode.attachChild(chunk.getNode());

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(1, 10, 10));
        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        BulletAppState bulletAppState = stateManager.getState(BulletAppState.class);
        if (bulletAppState.isInitialized() && !chunkAttached) {
            getPhysicsSpace().setGravity(new Vector3f(0, -20, 0));
            PhysicsRigidBody physicsChunk = new PhysicsRigidBody(new MeshCollisionShape(chunk.getCollisionMesh()), 0);
            physicsChunk.setPhysicsLocation(chunk.getWorldLocation());
            bulletAppState.getPhysicsSpace().addCollisionObject(physicsChunk);
            chunkAttached = true;
        }

        PostProcessingState postProcessingState = getStateManager().getState(PostProcessingState.class);
        if (postProcessingState.getFilterPostProcessor() != null && postProcessingState.getFilterPostProcessor().getFilter(FluidFilter.class) == null) {
            FluidFilter fluidFilter = new FluidFilter();
            fluidFilter.addFluidGeometry((Geometry) chunk.getNode().getChild(TypeIds.WATER));
            postProcessingState.getFilterPostProcessor().addFilter(fluidFilter);
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ("shoot".equals(name) && !isPressed) {
            Geometry ball = new Geometry("ball", new Sphere(32, 32, 0.4f));
            ball.setMaterial(getBallMaterial());
            ball.setLocalTranslation(cam.getLocation());
            ball.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            RigidBodyControl bulletControl = new RigidBodyControl(new SphereCollisionShape(0.4f), 20);
            bulletControl.setLinearVelocity(cam.getDirection().mult(25));
            ball.addControl(bulletControl);

            rootNode.attachChild(ball);
            getPhysicsSpace().add(bulletControl);
        }
    }

    private Material getBallMaterial() {
        if (ballMaterial == null) {
            ballMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            ColorRGBA color = ColorRGBA.randomColor();
            ballMaterial.setColor("Diffuse", color);
            ballMaterial.setColor("Ambient", color);
            ballMaterial.setBoolean("UseMaterialColors", true);
        }
        return ballMaterial;
    }

    private PhysicsSpace getPhysicsSpace() {
        return stateManager.getState(BulletAppState.class).getPhysicsSpace();
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
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

}
