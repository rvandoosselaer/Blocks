package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SlabTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testDirection() {
        // when the slab is rotated to the west, the down face is now facing east
        Block block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.SLAB_WEST)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // vertices of the east face
        List<Vector3f> positions = TestHelper.getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        int index1 = positions.indexOf(new Vector3f(0.5f, -0.5f, -0.5f));
        int index2 = positions.indexOf(new Vector3f(0.5f, 0.5f, -0.5f));
        int index3 = positions.indexOf(new Vector3f(0.5f, -0.5f, 0.5f));
        int index4 = positions.indexOf(new Vector3f(0.5f, 0.5f, 0.5f));

        // test that those vertices exist in the mesh
        assertNotEquals(-1, index1);
        assertNotEquals(-1, index2);
        assertNotEquals(-1, index3);
        assertNotEquals(-1, index4);
    }

    @Test
    public void testDoNotRenderSharedFace() {
        Block block = Block.builder()
                .name("my-block")
                .type(TypeIds.BIRCH_LOG)
                .shape(ShapeIds.SLAB_EAST)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        chunk.addBlock(1, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh slabMesh = ((Geometry) chunk.getNode().getChild("birch_log")).getMesh();
        // the shared face between the cube and the slab should not exist. The slab shape should have 10 triangles.
        assertEquals(10, slabMesh.getTriangleCount());

        // when the cube is removed, the pole should have all 12 triangles
        chunk.removeBlock(0, 0, 0);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());
        slabMesh = ((Geometry) chunk.getNode().getChild("birch_log")).getMesh();
        assertEquals(12, slabMesh.getTriangleCount());
    }

}
