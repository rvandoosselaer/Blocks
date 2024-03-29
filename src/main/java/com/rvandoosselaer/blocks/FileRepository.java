package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.protobuf.BlocksProtos;
import com.simsilica.mathd.Vec3i;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A File repository implementation for loading and storing chunks using the Protocol Buffers method.
 * Each chunk is stored in a separate file.
 *
 * @author rvandoosselaer
 */
@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRepository implements ChunkRepository {

    public static final String EXTENSION = ".block";

    /**
     * The path to save chunks to and load chunks from.
     */
    private Path path;

    @Override
    public Chunk load(Vec3i location) {
        if (location == null) {
            return null;
        }

        return loadChunkFromPath(getChunkPath(location));
    }

    public Chunk load(String filename) {
        if (filename == null) {
            return null;
        }

        return load(getChunkPath(filename));
    }

    private Chunk load(Path chunkPath) {
        // path doesn't exist
        if (path == null || Files.notExists(path)) {
            log.warn("Unable to load chunk {}, file path {} doesn't exist.", chunkPath.getFileName(), path.toAbsolutePath());
            return null;
        }

        // path isn't a directory
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Invalid path specified: " + path.toAbsolutePath());
        }

        // chunk doesn't exist
        if (Files.notExists(chunkPath)) {
            if (log.isTraceEnabled()) {
                log.trace("Chunk {} not found in repository", chunkPath);
            }
            return null;
        }

        return loadChunkFromPath(chunkPath);
    }

    @Override
    public boolean save(Chunk chunk) {
        if (chunk == null) {
            return false;
        }

        return save(chunk, getChunkPath(chunk));
    }

    public boolean save(Chunk chunk, String filename) {
        if (chunk == null || filename == null) {
            return false;
        }

        return save(chunk, getChunkPath(filename + EXTENSION));
    }

    private boolean save(Chunk chunk, Path chunkPath) {
        if (path == null) {
            return false;
        }

        if (Files.notExists(path))  {
            try {
                Files.createDirectories(path);
                log.info("Created directory: {}", path.toAbsolutePath());
            } catch (IOException e) {
                log.error("Error while creating directory {}: {}", path.toAbsolutePath(), e.getMessage(), e);
                return false;
            }
        }

        return writeChunkToPath(chunk, chunkPath);
    }

    public Path getChunkPath(@NonNull Chunk chunk) {
        return path != null ? Paths.get(path.toAbsolutePath().toString(), getChunkFilename(chunk)) : null;
    }

    public static String getChunkFilename(@NonNull Chunk chunk) {
        return getChunkFilename(chunk.getLocation());
    }

    private Path getChunkPath(String filename) {
        return path.resolve(filename);
    }

    private Chunk loadChunkFromPath(Path chunkPath) {
        if (log.isTraceEnabled()) {
            log.trace("Loading {}", chunkPath.toAbsolutePath());
        }

        long start = System.nanoTime();
        try (InputStream in = Files.newInputStream(chunkPath)) {
            BlocksProtos.ChunkProto chunkProto =  BlocksProtos.ChunkProto.newBuilder()
                    .mergeFrom(in)
                    .build();
            Chunk chunk = chunkProtoToChunk(chunkProto);
            if (log.isTraceEnabled()) {
                log.trace("Loading {} took {}ms", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            }

            return chunk;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private boolean writeChunkToPath(Chunk chunk, Path chunkPath) {
        if (log.isTraceEnabled()) {
            log.trace("Saving {} to {}", chunk, chunkPath.toAbsolutePath());
        }

        long start = System.nanoTime();
        try (OutputStream out = Files.newOutputStream(chunkPath)) {
            BlocksProtos.ChunkProto chunkProto = chunkToChunkProto(chunk);
            chunkProto.writeTo(out);
            if (log.isTraceEnabled()) {
                log.trace("Saving {} took {}ms", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            }
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private Path getChunkPath(@NonNull Vec3i location) {
        return path != null ? Paths.get(path.toAbsolutePath().toString(), getChunkFilename(location)) : null;
    }

    private static String getChunkFilename(@NonNull Vec3i location) {
        return "chunk_" + location.x + "_" + location.y + "_" + location.z + EXTENSION;
    }

    private static Chunk chunkProtoToChunk(@NonNull BlocksProtos.ChunkProto chunkProto) {
        Vec3i location = getVector(chunkProto.getLocationList());
        Vec3i size = getVector(chunkProto.getSizeList());

        int expectedSize = size.x * size.y * size.z;

        if (expectedSize != chunkProto.getBlocksCount()) {
            throw new IllegalStateException("Invalid block data specified! Expected " + expectedSize + " blocks, but found " + chunkProto.getBlocksCount() + " blocks.");
        }

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        Block[] blocks = chunkProto.getBlocksList().stream()
                .map(blockRegistry::get)
                .toArray(Block[]::new);

        Chunk chunk = Chunk.createAt(location);
        chunk.setBlocks(blocks);
        chunk.update();

        return chunk;
    }

    private static BlocksProtos.ChunkProto chunkToChunkProto(@NonNull Chunk chunk) {
        Vec3i size = BlocksConfig.getInstance().getChunkSize();

        return BlocksProtos.ChunkProto.newBuilder()
                // location
                .addLocation(chunk.getLocation().x)
                .addLocation(chunk.getLocation().y)
                .addLocation(chunk.getLocation().z)
                // size
                .addSize(size.x)
                .addSize(size.y)
                .addSize(size.z)
                .addAllBlocks(Arrays.stream(chunk.getBlocks())
                        .map(block -> block != null ? block.getName() : BlockIds.EMPTY)
                        .collect(Collectors.toList()))
                .build();
    }

    private static Vec3i getVector(@NonNull List<Integer> integers) {
        if (integers.size() != 3) {
            throw new IllegalArgumentException("Invalid vector data specified! data: " + integers);
        }

        return new Vec3i(integers.get(0), integers.get(1), integers.get(2));
    }

}
