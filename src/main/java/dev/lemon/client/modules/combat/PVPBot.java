package dev.lemon.client.modules.combat;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.PreUpdateEvent;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import optifine.MathUtils;

import javax.vecmath.Vector2f;

public class PVPBot extends Module {

    public NumberSetting distance = new NumberSetting("Distance", 10, 10, 35, 1);
    public BooleanSetting checkTab = new BooleanSetting("Check Tab", false);

    public PVPBot() {
        super("PVP Bot", Category.COMBAT);
    }

    private Vector2f lockViewRotation;
    private int aimTicks;
    private boolean allowMovement;
    private final TimerUtil timerUtil = new TimerUtil();

    @Override
    protected void onEnable() {
        this.allowMovement = true;
        this.lockViewRotation = null;
        this.aimTicks = 0;
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> {

    };

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        Entity entity = this.getTarget();

        if (entity != null) {
            this.allowMovement = mc.player.getDistanceToEntity(entity) > 2.8; // should W-Tap, edit: yes it works
            this.lockViewRotation = RotationUtil.getRotations(entity);

            mc.player.rotationYaw = (float) (this.lockViewRotation.getX() - (Math.random()) / 1000);
            mc.player.rotationPitch = (float) (this.lockViewRotation.getY() - (Math.random()) / 200);

            if (this.aimTicks++ > 20)
                mc.gameSettings.keyBindForward.pressed = this.allowMovement;

            if (this.aimTicks > 22) {
                int cps = 11;

                if (mc.player.getDistanceToEntity(entity) < 3) {
                    mc.gameSettings.keyBindSprint.pressed = false;

                    double aps = (cps + MathHelper.randFloat(MathHelper.randFloat(2, 4), MathHelper.randFloat(4, 8)));

                    if (this.timerUtil.hasTimeElapsed((long) (1000L / aps))) {
                        this.timerUtil.reset();

                        mc.player.swingItem();
                        mc.playerController.attackEntity(mc.player, entity);
                    }
                } else {
                    mc.gameSettings.keyBindSprint.pressed = true;
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }
            }

            if (mc.player.isCollidedHorizontally && mc.player.onGround)
                mc.player.jump();
            return;
        }

        this.aimTicks = 0;
    };

    private Entity getTarget() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity == null || mc.player.getDistanceToEntity(entity) > this.distance.getVal()
                    || mc.player.isEntityEqual(entity) || (this.checkTab.isToggled()
                    && !this.checkTab(entity)) || !(entity instanceof EntityPlayer)) continue;

            return entity;
        }
        return null;
    }

    private boolean checkTab(Entity entity) {
        return entity instanceof EntityPlayer && GuiPlayerTabOverlay.getPlayerList().contains(entity);
    }
}
