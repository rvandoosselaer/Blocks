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
        BlockRegistry blockRegistry = new BlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("grass"));
        chunk.createNode(createMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 6 faces, 2 triangles per face
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testDoNotRenderUnwantedTriangles() {
        BlockRegistry blockRegistry = new BlockRegistry();

        MeshGenerationStrategy meshGenerator = createMeshGenerator();
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 0, blockRegistry.get("grass"));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 10 faces, 2 triangles per face, the face between the blocks should not be rendered
        Assertions.assertEquals(10 * 2, mesh.getTriangleCount());

        // render the shared face of the not transparent block, do not render the shared face of the transparent block
        Assertions.assertTrue(blockRegistry.get("water").isTransparent());
        chunk.addBlock(0, 1, 0, blockRegistry.get("water"));
        chunk.createNode(meshGenerator);

        // one geometry per type
        Assertions.assertEquals(2, chunk.getNode().getChildren().size());

        // grass
        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());

        // water
        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("water").getType())).getMesh();
        Assertions.assertEquals(5 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testPyramidShape() {
        BlockRegistry blockRegistry = new BlockRegistry();

        MeshGenerationStrategy meshGenerator = createMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("stonebrick-merlon"));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("stonebrick-merlon").getType())).getMesh();
        // 5 faces, 6 triangles
        Assertions.assertEquals(6, mesh.getTriangleCount());

        chunk.addBlock(0, 0, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 0, blockRegistry.get("stonebrick-merlon"));
        chunk.createNode(meshGenerator);

        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        // 6 faces, 10 triangles
        Assertions.assertEquals(6 * 2, mesh.getTriangleCount());

        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("stonebrick-merlon").getType())).getMesh();
        // 4 faces, 4 triangles
        Assertions.assertEquals(4, mesh.getTriangleCount());
    }

    @Test
    public void testWedgeShape() {
        BlockRegistry blockRegistry = new BlockRegistry();

        MeshGenerationStrategy meshGenerator = createMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("stonebrick-wedge-front"));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("stonebrick-wedge-front").getType())).getMesh();
        // 5 faces, 8 triangles
        Assertions.assertEquals(8, mesh.getTriangleCount());

        // test other wedge directions
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get("stonebrick-wedge-front"));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get("stonebrick-wedge-right"));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get("stonebrick-wedge-back"));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get("stonebrick-wedge-left"));
    }

    private void testEnclosedWedge(Chunk chunk, MeshGenerationStrategy meshGenerator, Block block) {
        BlockRegistry blockRegistry = new BlockRegistry();

        chunk.addBlock(1, 0, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 2, blockRegistry.get("grass"));
        chunk.addBlock(2, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 1, block);
        chunk.createNode(meshGenerator);

        // cube mesh
        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        // 5 cubes = 5 * (6 faces, 12 triangles)
        Assertions.assertEquals(5 * 12, mesh.getTriangleCount());

        // wedge mesh
        mesh = ((Geometry) chunk.getNode().getChild(block.getType())).getMesh();
        // 1 face, 2 triangles
        Assertions.assertEquals(2, mesh.getTriangleCount());
    }

    private MeshGenerationStrategy createMeshGenerator() {
        return new FacesMeshGeneration(new ShapeRegistry(), new MaterialRegistry(createAssetManager()));
    }

    private AssetManager createAssetManager() {
        return new DesktopAssetManager(true);
    }
}
