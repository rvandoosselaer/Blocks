package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.rvandoosselaer.blocks.*;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * An application where you can shoot a ball using the space bar.
 *
 * @author rvandoosselaer
 */
public class PhysicsScene extends SimpleApplication implements ActionListener {

    private Material ballMaterial;
    private Chunk chunk;
    private boolean chunkAttached = false;

    public static void main(String[] args) {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        PhysicsScene physicsScene = new PhysicsScene();
        physicsScene.start();
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
                        chunk.addBlock(x, y, z, blockRegistry.get(Block.GRASS));
                    } else if ((y == 1 || y == 2 ) && ((x == 10 && z >= 10 && z <= 20) || (x == 20 && z >= 10 && z <= 20) || (z == 10 && x >= 10 && x <= 20) || (z == 20 && x >= 10 && x <= 20))) {
                        chunk.addBlock(x, y, z, blockRegistry.get(Block.STONE));
                    } else if ((y == 1 || y == 2) && (x > 10 && x < 20 && z > 10 && z < 20)) {
                        chunk.addBlock(x, y, z, blockRegistry.get(Block.WATER));
                    } else if (y == 1) {
                        int random = FastMath.nextRandomInt(0, 9);
                        if (random == 0) {
                            chunk.addBlock(x, y, z, blockRegistry.get(Block.DIRT));
                        }
                    }
                }
            }
        }
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        meshGenerator.createAndSetNodeAndCollisionMesh(chunk);

        stateManager.attach(new BulletAppState());

        createCrossHair();
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);

        inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "shoot");

        rootNode.attachChild(chunk.getNode());
        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        addLights(rootNode);

        setupPostProcessing();

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

    private static void addLights(Node node) {
        node.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        node.addLight(new DirectionalLight(new Vector3f(-0.2f, -1.0f, -0.2f).normalizeLocal(), ColorRGBA.White));
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
