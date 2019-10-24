package com.rvandoosselaer.blocks;

import com.rvandoosselaer.blocks.protobuf.BlocksProtos;
import com.simsilica.mathd.Vec3i;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Move to {@link FileRepository}
 * @author rvandoosselaer
 */
@RequiredArgsConstructor
public class ProtobufHelper {

    private final BlockRegistry blockRegistry;

    public Chunk from(@NonNull BlocksProtos.ChunkProto chunkProto) {
        Vec3i location = getVector(chunkProto.getLocationList());
        Vec3i size = getVector(chunkProto.getSizeList());

        int expectedSize = size.x * size.y * size.z;

        if (expectedSize != chunkProto.getBlocksCount()) {
            throw new IllegalStateException("Invalid block data specified! Expected " + expectedSize + " blocks, but found " + chunkProto.getBlocksCount() + " blocks.");
        }

        Block[] blocks = chunkProto.getBlocksList().stream()
                .map(blockProto -> blockRegistry.get(blockProto.getName()))
                .toArray(Block[]::new);

        return Chunk.createFrom(location, blocks);
    }

    public static BlocksProtos.ChunkProto from(@NonNull Chunk chunk) {
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
                        .map(block -> BlocksProtos.BlockProto.newBuilder()
                                .setName(block.getName())
                                .setShape(block.getShape())
                                .setType(block.getType())
                                .setMultipleImages(block.isUsingMultipleImages())
                                .setTransparent(block.isTransparent())
                                .setSolid(block.isSolid())
                                .build())
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
