package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.simsilica.mathd.Vec3i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: rvandoosselaer
 */
public class CubeTest {

    private static final float precision = 1000f;

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testDirection() {
        // when the cube is rotated to the north, the top texture should be on the north face.
        Block block = Block.builder()
                .name("my-block")
                .type("grass")
                .shape(ShapeIds.CUBE_NORTH)
                .usingMultipleImages(true)
                .build();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, block);
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild("grass")).getMesh();

        // vertices of the north face
        List<Vector3f> positions = getPositions(mesh.getBuffer(VertexBuffer.Type.Position));
        int index1 = positions.indexOf(new Vector3f(-0.5f, -0.5f, -0.5f));
        int index2 = positions.indexOf(new Vector3f(-0.5f, 0.5f, -0.5f));
        int index3 = positions.indexOf(new Vector3f(0.5f, -0.5f, -0.5f));
        int index4 = positions.indexOf(new Vector3f(0.5f, 0.5f, -0.5f));

        // uv coordinates of the top image
        Set<Vector2f> upFaceUvs = new HashSet<>();
        upFaceUvs.add(new Vector2f(1.0f, 1.0f));
        upFaceUvs.add(new Vector2f(0.0f, 1.0f));
        upFaceUvs.add(new Vector2f(1.0f, 0.667f));
        upFaceUvs.add(new Vector2f(0.0f, 0.667f));

        // check the uv coordinates of the north face, map to the top image
        List<Vector2f> uvs = getTexCoords(mesh.getBuffer(VertexBuffer.Type.TexCoord));
        assertTrue(upFaceUvs.contains(uvs.get(index1)));
        assertTrue(upFaceUvs.contains(uvs.get(index2)));
        assertTrue(upFaceUvs.contains(uvs.get(index3)));
        assertTrue(upFaceUvs.contains(uvs.get(index4)));
    }

    private static List<Vector3f> getPositions(VertexBuffer positionBuffer) {
        List<Vector3f> positions = new ArrayList<>();
        for (int i = 0; i < positionBuffer.getNumElements(); i++) {
            Vector3f v = new Vector3f();
            for (int j = 0; j < positionBuffer.getNumComponents(); j++) {
                float value = (Float) positionBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        v.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        v.setY(Math.round(value * precision) / precision);
                        break;
                    case 2:
                        v.setZ(Math.round(value * precision) / precision);
                        break;
                }
            }
            positions.add(v);
        }
        return positions;
    }

    private static List<Vector2f> getTexCoords(VertexBuffer uvBuffer) {
        List<Vector2f> texCoords = new ArrayList<>();
        for (int i = 0; i < uvBuffer.getNumElements(); i++) {
            Vector2f v = new Vector2f();
            for (int j = 0; j < uvBuffer.getNumComponents(); j++) {
                float value = (Float) uvBuffer.getElementComponent(i, j);
                switch (j) {
                    case 0:
                        v.setX(Math.round(value * precision) / precision);
                        break;
                    case 1:
                        v.setY(Math.round(value * precision) / precision);
                        break;
                }
            }
            texCoords.add(v);
        }
        return texCoords;
    }


}
