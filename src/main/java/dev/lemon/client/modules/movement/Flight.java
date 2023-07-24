package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.notification.NotificationType;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.other.CollideEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.render.HUD;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Flight extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Creative", "Creative", "Vanilla", "Collide", "Vulcan", "Negativity", "Updated NCP");
    public ModeSetting vulcanMode = new ModeSetting("Vulcan Mode", "Glide", () -> mode.is("Vulcan"), "Glide", "");

    public NumberSetting vanillaSpeed = new NumberSetting("Vanilla Speed", 1, 0, 5, 0.1,() -> mode.is("Vanilla"));


    public Flight() {
        super("Flight", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        switch (mode.getMode()){
            case "Updated NCP":
                if (hitHead()){
                    mc.player.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.player.posX, mc.player.posY - 0.0654D, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
                    ChatUtil.addMessage("Clipped");
                } else {
                    ChatUtil.addMessage("You need to be under a block.");
                }
                break;
        }
    }


    private boolean hitHead(){
        Block blockAboveHead = mc.world.getBlockState(mc.player.getPosition().add(0, mc.player.getEyeHeight()+1, 0)).getBlock();
        return !(blockAboveHead instanceof BlockAir);
    }
    @Override
    protected void onDisable() {

        if (mc.player == null)
            return;

        mc.player.capabilities.isFlying = false;

        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;
    }

    @Subscribe
    public final IEventListener<CollideEvent> onCollide = e -> {
        switch (mode.getMode()){
            case "Collide":
                if (e.getPos().getY() < mc.player.posY)
                    e.setBoundingBox(new AxisAlignedBB(-15, 1, -15, 15, 1, 15).offset(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()));
                break;
        }
    };

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.setSuffix(mode.getMode());

        switch (mode.getMode()){
            case "Creative":
                mc.player.capabilities.isFlying = true;
                break;

            case "Vanilla":
                mc.player.motionX = mc.player.motionZ = 0;
                mc.player.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? vanillaSpeed.getVal() : mc.gameSettings.keyBindSneak.isKeyDown() ? -vanillaSpeed.getVal() : 0;

                MoveUtil.strafe(vanillaSpeed.getVal());
                break;
            case "Vulcan":
                switch (vulcanMode.getMode()) {
                    case "Glide":
                        if (mc.player.ticksExisted % 10 == 5) {
                            mc.player.motionY = -0.1;
                        }

                        if (mc.player.ticksExisted % 10 == 0) {
                            mc.player.motionY = -0.1;
                        }
                        break;
                }
                break;
            case "Updated NCP":
                if (!hitHead()){
                    mc.player.motionY += 0.12;
                    MoveUtil.setSpeed(0.35);
                }
                break;
        }

    };

}
