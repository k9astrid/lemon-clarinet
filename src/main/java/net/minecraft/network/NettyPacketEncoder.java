package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.List;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);

    private final EnumPacketDirection direction;

    public NettyPacketEncoder(EnumPacketDirection direction) {
        this.direction = direction;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf) throws Exception {
        EnumConnectionState state = channelHandlerContext.channel().attr(NetworkManager.attrKeyConnectionState).get();

        if (state == null)
            throw new RuntimeException("ConnectionProtocol unknown: " + byteBuf.toString());

        Integer id = state.getPacketId(this.direction, packet);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", channelHandlerContext.channel().attr(NetworkManager.attrKeyConnectionState).get(), id, byteBuf.getClass().getName());

        if (id == null)
            throw new IOException("Can't serialize unregistered packet");

        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        packetBuffer.writeVarIntToBuffer(id.intValue());

        try {
            packet.writePacketData(packetBuffer);
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
        }
    }
}
