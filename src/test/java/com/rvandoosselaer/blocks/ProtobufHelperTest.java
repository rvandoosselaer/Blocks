package com.rvandoosselaer.blocks;

/**
 * @author rvandoosselaer
 */
public class ProtobufHelperTest {

//    @Test
//    public void testMapping() {
//        Configurator.setLevel("com.rvandoosselaer.blocks", Level.DEBUG);
//
//        BlocksConfig.getInstance().setChunkSize(new Vec3i(32, 32, 32));
//        Chunk chunk = Chunk.createAt(new Vec3i(1, 2, 3));
//
//        for (int x = 0; x < BlocksConfig.getInstance().getChunkSize().x; x++) {
//            for (int y = 0; y < BlocksConfig.getInstance().getChunkSize().y; y++) {
//                for (int z = 0; z < BlocksConfig.getInstance().getChunkSize().z; z++) {
//                    chunk.addBlock(x, y, z, Blocks.GRASS);
//                }
//            }
//        }
//
//        BlocksProtos.ChunkProto chunkProto = ProtobufHelper.from(chunk);
//
//        // location
//        Assertions.assertEquals(chunkProto.getLocation(0), 1);
//        Assertions.assertEquals(chunkProto.getLocation(1), 2);
//        Assertions.assertEquals(chunkProto.getLocation(2), 3);
//
//        // blocks
//        Assertions.assertEquals(chunkProto.getBlocksCount(), chunk.getBlocks().length);
//
//        BlockRegistry blockRegistry = new BlockRegistry();
//        blockRegistry.register(Blocks.GRASS.getName(), Blocks.GRASS);
//
//        ProtobufHelper protobufHelper = new ProtobufHelper(blockRegistry);
//        Chunk other = protobufHelper.from(chunkProto);
//
//        Assertions.assertEquals(chunk.getLocation(), other.getLocation());
//        Assertions.assertEquals(chunk.getBlocks().length, other.getBlocks().length);
//
//    }
}
