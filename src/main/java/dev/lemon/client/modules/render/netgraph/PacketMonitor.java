package dev.lemon.client.modules.render.netgraph;

import java.util.ArrayDeque;
import java.util.Deque;

public class PacketMonitor {
    private final Deque<Integer> packets = new ArrayDeque<>();

    private int counter;

    private long lastReset;

    public Deque<Integer> getPacketRecord() {
        return packets;
    }

    void update(final int maxSamples) {
        if (System.currentTimeMillis() - this.lastReset > 1000L) {
            this.packets.addFirst(this.counter);
            this.lastReset = System.currentTimeMillis();
            this.counter = 0;

            if (this.packets.size() > maxSamples)
                this.packets.removeLast();
        }
    }
}
