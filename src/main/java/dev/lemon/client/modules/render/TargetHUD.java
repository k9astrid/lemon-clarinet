package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.other.Animator;
import dev.lemon.api.utils.other.Easing;
import dev.lemon.api.utils.other.StencilUtils;
import dev.lemon.api.utils.render.ColorUtil;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.input.MouseEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.combat.KillAura;
import dev.lemon.client.modules.render.targethud.Particle;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TargetHUD extends Module {
    public ModeSetting modeValue = new ModeSetting("Mode", "Basic", "Basic");
    public NumberSetting posX = new NumberSetting("Pos X",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().width / 2, 1, () -> false);
    public NumberSetting posY = new NumberSetting("Pos Y",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().height / 2, 1, () -> false);
    public BooleanSetting renderParticles = new BooleanSetting("Particles", false);

    private double draggingX, draggingY, width, height;
    private boolean dragging, sentParticles;

    private final Animator animator = new Animator();

    public TargetHUD() {
        super("Target HUD", Category.RENDER);
    }

    public EntityLivingBase target, finalTarget;

    private final List<Particle> particles = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();

    @Subscribe
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        if (Lemon.INSTANCE.getModuleManager().getModuleByName("Kill Aura").isToggled() && KillAura.target != null) {
            this.target = (EntityLivingBase) KillAura.target;
            if (!(this.target instanceof EntityMob || this.target instanceof EntityAnimal || this.target instanceof EntityWaterMob))
                this.finalTarget = this.target;
        } else
            this.target = null;

        if (target == null && KillAura.target == null || !Lemon.INSTANCE.getModuleManager().getModuleByName("Kill Aura").isToggled()) {
            if (mc.currentScreen instanceof GuiChat) {
                target = mc.player;
                if (target != null) {
                    this.finalTarget = this.target;
                }
            }
        }

        if (finalTarget == null && target == null)
            particles.clear();

        if (finalTarget == null)
            return;

        ScaledResolution sr = new ScaledResolution(mc);

        if (this.dragging) {
            if (!(mc.currentScreen instanceof GuiChat)) {
                this.dragging = false;
            } else {
                this.posX.setValue(this.draggingX + ((double) (Mouse.getX() * sr.getScaledWidth()) / mc.displayWidth));
                this.posY.setValue(this.draggingY + (sr.getScaledHeight() - (double) (Mouse.getY() *
                        sr.getScaledHeight()) / mc.displayHeight - 1));
            }
        }

        this.posX.setValue(Math.min(this.posX.getVal(), sr.getScaledWidth() - this.width - 1));
        this.posY.setValue(Math.min(this.posY.getVal(), sr.getScaledHeight() - this.height - 1));
        this.posX.setValue(Math.max(this.posX.getVal(), 1));
        this.posY.setValue(Math.max(this.posY.getVal(), 0.5));

        this.animator.setMin(0).setMax(1).setSpeed(3.3f);
        Easing animationTypeOut = Easing.QUINTIC_OUT;
        Easing animationTypeIn = Easing.QUINTIC_IN;

        if (this.target != null && this.animator.getValue() <= 1F) {
            this.animator.setEase(animationTypeOut).setReversed(false).update();
        } else if (this.target == null && this.animator.getValue() > 0F) {
            this.animator.setEase(animationTypeIn).setReversed(true).update();
        } else if (this.animator.getValue() <= 0F) {
            this.finalTarget = null;
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.posX.getVal(), this.posY.getVal(), 0);

        if (this.animator.getValue() < 1D) {
            GlStateManager.translate((this.width / 2f) * (1 - this.animator.getValue()),
                    (this.height / 2f) * (1 - this.animator.getValue()), (this.width / 2f) * (1 - this.animator.getValue()));
            GlStateManager.scale(this.animator.getValue(), this.animator.getValue(),
                    this.animator.getValue());
        }

        switch (modeValue.getMode()) {
            case "Basic":
                this.width = 145;
                this.height = 48;

                if (!HUD.newStyle.isToggled())
                    RenderUtil.drawGradientRound(0, 0, (float) width, (float) height, 5,
                            Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                            Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                            Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                            Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());
                RenderUtil.drawRound(1F, 1, (float) width - 2, (float) height - 2, 4, new Color(0, 0, 0, 160));

                if (renderParticles.isToggled()) {
                    for (Particle p : particles)
                        if (p.opacity > 1) p.render();
                }

                Fonts.GREYCLIFF_BOLD_18.drawString(finalTarget.getName(), 47, 12, -1);
                Fonts.GREYCLIFF_18.drawString("HP: " + Math.round((finalTarget.getHealth() * 5)) + "%", 47, 23, -1);
                GlStateManager.pushMatrix();
                RenderUtil.drawRound(47, 34.2f, (float) (this.width / 2) + 18, 4, 1.5f, new Color(0, 0, 0, 90));

                RenderUtil.drawGradientRound(47, 35, (float) (this.width / 2) - 69 + ((finalTarget.getHealth() / finalTarget.getMaxHealth()) * 86.8f) + 0.5f, 2.5f, 1.5f,
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());
                RenderUtil.drawRound(47.5f, 35.5f, (float) (this.width / 2) - 69 + ((finalTarget.getHealth() / finalTarget.getMaxHealth()) * 86.8f), 2, 1.2f, new Color(255, 255, 255, 100));
                GlStateManager.popMatrix();


                if (finalTarget instanceof AbstractClientPlayer) {
                    final double offset = finalTarget.hurtTime * .35f;
                    GlStateManager.enableCull();
                    mc.getTextureManager().bindTexture(((AbstractClientPlayer) finalTarget).getLocationSkin());
                    GlStateManager.pushMatrix();
                    StencilUtils.write(false);
                    RenderUtil.drawRound((float) (10 + offset / 2f), (float) (9 + offset / 2f), (float) (31 - offset), (float) (30 - offset), 5, new Color(0, 0, 0, 140));
                    StencilUtils.erase(true);
                    final double hurt = -finalTarget.hurtTime * 23;
                    GL11.glColor4d(255 / 255f, (255 + hurt) / 255f, (255 + hurt) / 255f, 1f);
                    Gui.drawScaledCustomSizeModalRect(9, 6, 8.0F, 8.0F, 8, 8, 34, 34, 64.0F, 66.0F);
                    StencilUtils.dispose();
                    GlStateManager.popMatrix();
                    GL11.glColor4d(255 / 255f, 255 / 255f, 255 / 255f, 1f);
                }

                if (renderParticles.isToggled()) {
                    if (timer.hasTimeElapsed(1000 / 60)) {
                        for (final Particle p : particles) {
                            p.update();

                            if (p.opacity < 1)
                                particles.remove(p);
                        }

                        timer.reset();
                    }

                    if (finalTarget.hurtTime == 9 && !sentParticles) {
                        for (int i = 0; i <= 25; i++) {
                            final Particle p = new Particle();
                            final Color color = ColorUtil.mixColors(
                                    Lemon.INSTANCE.getColorManager().getColor().getFirstColor(),
                                    Lemon.INSTANCE.getColorManager().getColor().getSecondColor(),
                                    (Math.sin(posX.getVal() * .4f + i) + 1) * .5f);

                            p.set(31 / 1.4f, 31 / 1.4f, ((Math.random() - .5) * 2) * 1.2, ((Math.random() - .5) * 2) * 1.2, Math.random() * 4, color);
                            particles.add(p);
                        }

                        sentParticles = true;
                    }

                    if (finalTarget.hurtTime == 8)
                        sentParticles = false;
                }
                break;
        }
        GlStateManager.popMatrix();
    };

    @Subscribe
    private final IEventListener<MouseEvent> onMouse = e -> {
        switch (e.getType()) {
            case CLICK:
                if (e.getMouseButton() == 0) {
                    if (isMouseInBounds(e.getMouseX(), e.getMouseY(),
                            this.posX.getVal(), this.posY.getVal(),
                            this.posX.getVal() + this.width,
                            this.posY.getVal() + this.height)) {
                        this.dragging = true;
                        this.draggingX = this.posX.getVal() - e.getMouseX();
                        this.draggingY = this.posY.getVal() - e.getMouseY();
                    }
                }
                break;
            case RELEASED:
                this.dragging = false;
                break;
        }
    };

    public boolean isMouseInBounds(double mouseX, double mouseY, double x, double y, double x1, double y1) {
        return mouseX >= x && mouseX <= x1 && mouseY >= y && mouseY <= y1;
    }
}
