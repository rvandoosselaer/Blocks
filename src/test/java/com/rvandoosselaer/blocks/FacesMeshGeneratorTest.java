package com.rvandoosselaer.blocks;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.simsilica.mathd.Vec3i;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rvandoosselaer
 */
public class FacesMeshGeneratorTest {

    @BeforeAll
    public static void setUp() {
        BlocksConfig.initialize(new DesktopAssetManager(true));
    }

    @Test
    public void testMesh() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("grass"));
        chunk.createNode(BlocksConfig.getInstance().getChunkMeshGenerator());

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 6 faces, 2 triangles per face
        assertEquals(6 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testDoNotRenderUnwantedTriangles() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 0, blockRegistry.get("grass"));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 10 faces, 2 triangles per face, the face between the blocks should not be rendered
        assertEquals(10 * 2, mesh.getTriangleCount());

        // render the shared face of the not transparent block, do not render the shared face of the transparent block
        assertTrue(blockRegistry.get("water").isTransparent());
        chunk.addBlock(0, 1, 0, blockRegistry.get("water"));
        chunk.createNode(meshGenerator);

        // one geometry per type
        assertEquals(2, chunk.getNode().getChildren().size());

        // grass
        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        assertEquals(6 * 2, mesh.getTriangleCount());

        // water
        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("water").getType())).getMesh();
        assertEquals(5 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testDoNotRenderSharedTrianglesBetweenBlocksInNeighbouringChunks() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        Vec3i chunkSize = BlocksConfig.getInstance().getChunkSize();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(chunkSize.x - 1, 0, 0, blockRegistry.get("grass"));

        Chunk neighbour = Chunk.createAt(new Vec3i(1, 0, 0));
        neighbour.addBlock(0, 0, 0, blockRegistry.get("grass"));

        ChunkCache chunkResolver = new ChunkCache();
        chunkResolver.addAll(chunk, neighbour);
        chunk.setChunkResolver(chunkResolver);
        neighbour.setChunkResolver(chunkResolver);

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);
        neighbour.createNode(meshGenerator);

        // The shared face between the blocks in neighbouring chunks should not be rendered
        Mesh mesh = ((Geometry) chunk.getNode().getChild(0)).getMesh();
        // 5 faces, 2 triangles per face
        assertEquals(5 * 2, mesh.getTriangleCount());

        mesh = ((Geometry) neighbour.getNode().getChild(0)).getMesh();
        // 5 faces, 2 triangles per face
        assertEquals(5 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testPyramidShape() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.BRICK_PYRAMID));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get(BlockIds.BRICK_PYRAMID).getType())).getMesh();
        // 5 faces, 6 triangles
        assertEquals(6, mesh.getTriangleCount());

        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.GRASS));
        chunk.addBlock(0, 1, 0, blockRegistry.get(BlockIds.BRICK_PYRAMID));
        chunk.createNode(meshGenerator);

        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get(BlockIds.GRASS).getType())).getMesh();
        // 6 faces, 10 triangles
        assertEquals(6 * 2, mesh.getTriangleCount());

        mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get(BlockIds.BRICK_PYRAMID).getType())).getMesh();
        // 4 faces, 4 triangles
        assertEquals(4, mesh.getTriangleCount());
    }

    @Test
    public void testWedgeShape() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.BRICK_WEDGE_FRONT));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get(BlockIds.BRICK_WEDGE_FRONT).getType())).getMesh();
        // 5 faces, 8 triangles
        assertEquals(8, mesh.getTriangleCount());

        // test other wedge directions
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_WEDGE_BACK));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_WEDGE_FRONT));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_WEDGE_LEFT));
        testEnclosedWedge(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_WEDGE_RIGHT));
    }

    private void testEnclosedWedge(Chunk chunk, ChunkMeshGenerator meshGenerator, Block block) {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        chunk.addBlock(1, 0, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 2, blockRegistry.get("grass"));
        chunk.addBlock(2, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 2, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 1, block);
        chunk.createNode(meshGenerator);

        // cube mesh
        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        // 6 cubes = 6 * (6 faces = 12 triangles)
        assertEquals(6 * 12, mesh.getTriangleCount());

        // wedge mesh
        mesh = ((Geometry) chunk.getNode().getChild(block.getType())).getMesh();
        // 1 face, 2 triangles
        assertEquals(2, mesh.getTriangleCount());
    }

    @Test
    public void testStairShape() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.BRICK_STAIRS_FRONT));
        chunk.createNode(meshGenerator);

        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get(BlockIds.BRICK_STAIRS_FRONT).getType())).getMesh();
        // 14 faces
        assertEquals(14 * 2, mesh.getTriangleCount());

        testEnclosedStair(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_STAIRS_BACK));
        testEnclosedStair(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_STAIRS_FRONT));
        testEnclosedStair(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_STAIRS_LEFT));
        testEnclosedStair(Chunk.createAt(new Vec3i(0, 0, 0)), meshGenerator, blockRegistry.get(BlockIds.BRICK_STAIRS_RIGHT));
    }

    @Test
    public void testStairCollisionShape() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.BRICK_STAIRS_FRONT));
        Mesh mesh = chunk.createCollisionMesh(meshGenerator);

        // 5 faces, 8 triangles
        assertEquals(8, mesh.getTriangleCount());
    }

    private void testEnclosedStair(Chunk chunk, ChunkMeshGenerator meshGenerator, Block block) {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();

        chunk.addBlock(1, 0, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 0, blockRegistry.get("grass"));
        chunk.addBlock(0, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 2, blockRegistry.get("grass"));
        chunk.addBlock(2, 1, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 2, 1, blockRegistry.get("grass"));
        chunk.addBlock(1, 1, 1, block);
        chunk.createNode(meshGenerator);

        // cube mesh
        Mesh mesh = ((Geometry) chunk.getNode().getChild(blockRegistry.get("grass").getType())).getMesh();
        // 6 cubes, 6 faces / cube
        assertEquals(6 * 6 * 2, mesh.getTriangleCount());

        // stair mesh
        mesh = ((Geometry) chunk.getNode().getChild(block.getType())).getMesh();
        // 6 faces
        assertEquals(6 * 2, mesh.getTriangleCount());
    }

    @Test
    public void testRoundedCubeCollisionShape() {
        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));
        chunk.addBlock(0, 0, 0, blockRegistry.get(BlockIds.GRASS_ROUNDED));
        chunk.createCollisionMesh(meshGenerator);

        Mesh mesh = chunk.getCollisionMesh();
        // 6 faces
        assertEquals(6 * 2, mesh.getTriangleCount());
    }

    private static class ChunkCache implements ChunkResolver {

        private final Map<Vec3i, Chunk> cache = new HashMap<>();

        public void add(Chunk chunk) {
            cache.put(chunk.getLocation(), chunk);
        }

        public void addAll(Chunk... chunks) {
            Arrays.stream(chunks).forEach(this::add);
        }

        @Override
        public Optional<Chunk> get(@NonNull Vec3i location) {
            return Optional.ofNullable(getChunk(location));
        }

        @Override
        public Chunk getChunk(@NonNull Vec3i location) {
            return cache.get(location);
        }

    }

}
