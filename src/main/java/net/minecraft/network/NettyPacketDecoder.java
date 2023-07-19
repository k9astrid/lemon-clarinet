package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.List;

public class NettyPacketDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);

    private final EnumPacketDirection direction;

    public NettyPacketDecoder(EnumPacketDirection direction) {
        this.direction = direction;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() != 0) {
            PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

            int id = packetBuffer.readVarIntFromBuffer();

            Packet<?> packet = channelHandlerContext.channel().attr(NetworkManager.attrKeyConnectionState).get().getPacket(this.direction, id);
            if (packet == null)
                throw new IOException("Bad packet id " + id);

            packet.readPacketData(packetBuffer);

            if (packetBuffer.readableBytes() > 0)
                throw new IOException("Packet " + channelHandlerContext.channel().attr(NetworkManager.attrKeyConnectionState).get().getId() + "/" + id + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetBuffer.readableBytes() + " bytes extra whilst reading packet " + id);

            list.add(packet);

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", channelHandlerContext.channel().attr(NetworkManager.attrKeyConnectionState).get(), Integer.valueOf(id), packet.getClass().getSimpleName());
        }
    }
}
