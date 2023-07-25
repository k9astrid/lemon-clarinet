package dev.lemon.client.modules.movement;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.gui.dropdown.ClickScreen;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public class InventoryMove extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Watchdog Dev");

    private boolean inventoryOpen;

    public InventoryMove() {
        super("Inventory Move", Category.MOVEMENT);
    }

    private final KeyBinding[] AFFECTED_BINDINGS = new KeyBinding[] {
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump
    };

    @Subscribe
    private final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat || mc.currentScreen.equals(new ClickScreen()))
            return;

        for (final KeyBinding bind : AFFECTED_BINDINGS) {
            bind.pressed = GameSettings.isKeyDown(bind);
        }
    };

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        if (mode.is("Watchdog Dev")) {
            final Packet<?> packet = e.getPacket();

            if (packet instanceof C03PacketPlayer && inventoryOpen && !(mc.currentScreen instanceof GuiChest)) {
                mc.player.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            } else if (packet instanceof C16PacketClientStatus) {
                final C16PacketClientStatus wrapper = (C16PacketClientStatus) packet;

                if (wrapper.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                    inventoryOpen = true;
                }
            } else if (packet instanceof C0BPacketEntityAction) {
                final C0BPacketEntityAction wrapper = (C0BPacketEntityAction) packet;

                if (wrapper.getAction() == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
                    inventoryOpen = true;
                }
            } else if (packet instanceof C08PacketPlayerBlockPlacement) {
                C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement = ((C08PacketPlayerBlockPlacement) packet);

                if (mc.world.getBlockState(c08PacketPlayerBlockPlacement.getPosition()).getBlock() instanceof BlockChest)
                    inventoryOpen = true;
            } else if (packet instanceof C0DPacketCloseWindow) {
                inventoryOpen = false;
            } else if (packet instanceof C0EPacketClickWindow) {
                inventoryOpen = true;
            }
        }
    };
}
