package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class NettyVarint21FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();

        byte[] abyte = new byte[3];

        for (int i = 0; i < abyte.length; i++) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }

            abyte[i] = byteBuf.readByte();

            if (abyte[i] >= 0) {
                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte));

                try {
                    int j = packetBuffer.readVarIntFromBuffer();

                    if (byteBuf.readableBytes() >= j) {
                        list.add(byteBuf.readBytes(j));
                        return;
                    }

                    byteBuf.resetReaderIndex();
                } finally {
                    packetBuffer.release();
                }

                return;
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
