package dev.lemon.api.bot.network;

import com.google.common.collect.Queues;
import com.sun.istack.internal.Nullable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Queue;
import java.util.concurrent.Future;
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
            dispatch(packet, null);
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
            dispatch(packet, (GenericFutureListener<? extends Future<? super Void>>[]) ArrayUtils.add((Object[]) array, 0, listener));
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener<? extends Future<? super Void>>[]) ArrayUtils.add((Object[]) array, 0, listener)));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    private void dispatch(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>>[] array) {
        EnumConnectionState enumConnectionState = EnumConnectionState.getFromPacket(packet);
    }

    private void flushOutQueue() {
        if (isChannelOpen()) {
            this.readWriteLock.readLock().lock();

            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    InboundHandlerTuplePacketListener listener = this.outboundPacketsQueue.poll();
                    dispatch(listener.packet, listener.listener);
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
