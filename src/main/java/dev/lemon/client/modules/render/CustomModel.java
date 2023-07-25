package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomModel extends Module {

    public static final ResourceLocation amongusModel = new ResourceLocation("lemon/images/models/amogus.png");
    public static final ResourceLocation rabbitModel = new ResourceLocation("lemon/images/models/rabbit.png");
    public static final ResourceLocation pandaModel = new ResourceLocation("lemon/images/models/panda.png");

    public static ModeSetting mode = new ModeSetting("Mode", "Among Us", "Among Us", "Panda", "Rabbit");
    public static ModeSetting amongusMode = new ModeSetting("Among Us Color", "Sync", () -> mode.is("Among Us"), "Random", "Sync");
    public static BooleanSetting onlyMe = new BooleanSetting("Only Me", false);

    public CustomModel() {
        super("Custom Model", Category.RENDER);
    }

    private static final Map<Object, Color> entityColorMap = new HashMap<>();

    @Override
    protected void onEnable() {
        entityColorMap.clear();
    }

    public static Color getRandomColor() {
        return new Color(Color.HSBtoRGB((float) Math.random(), (float) (.5 + Math.random() / 2), (float) (.5 + Math.random() / 2)));
    }

    public static Color getColor(Entity entity) {
        Color color;

        if (entityColorMap.containsKey(entity)) {
            color = entityColorMap.get(entity);
        } else {
            color = getRandomColor();
            entityColorMap.put(entity, color);
        }

        return color;
    }
}
