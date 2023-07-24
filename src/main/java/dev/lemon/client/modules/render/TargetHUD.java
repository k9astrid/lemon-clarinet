package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.other.Animator;
import dev.lemon.api.utils.other.Easing;
import dev.lemon.api.utils.other.StencilUtils;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.input.MouseEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.combat.KillAura;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class TargetHUD extends Module {
    public ModeSetting modeValue = new ModeSetting("Mode", "Tenacity", "Tenacity", "Old Tenacity", "Wave");
    public NumberSetting posX = new NumberSetting("Pos X",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().width / 2, 1, () -> false);
    public NumberSetting posY = new NumberSetting("Pos Y",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().height / 2, 1, () -> false);

    private double draggingX, draggingY, width, height;
    private boolean dragging;

    private final Animator animator = new Animator();

    public TargetHUD() {
        super("Target HUD", Category.RENDER);
    }

    public EntityLivingBase target, finalTarget;

    @Subscribe
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        if (Lemon.INSTANCE.getModuleManager().getModuleByName("Kill Aura").isToggled() && KillAura.target != null) {
            this.target = (EntityLivingBase) KillAura.target;
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

        if(finalTarget == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        if(this.dragging){
            if (!(mc.currentScreen instanceof GuiChat)) {
                this.dragging = false;
            }else{
                this.posX.setValue(this.draggingX + (Mouse.getX() * sr.getScaledWidth() / mc.displayWidth));
                this.posY.setValue(this.draggingY + (sr.getScaledHeight() - Mouse.getY() *
                        sr.getScaledHeight() / mc.displayHeight - 1));
            }
        }

        this.posX.setValue(Math.min(this.posX.getVal(), sr.getScaledWidth() - this.width - 1));
        this.posY.setValue(Math.min(this.posY.getVal(), sr.getScaledHeight() - this.height - 1));
        this.posX.setValue(Math.max(this.posX.getVal(), 1));
        this.posY.setValue(Math.max(this.posY.getVal(), 0.5));

        this.animator.setMin(0).setMax(1).setSpeed(3.3f);
        Easing animationTypeOut = Easing.CUBIC_OUT;
        Easing animationTypeIn = Easing.CUBIC_IN;

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
            case "Tenacity":
                this.width = 143;
                this.height = 47;

                RenderUtil.drawGradientRound(0, 0, (float) width, (float) height, 7,
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                        Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());

                Fonts.GREYCLIFF_BOLD_18.drawCenteredString(finalTarget.getName(), 90, 10, -1);
                Fonts.GREYCLIFF_18.drawCenteredString(Math.round((finalTarget.getHealth() * 5)) + "% - " + Math.round(mc.player.getDistanceToEntity(finalTarget)) + "m", 87 + 3, 32, -1);
                GlStateManager.pushMatrix();
                RenderUtil.drawRound(47, 22, (float) (this.width / 2) + 18, 4, 1.5f, new Color(0,0,0, 90));
                RenderUtil.drawRound(47, 22, (float) (this.width / 2) - 69 + ((finalTarget.getHealth() / finalTarget.getMaxHealth()) * 86.8f), 4, 1.5f, new Color(255,255,255));
                GlStateManager.popMatrix();

                if (((AbstractClientPlayer) finalTarget) != null) {
                    GlStateManager.enableCull();
                    mc.getTextureManager().bindTexture(((AbstractClientPlayer) finalTarget).getLocationSkin());
                    GlStateManager.pushMatrix();
                    StencilUtils.write(false);
                    RenderUtil.drawCircle(25, (32) - 8, 16.0D, 0, 360, -1);
                    StencilUtils.erase(true);
                    Gui.drawScaledCustomSizeModalRect(8, 6, 8.0F, 8.0F, 8, 8, 34, 34, 64.0F, 66.0F);
                    StencilUtils.dispose();
                    GlStateManager.popMatrix();
                }
                break;
        }

        GlStateManager.popMatrix();
    };

    @Subscribe
    private final IEventListener<MouseEvent> onMouse = e -> {
        switch (e.getType()){
            case CLICK:
                if(e.getMouseButton() == 0){
                    if(isMouseInBounds(e.getMouseX(), e.getMouseY(),
                            this.posX.getVal(), this.posY.getVal(),
                            this.posX.getVal() + this.width,
                            this.posY.getVal() + this.height)){
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
