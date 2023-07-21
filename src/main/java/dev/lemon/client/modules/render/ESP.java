package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.other.NametagRenderEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.events.render.Render3DEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static dev.lemon.api.utils.other.ESPUtil.*;
import static org.lwjgl.opengl.GL11.*;

public class ESP extends Module {
    public BooleanSetting box = new BooleanSetting("Box", true);
    public BooleanSetting nametag = new BooleanSetting("Name tag", true);
    public BooleanSetting heldItem = new BooleanSetting("Held item", true);
    public BooleanSetting armorBar = new BooleanSetting("Armor bar", false);
    public BooleanSetting healthBar = new BooleanSetting("Health bar", true);
    public BooleanSetting healthText = new BooleanSetting("Health text", false, () -> healthBar.isToggled());
    public ModeSetting healthBarColor = new ModeSetting("Health Color", "Health", () -> healthBar.isToggled(), "Health", "Green", "Gradient");

    public ESP() {
        super("2D ESP", Category.RENDER);
    }

    private final Map<Entity, Vector4f> positions = new HashMap<>();

    @Subscribe
    private final IEventListener<Render3DEvent> onRender3D = e -> {
        positions.clear();

        for (final Entity entity : mc.world.loadedEntityList) {
            if (shouldRender(entity) && isInView(entity))
                positions.put(entity, getPositions(entity));
        }
    };


    @Subscribe
    private final IEventListener<NametagRenderEvent> onRenderNametag = e -> {
        if (nametag.isToggled())
            e.setCancelled(true);
    };

    @Subscribe
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.bindTexture(0);
        glPushMatrix();
        for (Entity entity : positions.keySet()) {
            Vector4f pos = positions.get(entity);

            float x = pos.getX(),
                    y = pos.getY(),
                    right = pos.getZ(),
                    bottom = pos.getW();

            if (nametag.isToggled()) {
                EntityLivingBase renderingEntity = (EntityLivingBase) entity;
                StringBuilder text = new StringBuilder(StringUtils.stripControlCodes(renderingEntity.getName()));
                double fontScale = .5f;
                float middle = x + ((right - x) / 2);
                float textWidth;
                double fontHeight;

                textWidth = mc.fontRendererObj.getStringWidth(text.toString());
                middle -= (textWidth * fontScale) / 2f;
                fontHeight = mc.fontRendererObj.FONT_HEIGHT * fontScale;

                glPushMatrix();
                glTranslated(middle, y - (fontHeight + 2), 0);
                glScaled(fontScale, fontScale, 1);
                glTranslated(-middle, -(y - (fontHeight + 2)), 0);
                GlStateManager.bindTexture(0);
                GlStateManager.resetColor();
                mc.fontRendererObj.drawStringWithShadow(text.toString(), middle, (float) (y - (fontHeight + 4)), -1);
                glPopMatrix();

            }

            if (heldItem.isToggled()) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (entityLivingBase.getHeldItem() != null) {
                    double fontScale = .5f;
                    float middle = x + ((right - x) / 2);
                    float textWidth;
                    String text = entityLivingBase.getHeldItem().getDisplayName();
                    textWidth = mc.fontRendererObj.getStringWidth(text);
                    middle -= (textWidth * fontScale) / 2f;

                    glPushMatrix();
                    glTranslated(middle, (bottom + 4), 0);
                    glScaled(fontScale, fontScale, 1);
                    glTranslated(-middle, -(bottom + 4), 0);
                    GlStateManager.bindTexture(0);
                    mc.fontRendererObj.drawStringWithShadow(text, middle, bottom + mc.fontRendererObj.FONT_HEIGHT +
                            (armorBar.isToggled() && (entityLivingBase.getTotalArmorValue() / 20f) != 0 ? 1 : -6), -1);
                    GlStateManager.resetColor();
                    glPopMatrix();
                }
            }

            if (healthBar.isToggled()) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                float healthValue = entityLivingBase.getHealth() / entityLivingBase.getMaxHealth();
                Color healthColor = healthValue > .75 ? Color.GREEN :
                                    healthValue > .5 ? new Color(228, 255, 105) :
                                    healthValue > .35 ? new Color(236, 100, 64) :
                                            new Color(255, 65, 68);

                float height = (bottom - y) + 1;
                Gui.drawRect2(x - 3.5f, y - .5f, 2, height + 1, new Color(0, 0, 0, 180).getRGB());

                switch (this.healthBarColor.getMode()) {
                    case "Health":
                        Gui.drawRect2(x - 3f, y + (height - (height * healthValue)), 1, height * healthValue, healthColor.getRGB());
                        break;

                    case "Green":
                        Gui.drawRect2(x - 3f, y + (height - (height * healthValue)), 1, height * healthValue, Color.GREEN.getRGB());
                        break;

                    case "Gradient":
                        RenderUtil.drawGradientTB(x - 3f, y + (height - (height * healthValue)), 1, height * healthValue, 1, Color.GREEN, Color.RED);
                        break;
                }

                if (healthText.isToggled()) {
                    healthValue *= 100;
                    BigDecimal bd = new BigDecimal(healthValue);
                    bd = bd.setScale(1, RoundingMode.HALF_UP);
                    String health = String.valueOf(bd.doubleValue()).substring(0, healthValue == 100 ? 3 : 2);
                    String text = health + "%";
                    double fontScale = .5;
                    float textX = x - 14;
                    float fontHeight = (float) (mc.fontRendererObj.FONT_HEIGHT * fontScale);
                    float newHeight = height - fontHeight;
                    float textY = y + (newHeight - (newHeight * (healthValue / 100)));

                    glPushMatrix();
                    glTranslated(textX - 5, textY, 1);
                    glScaled(fontScale, fontScale, 1);
                    glTranslated(-(textX - 5), -textY, 1);
                    mc.fontRendererObj.drawStringWithShadow(text, textX, textY, -1);
                    glPopMatrix();
                }
            }

            if (armorBar.isToggled()) {
                EntityLivingBase entityLiving = (EntityLivingBase) entity;
                float armorValue = entityLiving.getTotalArmorValue() / 20f;
                if (armorValue != 0) {
                    Gui.drawRect2(x + .5f, bottom + 3f, (right - x), 2, new Color(0, 0, 0, 180).getRGB());
                    Gui.drawRect2(x + 1, bottom + 3.5f, (right - x) * armorValue - 1, 1, new Color(113, 171, 248, 255).getRGB());
                }
            }

            if (box.isToggled()) {
                float thickness = .5f;
                GlStateManager.resetColor();

                RenderUtil.drawGradientLR(x, y, right - x, 1, 1, Lemon.INSTANCE.getColorManager().getColor().getFirstColor(),
                        Lemon.INSTANCE.getColorManager().getColor().getSecondColor());
                RenderUtil.drawGradientLR(x, bottom, right - x, 1, 1, Lemon.INSTANCE.getColorManager().getColor().getSecondColor(),
                        Lemon.INSTANCE.getColorManager().getColor().getFirstColor());

                RenderUtil.drawGradientTB(x, y, 1, bottom - y, 1, Lemon.INSTANCE.getColorManager().getColor().getFirstColor(),
                        Lemon.INSTANCE.getColorManager().getColor().getSecondColor());
                RenderUtil.drawGradientTB(right, y, 1, bottom - y + 1, 1, Lemon.INSTANCE.getColorManager().getColor().getSecondColor(),
                        Lemon.INSTANCE.getColorManager().getColor().getFirstColor());

                Gui.drawRect2(x - thickness, y - thickness, (right - x) + 2, thickness, Color.BLACK.getRGB());
                Gui.drawRect2(x - thickness, y, thickness, (bottom - y) + 1, Color.BLACK.getRGB());
                Gui.drawRect2(x - thickness, (bottom + 1), (right - x) + 2, thickness, Color.BLACK.getRGB());
                Gui.drawRect2(right + 1, y, thickness, (bottom - y) + 1, Color.BLACK.getRGB());

                Gui.drawRect2(x + 1, y + 1, (right - x) - 1, thickness, Color.BLACK.getRGB());
                Gui.drawRect2(x + 1, y + 1, thickness, (bottom - y) - 1, Color.BLACK.getRGB());
                Gui.drawRect2(x + 1, (bottom - thickness), (right - x) - 1, thickness, Color.BLACK.getRGB());
                Gui.drawRect2(right - thickness, y + 1, thickness, (bottom - y) - 1, Color.BLACK.getRGB());
            }
        }
        glPopMatrix();
    };

    private boolean shouldRender(Entity e) {
        if (e.isDead || e.isInvisible())
            return false;

        if (e instanceof EntityPlayer) {
            if (e == mc.player)
                return mc.gameSettings.thirdPersonView != 0;

            return true;
        }

        return false;
    }
}

