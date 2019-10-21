package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3i;

/**
 * An application to test the water blocks.
 * You can move through the scene using the mouse, arrow keys and A, D, W, S, Q and Z keys.
 *
 * @rvandoosselaer
 */
public class WaterBlockTest extends SimpleApplication {

    public static void main(String[] args) {
        WaterBlockTest waterBlockTest = new WaterBlockTest();
        waterBlockTest.start();
    }

    @Override
    public void simpleInitApp() {
        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    chunk.addBlock(x, y, z, y == 0 ? Blocks.SAND : Blocks.WATER);
                }
            }
        }
        chunk.update();

        chunk.createNode(new FacesMeshGeneration(new ShapeRegistry(), new MaterialRegistry(assetManager)));

        rootNode.attachChild(chunk.getNode());
        rootNode.addLight(new AmbientLight(ColorRGBA.White.mult(0.2f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(-0.2f, -1f, -0.2f).normalizeLocal(), ColorRGBA.White));

        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        cam.setLocation(new Vector3f(chunkSize.x * 0.5f, 4f, chunkSize.z));
    }

}
