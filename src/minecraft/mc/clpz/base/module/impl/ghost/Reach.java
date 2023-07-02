package mc.clpz.base.module.impl.ghost;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.awt.*;

import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.impl.input.ClickMouseEvent;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.CombatUtil;
import mc.clpz.base.utils.value.impl.NumberValue;

public class Reach extends Module {
    private NumberValue<Float> range = new NumberValue<>("Range", 3.1F, 3F, 5.0F, 0.01F);

    public Reach() {
        super("Reach", Category.GHOST, new Color(0xA4A29E).getRGB());
        setDescription("Increase your reach");
    }

    @Handler
    public void onUpdate(ClickMouseEvent event) {
        final Object[] objects = CombatUtil.getEntityCustom(getMc().thePlayer.rotationPitch, getMc().thePlayer.rotationYaw, range.getValue(), 0, 0.0F);
        if (objects == null) {
            return;
        }
        getMc().objectMouseOver = new MovingObjectPosition((Entity) objects[0], (Vec3) objects[1]);
        getMc().pointedEntity = (Entity)objects[0];
    }
}