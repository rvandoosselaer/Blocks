package com.rvandoosselaer.blocks;

import com.jme3.material.RenderState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.mikktspace.MikkTSpaceImpl;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;
import com.simsilica.mathd.Vec3i;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A mesh generation implementation that creates a mesh for each block type in the chunk. A geometry is created with
 * the generated mesh and the material retrieved from the {@link MaterialRegistry}. The geometry is attached to the
 * node, and the node is positioned based on the location of the chunk.
 * If lod generation was enabled, an lod level will be created for each supplied reduction value.
 *
 * @author remy
 */
@Slf4j
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class FacesMeshGeneration implements MeshGenerationStrategy {

    private final ShapeRegistry shapeRegistry;
    private final MaterialRegistry materialRegistry;

    @Override
    public Node generateNode(Chunk chunk) {
        long start = System.nanoTime();

        // create the node of the chunk
        Vec3i chunkLocation = chunk.getLocation();
        Node node = new Node("Chunk - " + chunkLocation);

        // create the map holding all the meshes of the chunk
        Map<String, ChunkMesh> meshMap = new HashMap<>();

        // the first block location is (0, 0, 0)
        Vec3i blockLocation = new Vec3i(0, 0, 0);

        for (Block block : chunk.getBlocks()) {
            // check if there is a block
            if (block != null) {
                // create a mesh for each different block type
                ChunkMesh mesh = meshMap.get(block.getType());
                if (mesh == null) {
                    mesh = new ChunkMesh();
                    meshMap.put(block.getType(), mesh);
                }

                // add the block mesh to the chunk mesh
                Shape shape = shapeRegistry.get(block.getShape());
                shape.add(blockLocation, chunk, mesh);
            }

            // increment the block location
            incrementBlockLocation(blockLocation);
        }

        if (log.isTraceEnabled()) {
            log.trace("Chunk {} meshes construction took {}ms", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }

        // create a geometry for each type of block
        meshMap.forEach((type, chunkMesh) -> {
            Mesh mesh = chunkMesh.generateMesh();
            if (mesh.getBuffer(VertexBuffer.Type.Tangent) == null) {
                long tangentGeneratorStart = System.nanoTime();
                MikktspaceTangentGenerator.genTangSpaceDefault(new MikkTSpaceImpl(mesh));
                if (log.isTraceEnabled()) {
                    log.trace("Generating tangents using {} took {}ms", MikktspaceTangentGenerator.class, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - tangentGeneratorStart));
                }
            }
            Geometry geometry = new Geometry(type, mesh);
            geometry.setMaterial(materialRegistry.get(type));
            geometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            if (geometry.getMaterial().getAdditionalRenderState().getBlendMode() == RenderState.BlendMode.Alpha) {
                if (log.isTraceEnabled()) {
                    log.trace("Setting queue bucket to {} for geometry {}", RenderQueue.Bucket.Transparent, geometry);
                }
                geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
            }
            node.attachChild(geometry);
        });

        // clear the map for gc purposes
        meshMap.clear();

        // position the node
        node.setLocalTranslation(chunk.getWorldLocation());

        if (log.isTraceEnabled()) {
            log.trace("Total chunk node generation took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }
        return node;
    }

    @Override
    public Mesh generateCollisionMesh(Chunk chunk) {
        long start = System.nanoTime();

        // create the collision mesh
        ChunkMesh collisionMesh = new ChunkMesh(true);

        // the first block location is (0, 0, 0)
        Vec3i blockLocation = new Vec3i(0, 0, 0);

        for (Block block : chunk.getBlocks()) {
            if (block != null && block.isCollidable()) {
                // add the block to the collision mesh
                Shape shape = shapeRegistry.get(block.getShape());
                shape.add(blockLocation, chunk, collisionMesh);
            }

            // increment the block location
            incrementBlockLocation(blockLocation);
        }

        if (log.isTraceEnabled()) {
            log.trace("Chunk {} collision mesh construction took {}ms", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }

        Mesh mesh = collisionMesh.generateMesh();
        if (log.isTraceEnabled()) {
            log.trace("Total collision mesh generation took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }

        return mesh;
    }

    @Override
    public void generateNodeAndCollisionMesh(Chunk chunk) {
        long start = System.nanoTime();

        // create the node of the chunk
        Vec3i chunkLocation = chunk.getLocation();
        Node node = new Node("Chunk - " + chunkLocation);

        // create the map holding all the meshes of the chunk and the collision mesh
        Map<String, ChunkMesh> meshMap = new HashMap<>();
        ChunkMesh collisionMesh = new ChunkMesh(true);

        // the first block location is (0, 0, 0)
        Vec3i blockLocation = new Vec3i(0, 0, 0);

        for (Block block : chunk.getBlocks()) {
            // check if there is a block
            if (block != null) {
                // create a mesh for each different block type
                ChunkMesh mesh = meshMap.get(block.getType());
                if (mesh == null) {
                    mesh = new ChunkMesh();
                    meshMap.put(block.getType(), mesh);
                }

                // add the block mesh to the chunk mesh
                Shape shape = shapeRegistry.get(block.getShape());
                shape.add(blockLocation, chunk, mesh);

                // add the block to the collision mesh
                if (block.isCollidable()) {
                    shape.add(blockLocation, chunk, collisionMesh);
                }
            }

            // increment the block location
            incrementBlockLocation(blockLocation);
        }

        if (log.isTraceEnabled()) {
            log.trace("Chunk {} meshes construction took {}ms", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }

        // create a geometry for each type of block
        meshMap.forEach((type, chunkMesh) -> {
            Mesh mesh = chunkMesh.generateMesh();
            if (mesh.getBuffer(VertexBuffer.Type.Tangent) == null) {
                long tangentGeneratorStart = System.nanoTime();
                MikktspaceTangentGenerator.genTangSpaceDefault(new MikkTSpaceImpl(mesh));
                if (log.isTraceEnabled()) {
                    log.trace("Generating tangents using {} took {}ms", MikktspaceTangentGenerator.class, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - tangentGeneratorStart));
                }
            }
            Geometry geometry = new Geometry(type, mesh);
            geometry.setMaterial(materialRegistry.get(type));
            geometry.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            if (geometry.getMaterial().getAdditionalRenderState().getBlendMode() == RenderState.BlendMode.Alpha) {
                if (log.isTraceEnabled()) {
                    log.trace("Setting queue bucket to {} for geometry {}", RenderQueue.Bucket.Transparent, geometry);
                }
                geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
            }
            node.attachChild(geometry);
        });

        // clear the map for gc purposes
        meshMap.clear();

        // position the node
        node.setLocalTranslation(chunk.getWorldLocation());

        // set the node and collision mesh on the chunk
        chunk.setNode(node);
        chunk.setCollisionMesh(collisionMesh.generateMesh());

        if (log.isTraceEnabled()) {
            log.trace("Total chunk node generation took {}ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        }
    }

    /**
     * Reverse calculate the index of the block in the chunk blocks array. When looping through the blocks array, this
     * method should be called once per iteration to know the location of the current block.
     * The first passed block location should be Vec3i(0, 0, 0).
     *
     * @param blockLocation the current block location in the chunk block array
     * @return the next block location in the chunk block array
     */
    private Vec3i incrementBlockLocation(Vec3i blockLocation) {
        // reverse calculate the block location, based on the position in the array.
        // eg. for a chunk(3,3,3) the index is calculated as followed:
        // [0] = block(0,0,0)
        // [1] = block(0,0,1)
        // [2] = block(0,0,2)
        // [3] = block(0,1,0)
        // ...
        // [26] = block(2,2,2)
        if (blockLocation.z + 1 >= BlocksConfig.getInstance().getChunkSize().z) {
            blockLocation.z = 0;
            if (blockLocation.y + 1 >= BlocksConfig.getInstance().getChunkSize().y) {
                blockLocation.y = 0;
                blockLocation.x++;
            } else {
                blockLocation.y++;
            }
        } else {
            blockLocation.z++;
        }
        return blockLocation;
    }

}
