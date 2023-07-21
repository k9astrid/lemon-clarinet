package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.events.render.Render3DEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.vector.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static dev.lemon.api.utils.other.ESPUtil.*;
import static org.lwjgl.opengl.GL11.*;

public class ESP extends Module {
    public BooleanSetting box = new BooleanSetting("Box", true);
    public BooleanSetting nametag = new BooleanSetting("Name tag", true);
    public BooleanSetting heldItem = new BooleanSetting("Held item", true);
    public BooleanSetting healthBar = new BooleanSetting("Health bar", true);
    public BooleanSetting healthText = new BooleanSetting("Health text", false, () -> healthBar.isToggled());
    public ModeSetting healthBarColor = new ModeSetting("Health Color", "Sync", () -> healthBar.isToggled(), "Sync", "Health", "Green", "Gradient");

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
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (Entity entity : positions.keySet()) {
            Vector4f pos = positions.get(entity);

            float x = pos.getX(),
                    y = pos.getY(),
                    right = pos.getZ(),
                    bottom = pos.getW();

            if (box.isToggled()) {

            }
        }
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

