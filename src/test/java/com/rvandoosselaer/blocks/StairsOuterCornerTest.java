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

/**
 * @author: rvandoosselaer
 */
public class StairsOuterCornerTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testDirection() {
        // when the stair is rotated to the west, the north face is now facing east
        Block block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.STAIRS_OUTER_CORNER_WEST)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // vertices of the east face
        List<Vector3f> positions = TestHelper.getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        int index1 = positions.indexOf(new Vector3f(0.5f, 0.167f, 0.167f));
        int index2 = positions.indexOf(new Vector3f(0.5f, -0.167f, 0.5f));
        int index3 = positions.indexOf(new Vector3f(0.5f, 0.5f, -0.5f));
        int index4 = positions.indexOf(new Vector3f(0.5f, 0.5f, -0.167f));
        int index5 = positions.indexOf(new Vector3f(0.5f, 0.167f, -0.167f));
        int index6 = positions.indexOf(new Vector3f(0.5f, -0.167f, 0.167f));
        int index7 = positions.indexOf(new Vector3f(0.5f, 0.167f, -0.5f));
        int index8 = positions.indexOf(new Vector3f(0.5f, -0.167f, -0.5f));
        int index9 = positions.indexOf(new Vector3f(0.5f, -0.5f, -0.5f));
        int index10 = positions.indexOf(new Vector3f(0.5f, -0.5f, 0.5f));
        int index11 = positions.indexOf(new Vector3f(0.5f, -0.5f, -0.5f));
        int index12 = positions.indexOf(new Vector3f(0.5f, -0.5f, 0.5f));
        int index13 = positions.indexOf(new Vector3f(0.5f, -0.5f, -0.5f));
        int index14 = positions.indexOf(new Vector3f(0.5f, -0.5f, 0.5f));

        // test that those vertices exist in the mesh
        assertNotEquals(-1, index1);
        assertNotEquals(-1, index2);
        assertNotEquals(-1, index3);
        assertNotEquals(-1, index4);
        assertNotEquals(-1, index5);
        assertNotEquals(-1, index6);
        assertNotEquals(-1, index7);
        assertNotEquals(-1, index8);
        assertNotEquals(-1, index9);
        assertNotEquals(-1, index10);
        assertNotEquals(-1, index11);
        assertNotEquals(-1, index12);
        assertNotEquals(-1, index13);
        assertNotEquals(-1, index14);
    }

    @Test
    public void testDoNotRenderSharedFace() {
        Block block = Block.builder()
                .name("my-block")
                .type(TypeIds.BIRCH_LOG)
                .shape(ShapeIds.STAIRS_OUTER_CORNER_SOUTH)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, BlocksConfig.getInstance().getBlockRegistry().get(BlockIds.GRASS));
        chunk.addBlock(1, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh stairMesh = ((Geometry) chunk.getNode().getChild("birch_log")).getMesh();
        // the shared face between the cube and the stairs should not exist. The stair shape should have 30 triangles.
        assertEquals(30, stairMesh.getTriangleCount());

        // when the cube is removed, the stair should have all 36 triangles
        chunk.removeBlock(0, 0, 0);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());
        stairMesh = ((Geometry) chunk.getNode().getChild("birch_log")).getMesh();
        assertEquals(36, stairMesh.getTriangleCount());
    }
}
