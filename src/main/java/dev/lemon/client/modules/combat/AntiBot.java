package dev.lemon.client.modules.combat;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class AntiBot extends Module {
    public BooleanSetting advanced = new BooleanSetting("Advanced Check", false);
    public BooleanSetting watchdog = new BooleanSetting("Watchdog Check", false);
    public BooleanSetting funcraft = new BooleanSetting("Funcraft Check", false);
    public BooleanSetting middleClick = new BooleanSetting("Middle Click Check", false);

    public AntiBot() {
        super("Anti Bot", Category.COMBAT);
    }

    private boolean down;

    @Override
    protected void onDisable() {
        Lemon.INSTANCE.getBotManager().clear();
    }

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        if (funcraft.isToggled()) {
            mc.world.playerEntities.forEach(player -> {
                if (player.getDisplayName().getUnformattedText().contains("§")) {
                    Lemon.INSTANCE.getBotManager().remove(player);
                    return;
                }

                Lemon.INSTANCE.getBotManager().add(player);
            });
        }
    };

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        if (advanced.isToggled()) {
            mc.world.playerEntities.forEach(player -> {
                if (mc.player.getDistanceSq(player.posX, mc.player.posY, player.posZ) > 200)
                    Lemon.INSTANCE.getBotManager().remove(player);

                if (player.ticksExisted < 5 || player.isInvisible() || mc.player.getDistanceSq(player.posX, mc.player.posY, player.posZ) > 100 * 100)
                    Lemon.INSTANCE.getBotManager().add(player);
            });
        }

        if (watchdog.isToggled()) {
            mc.world.playerEntities.forEach(player -> {
                final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());

                if (info == null)
                    Lemon.INSTANCE.getBotManager().add(player);
                else
                    Lemon.INSTANCE.getBotManager().remove(player);
            });
        }

        if (middleClick.isToggled()) {
            if (Mouse.isButtonDown(2)) {
                if (down)
                    return;

                down = true;

                if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    if (Lemon.INSTANCE.getBotManager().contains(mc.objectMouseOver.entityHit)) {
                        Lemon.INSTANCE.getBotManager().remove(mc.objectMouseOver.entityHit);
                    } else Lemon.INSTANCE.getBotManager().add(mc.objectMouseOver.entityHit);
                }
            } else down = false;
        }
    };
}
