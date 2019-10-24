package com.rvandoosselaer.blocks;

import com.simsilica.mathd.Vec3i;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.nio.file.Path;

/**
 * when a chunk is empty, don't store the 'block' data.
 * message chunk {
 * required location: 0, 0, 0
 * required size: 32, 32, 32
 * repeated block
 * }
 * message block {
 * name
 * type
 * shape
 * solid
 * transparent
 * multipleImages
 * }
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRepository implements ChunkRepository {

    public static final String EXTENSION = ".block";

    private Path path;

    @Override
    public Chunk load(Vec3i location) {
//        long start = System.nanoTime();
//
//        // path doesn't exist
//        if (Files.notExists(path)) {
//            log.warn("Unable to load chunk {}, file repository path {} doesn't exist.", location, path.toAbsolutePath());
//            return null;
//        }
//
//        // path isn't a directory
//        if (!Files.isDirectory(path)) {
//            throw new IllegalArgumentException("Invalid path specified: " + path.toAbsolutePath());
//        }
//
//        // chunk doesn't exist
//        if (Files.notExists(Paths.get(path.toAbsolutePath().toString(), getChunkFilename(location)))) {
//            if (log.isTraceEnabled()) {
//                log.trace("Chunk {} not found in repository", location);
//            }
//            return null;
//        }
        /**
         *         Path chunkFile = Paths.get(root.toAbsolutePath().toString(), getChunkFilename(location));
         *         Chunk chunk = null;
         *         try (InputStream in = Files.newInputStream(chunkFile);
         *              ObjectInputStream oin = new ObjectInputStream(in)) {
         *             if (log.isTraceEnabled()) {
         *                 log.trace("Loading chunk {} at {}...", location, chunkFile);
         *             }
         *             Block[] blocks = (Block[]) oin.readObject();
         *             chunk = new Chunk(location);
         *             chunk.setBlocks(blocks);
         *             chunk.update();
         *         } catch (IOException | ClassNotFoundException e) {
         *             log.error("Error while loading chunk {}: {}", location, e.getMessage(), e);
         *         }
         *
         *         if (log.isTraceEnabled()) {
         *             log.trace("{} loaded [{}ms]", chunk, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
         *         }
         *         return chunk;
         */
        return null;
    }

    @Override
    public boolean save(Chunk chunk) {
        return false;
    }

    static String getChunkFilename(@NonNull Vec3i location) {
        return "chunk_" + location.x + "_" + location.y + "_" + location.z + EXTENSION;
    }

}
