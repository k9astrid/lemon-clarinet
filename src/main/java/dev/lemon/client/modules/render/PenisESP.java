package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.render.Render3DEvent;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class PenisESP extends Module {

    public PenisESP() {
        super("Penis ESP", Category.RENDER);
    }

    @Subscribe
    private final IEventListener<Render3DEvent> render3DEvent = e -> {
        for (final Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)o;
                final double n = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.timer.renderPartialTicks;
                final double x = n - RenderManager.renderPosX;
                final double n2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.timer.renderPartialTicks;
                final double y = n2 - RenderManager.renderPosY;
                final double n3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.timer.renderPartialTicks;
                final double z = n3 - RenderManager.renderPosZ;
                GL11.glPushMatrix();
                RenderHelper.disableStandardItemLighting();
                this.esp(player, x, y, z);
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    };


    public void esp(final EntityPlayer cummer, final double x, final double y, final double z) {
        GL11.glDisable(GL_LIGHTING);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glLineWidth(1.0f);
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-cummer.rotationYawHead, 0.0f, cummer.height, 0.0f);
        GL11.glTranslated(-x, -y, -z);
        GL11.glTranslated(x, y + cummer.height / 2.0f - 0.22499999403953552, z);
        GL11.glColor4f(1.38f, 0.55f, 2.38f, 1.0f);
        GL11.glTranslated(0.0, 0.0, 0.37500000298023224);
        final Cylinder shaft = new Cylinder();
        shaft.setDrawStyle(100013);
        shaft.draw(0.1f, 0.11f, cummer == mc.player ? 0.9f : 0.3f, 25, 20);
        GL11.glColor4f(1.38f, 0.85f, 1.38f, 1.0f);
        GL11.glTranslated(0.0, 0.0, -0.12500000298023223);
        GL11.glTranslated(-0.09000000074505805, 0.0, 0.0);
        final Sphere right = new Sphere();
        right.setDrawStyle(100013);
        right.draw(0.14f, 10, 20);
        GL11.glTranslated(0.16000000149011612, 0.0, 0.0);
        final Sphere left = new Sphere();
        left.setDrawStyle(100013);
        left.draw(0.14f, 10, 20);
        GL11.glColor4f(1.35f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated(-0.07000000074505806, 0.0, cummer == mc.player ? 0.989999952316284f : 0.389999952316284);
        final Sphere tip = new Sphere();
        tip.setDrawStyle(100013);
        tip.draw(0.13f, 15, 20);
        GL11.glColor4f(1f, 1f, 1f, 1.0f);
        GL11.glTranslated(0, 0, 0.1);
        final Sphere tip2 = new Sphere();
        tip2.setDrawStyle(100013);
        tip2.draw(0.05f, 15, 20);
        GL11.glDepthMask(true);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glDisable(GL_BLEND);
        GL11.glEnable(GL_LIGHTING);
        GL11.glEnable(GL_TEXTURE_2D);
    }
}
