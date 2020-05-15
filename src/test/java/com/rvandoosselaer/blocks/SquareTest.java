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
public class SquareTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testDirection() {
        Block block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.SQUARE_WEST)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // vertices of the west face
        List<Vector3f> positions = TestHelper.getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        int index1 = positions.indexOf(new Vector3f(0, -0.5f, -0.5f));
        int index2 = positions.indexOf(new Vector3f(0, 0.5f, -0.5f));
        int index3 = positions.indexOf(new Vector3f(0, -0.5f, 0.5f));
        int index4 = positions.indexOf(new Vector3f(0, 0.5f, 0.5f));

        // test that only those vertices exist in the mesh
        assertEquals(4, positions.size());
        assertNotEquals(-1, index1);
        assertNotEquals(-1, index2);
        assertNotEquals(-1, index3);
        assertNotEquals(-1, index4);
    }

}
