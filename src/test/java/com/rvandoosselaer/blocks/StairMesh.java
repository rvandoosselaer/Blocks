package com.rvandoosselaer.blocks;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * @author rvandoosselaer
 */
public class StairMesh extends SimpleApplication {

    public static void main(String[] args) {
        StairMesh stairMesh = new StairMesh();
        stairMesh.start();
    }

    @Override
    public void simpleInitApp() {
        LogManager.getLogManager().getLogger("").setLevel(Level.ALL);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Spatial stair = assetManager.loadModel("stair.blend");
        stair.depthFirstTraversal(new SceneGraphVisitorAdapter() {
            @Override
            public void visit(Geometry geom) {
                GeometryBatchFactory.printMesh(geom.getMesh());
            }
        });
    }
}
