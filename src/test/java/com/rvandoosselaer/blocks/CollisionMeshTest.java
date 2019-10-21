package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.simsilica.mathd.Vec3i;

/**
 * An application to test the collision mesh generation.
 * You can move through the scene using the mouse, arrow keys and A, D, W, S, Q and Z keys.
 *
 * @author rvandoosselaer
 */
public class CollisionMeshTest extends SimpleApplication {

    public static void main(String[] args) {
        CollisionMeshTest collisionMeshTest = new CollisionMeshTest();
        collisionMeshTest.start();
    }

    @Override
    public void simpleInitApp() {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    if (y == 1) {
                        if ((x >= 13 && x < 19 && z >= 13 && z < 19) || (x == 0 || x == chunkSize.x - 1 || z == 0 || z == chunkSize.z - 1)) {
                            chunk.addBlock(x, y, z, Blocks.SAND);
                        } else {
                            chunk.addBlock(x, y, z, Blocks.WATER);
                        }
                    } else {
                        chunk.addBlock(x, y, z, Blocks.SAND);
                    }
                }
            }
        }

        MeshGenerationStrategy meshGenerationStrategy = new FacesMeshGeneration(new ShapeRegistry(), new MaterialRegistry(assetManager));
        meshGenerationStrategy.generateNodeAndCollisionMesh(chunk);

        Geometry collision = new Geometry("collision mesh", chunk.getCollisionMesh());
        collision.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        collision.getMaterial().setColor("Color", ColorRGBA.Red);
        collision.getMaterial().getAdditionalRenderState().setWireframe(true);
        collision.setLocalTranslation(chunk.getWorldLocation());

        rootNode.attachChild(chunk.getNode());
        rootNode.attachChild(collision);

        rootNode.addLight(new AmbientLight(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal(), ColorRGBA.White));

        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        cam.setLocation(new Vector3f(chunkSize.x * 0.5f, 10f, chunkSize.z));
        cam.lookAt(new Vector3f(chunkSize.x * 0.5f, 0, chunkSize.z * 0.5f), Vector3f.UNIT_Y);
    }
}
