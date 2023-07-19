package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyVarint21FrameEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        int bytes = byteBuf.readableBytes();
        int size = PacketBuffer.getVarIntSize(bytes);

        if (size > 3)
            throw new IllegalArgumentException("unable to fit " + size + " into 3");

        PacketBuffer packetBuffer = new PacketBuffer(byteBuf2);
        packetBuffer.ensureWritable(size + bytes);
        packetBuffer.writeVarIntToBuffer(bytes);
        packetBuffer.writeBytes(byteBuf, byteBuf.readerIndex(), size);
    }
}
