package dev.lemon.api.bot.network;

import com.google.common.collect.Queues;
import com.sun.istack.internal.Nullable;
import io.netty.channel.*;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BotNetwork extends SimpleChannelInboundHandler<Packet<?>> {
    private Channel channel;

    private INetHandler packetListener;

    private final ReentrantReadWriteLock readWriteLock;
    private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue;

    public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");

    public BotNetwork(EnumPacketDirection direction) {
        this.outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if (this.channel.isOpen()) {
            try {
                packet.processPacket(this.packetListener);
            } catch (Exception ignored) { }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.channel = ctx.channel();

        try {
            setConnectionState(EnumConnectionState.HANDSHAKING);
        } catch (Throwable ignored) { }
    }

    public void sendPacket(Packet<?> packet) {
        if (isChannelOpen()) {
            flushOutQueue();
            dispatchPacket(packet, null);
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, new GenericFutureListener[0]));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    @SafeVarargs
    public final void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>... array) {
        if (isChannelOpen()) {
            flushOutQueue();
            dispatchPacket(packet, ArrayUtils.add(array, 0, listener));
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, ArrayUtils.add(array, 0, listener)));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    private void dispatchPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>>[] array) {
        EnumConnectionState enumConnectionState = EnumConnectionState.getFromPacket(packet);
        EnumConnectionState enumConnectionState1 = this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();

        if (enumConnectionState1 != enumConnectionState)
            this.channel.config().setAutoRead(false);

        if (this.channel.eventLoop().inEventLoop()) {
            if (enumConnectionState != enumConnectionState1)
                setConnectionState(enumConnectionState);

            ChannelFuture channelFuture = this.channel.writeAndFlush(packet);

            if (array != null)
                channelFuture.addListeners(array);

            channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(() -> {
               if (enumConnectionState != enumConnectionState1)
                   setConnectionState(enumConnectionState);

               ChannelFuture channelFuture = this.channel.writeAndFlush(packet);

               if (array != null)
                   channelFuture.addListeners(array);

                channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            });
        }
    }

    private void flushOutQueue() {
        if (isChannelOpen()) {
            this.readWriteLock.readLock().lock();

            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    InboundHandlerTuplePacketListener listener = this.outboundPacketsQueue.poll();
                    dispatchPacket(listener.packet, listener.listener);
                }
            } finally {
                this.readWriteLock.readLock().unlock();
            }
        }
    }

    public boolean isChannelOpen() {
        return (this.channel != null && this.channel.isOpen());
    }

    public void setConnectionState(EnumConnectionState state) {
        this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(state);
        this.channel.config().setAutoRead(true);
    }

    public void setNetHandler(INetHandler netHandler) {
        this.packetListener = netHandler;
    }

    public boolean isLocalChannel() {
        return (this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel);
    }

    public void setCompressionThreshold(int compressionThreshold) {
        if (compressionThreshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
                ((NettyCompressionDecoder) this.channel.pipeline().get("decompress")).setCompressionTreshold(compressionThreshold);
            else
                this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(compressionThreshold));

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
                ((NettyCompressionEncoder) this.channel.pipeline().get("compress")).setCompressionTreshold(compressionThreshold);
            else
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(compressionThreshold));
        } else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
                this.channel.pipeline().remove("decompress");

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
                this.channel.pipeline().remove("compress");
        }
    }

    static class InboundHandlerTuplePacketListener {
        private final GenericFutureListener<? extends Future<? super Void>>[] listener;

        private final Packet<?> packet;

        @SafeVarargs
        public InboundHandlerTuplePacketListener(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>... array) {
            this.packet = packet;
            this.listener = array;
        }
    }
}
