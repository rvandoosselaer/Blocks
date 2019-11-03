package com.rvandoosselaer.blocks.examples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.rvandoosselaer.blocks.*;
import com.simsilica.mathd.Vec3i;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

//TODO: rewrite
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
        chunk.createNode(meshGenerator);
        chunk.createCollisionMesh(meshGenerator);

        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        initCrossHairs();

        inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "shoot");

        rootNode.attachChild(chunk.getNode());
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        addLights(rootNode);
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(1, 10, 10));
        cam.lookAt(new Vector3f(16, 0, 16), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        BulletAppState bulletAppState = stateManager.getState(BulletAppState.class);
            if (bulletAppState.isInitialized() && !chunkAttached) {
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

            SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(0.4f);
            RigidBodyControl bulletControl = new RigidBodyControl(bulletCollisionShape, 50);
            bulletControl.setLinearVelocity(cam.getDirection().mult(25));
            ball.addControl(bulletControl);
            rootNode.attachChild(ball);
            getPhysicsSpace().add(bulletControl);
        }
    }

    private Material getBallMaterial() {
        if (ballMaterial == null) {
            ballMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            ballMaterial.setColor("Diffuse", ColorRGBA.randomColor());
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

    protected void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

}
