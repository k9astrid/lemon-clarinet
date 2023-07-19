package dev.lemon.client.modules.movement;

import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.player.MoveUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.other.CollideEvent;
import net.minecraft.util.AxisAlignedBB;

public class Flight extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Creative", "Creative", "Vanilla", "Collide");
    public NumberSetting vanillaSpeed = new NumberSetting("Vanilla Speed", 1, 0, 5, 0.1);

    public Flight() {
        super("Flight", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() { }

    @Override
    protected void onDisable() {
        if (mc.player == null)
            return;

        mc.player.capabilities.isFlying = false;
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
                mc.player.motionY = 0;
                MoveUtil.setSpeed(vanillaSpeed.getVal());
                break;
        }

    };

}
