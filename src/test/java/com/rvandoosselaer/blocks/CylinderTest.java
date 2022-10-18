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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author: rvandoosselaer
 */
public class CylinderTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testDirection() {
        // when the cylinder is rotated to the east, the up face is now facing east
        Block block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.CYLINDER_EAST)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // get the center vertex of the top and bottom cap; those should be to the east and west
        List<Vector3f> positions = TestHelper.getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        int topCenter = positions.indexOf(new Vector3f(0.5f, 0, 0));
        int bottomCenter = positions.indexOf(new Vector3f(-0.5f, 0, 0));

        // test that those vertices exist in the mesh
        assertNotEquals(-1, topCenter);
        assertNotEquals(-1, bottomCenter);

        // rotate the block down
        block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.CYLINDER_DOWN)
                .usingMultipleImages(true)
                .build();

        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // get the center vertex of the top and bottom cap; those should be to the east and west
        positions = TestHelper.getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        topCenter = positions.indexOf(new Vector3f(0, -0.5f, 0));
        bottomCenter = positions.indexOf(new Vector3f(0, 0.5f, 0));

        // test that those vertices exist in the mesh
        assertNotEquals(-1, topCenter);
        assertNotEquals(-1, bottomCenter);
    }

}
