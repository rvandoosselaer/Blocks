package com.rvandoosselaer.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author rvandoosselaer
 */
public class FacesMeshGenerationTest {

    @Test
    public void testMesh() {
        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, Blocks.GRASS);
        chunk.createNode(createMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 6 faces, 2 triangles per face
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testDoNotRenderUnwantedTriangles() {
        MeshGenerationStrategy meshGenerator = createMeshGenerator();
        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, Blocks.GRASS);
        chunk.addBlock(0, 1, 0, Blocks.GRASS);
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 10 faces, 2 triangles per face, the face between the blocks should not be rendered
        Assertions.assertEquals(10 * 2, mesh.getTriangleCount());

        // render the shared face of the not transparent block, do not render the shared face of the transparent block
        Assertions.assertTrue(Blocks.WATER.isTransparent());
        chunk.addBlock(0, 1, 0, Blocks.WATER);
        chunk.createNode(meshGenerator);

        // one geometry per type
        Assertions.assertEquals(2, chunk.getNode().getChildren().size());

        // grass
        mesh = ((Geometry) chunk.getNode().getChild(Blocks.GRASS.getType())).getMesh();
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());

        // water
        mesh = ((Geometry) chunk.getNode().getChild(Blocks.WATER.getType())).getMesh();
        Assertions.assertEquals(5 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testPyramidShape() {
        MeshGenerationStrategy meshGenerator = createMeshGenerator();

        Chunk chunk = Chunk.create(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, Blocks.PYRAMID);
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(Blocks.PYRAMID.getType())).getMesh();
        // 5 faces, 6 triangles
        Assertions.assertEquals(6, mesh.getTriangleCount());

        chunk.addBlock(0, 0, 0, Blocks.GRASS);
        chunk.addBlock(0, 1, 0, Blocks.PYRAMID);
        chunk.createNode(meshGenerator);

        mesh = ((Geometry) chunk.getNode().getChild(Blocks.GRASS.getType())).getMesh();
        // 6 faces, 10 triangles
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());

        mesh = ((Geometry) chunk.getNode().getChild(Blocks.PYRAMID.getType())).getMesh();
        // 4 faces, 4 triangles
        Assertions.assertEquals(4, mesh.getTriangleCount());
    }

    private MeshGenerationStrategy createMeshGenerator() {
        return new FacesMeshGeneration(new ShapeRegistry(), new MaterialRegistry(createAssetManager()));
    }

    private AssetManager createAssetManager() {
        return new DesktopAssetManager(true);
    }
}
