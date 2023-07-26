package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.render.netgraph.PacketMonitor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.Collection;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;

public class NetGraph extends Module {
    private final PacketMonitor incomingPackets = new PacketMonitor();
    private final PacketMonitor outgoingPackets = new PacketMonitor();

    public NetGraph() {
        super("Net Graph", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        incomingPackets.packets.clear();
        outgoingPackets.packets.clear();
    }

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        if (e.getType() == PacketEvent.Type.RECEIVE)
            incomingPackets.counter++;
        else if (e.getType() == PacketEvent.Type.SENT)
            outgoingPackets.counter++;
    };

    @Subscribe
    private final IEventListener<Render2DEvent> onRender = e -> {
        final int samples = 15;
        final int width = samples * 6;

        this.incomingPackets.update(samples + 1);
        this.outgoingPackets.update(samples + 1);

        drawNetGraph(170, 7, width, 25, samples, this.incomingPackets.getPacketRecord());
        drawNetGraph(170 + width + 30, 7, width, 25, samples, this.outgoingPackets.getPacketRecord());

        glScaled(0.5, 0.5, 1);
        mc.fontRendererObj.drawStringWithShadow("Outgoing packets", (170 + width + 30) * 2, 25 + 43, -1);
        mc.fontRendererObj.drawStringWithShadow("Incoming packets", 170 * 2, 25 + 43, -1);
        glScaled(2.0, 2.0, 1);
    };


    private void drawNetGraph(final int x, final int y, final int width, final int height, final int nSamplesDisplayed, Collection<Integer> packetSeconds) {
        if (!HUD.newStyle.isToggled())
            RenderUtil.drawGradientRound(x - 2, y - 2, width + 4, height + 9, 4,
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                    Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());
        else
            RenderUtil.drawRound(x - 2, y - 2, width + 4, height + 9, 3, new Color(0, 0, 0, 150));
        RenderUtil.drawRound(x, y, width, height, 3, new Color(0, 0, 0, 150));

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glEnable(GL_BLEND);
        glLineWidth(1);
        glTranslated(x + width, y, 0);

        final double sampleWidth = (double) width / nSamplesDisplayed;
        double lineAlignment = 0;

        RenderUtil.setColor(Color.WHITE);

        final int maxPackets = packetSeconds.stream()
                .max(Integer::compareTo)
                .orElse(1);

        final int packetDenominator = (int) (maxPackets * 1.25);

        int totalPackets = 0;

        RenderUtil.setColor(Color.WHITE);

        glBegin(GL_LINE_STRIP);

        {
            for (final int packets : packetSeconds) {
                final double percentage = Math.min(1.0, (double) packets / packetDenominator);
                final double yPos = height * percentage;

                glVertex2d(-lineAlignment, height - yPos);

                lineAlignment += sampleWidth;

                totalPackets += packets;
            }
        }

        glEnd();

        final int avgPackets = totalPackets / nSamplesDisplayed;
        final double avgPercent = Math.min(1.0, (double) avgPackets / packetDenominator);
        final double avgPosition = height * avgPercent;

        RenderUtil.setColor(new Color(0xFF00FF00));
        glBegin(GL_LINES);

        {
            glVertex2d(-width, height - avgPosition);
            glVertex2d(0, height - avgPosition);
        }

        glEnd();

        glEnable(GL_TEXTURE_2D);

        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

        glDisable(GL_BLEND);

        glScaled(.5, .5, 1);
        mc.fontRendererObj.drawStringWithShadow(avgPackets + " avg", -mc.fontRendererObj.getStringWidth(avgPackets + " avg"), height + 30, -1);
        glScaled(2, 2, 1);

        glTranslated(-x - width, -y, 0);

        GlStateManager.color(1, 1, 1, 1);
    }

    public static boolean glEnableBlend() {
        final boolean wasEnabled = glIsEnabled(GL_BLEND);

        if (!wasEnabled) {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        return wasEnabled;
    }
}
