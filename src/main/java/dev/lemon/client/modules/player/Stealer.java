package dev.lemon.client.modules.player;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.RandomUtil;
import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.client.events.other.TickEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;

import java.util.Arrays;
import java.util.Objects;

public class Stealer extends Module {

    public NumberSetting minDelay = new NumberSetting("Min Delay", 200, 0, 5000, 50);
    public NumberSetting maxDelay = new NumberSetting("Max Delay", 500, 0, 5000, 50);
    public BooleanSetting autoClose = new BooleanSetting("Auto Close", true);
    private TimerUtil timer = new TimerUtil();
    public Stealer(){
        super("Stealer", Category.PLAYER);
    }

    @Subscribe
    public final IEventListener<TickEvent> eventListener = e -> {
        if (Objects.isNull(mc.thePlayer)) return;

        if (mc.thePlayer.openContainer == null || !(mc.currentScreen instanceof GuiChest)) return;


        ContainerChest containerChest = (ContainerChest) mc.thePlayer.openContainer;

        for (int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); i++){

            if (timer.hasTimeElapsed((long)RandomUtil.getRandomDoubleInRange(minDelay.getVal(), maxDelay.getVal()))){
                if (containerChest.getLowerChestInventory().getStackInSlot(i) == null)
                    continue;
                mc.playerController.windowClick(containerChest.windowId, i, 0, 1, mc.thePlayer);
                timer.reset();
            }

        }
        if (Arrays.stream(((InventoryBasic)containerChest.getLowerChestInventory()).inventoryContents).allMatch(Objects::isNull) && autoClose.isToggled())
            this.mc.thePlayer.closeScreen();
    };
}
