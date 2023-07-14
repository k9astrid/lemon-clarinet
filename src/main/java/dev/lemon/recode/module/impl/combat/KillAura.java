package dev.lemon.recode.module.impl.combat;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import dev.lemon.recode.event.impl.EventPreMotion;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.ModuleInfo;

import dev.lemon.recode.utils.math.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "KillAura", key = Keyboard.KEY_M, category = Category.COMBAT, suffix = "")
public class KillAura extends Module {
    private double reach = 6.0;
    private double cps = 10.0;
    private TimerUtil timer = new TimerUtil();

    private boolean checkEntity(Entity entity){
        return !entity.isDead && (entity instanceof EntityPlayer || entity instanceof EntityCreature) && entity.getDistanceToEntity(mc.thePlayer) <= reach && !(mc.thePlayer.getEntityId() == entity.getEntityId());
    }
    @EventHandler
    public Listener<EventPreMotion> eventPacketListener = e -> {
        List<Entity> entityList = mc.theWorld.loadedEntityList.stream()
                .filter(this::checkEntity)
                .sorted(Comparator.comparingInt(ent -> (int) ((EntityLivingBase) ent).getHealth()))
                .collect(Collectors.toList());

        for (Entity target : entityList){
            if (timer.hasTimeElapsed((long) (1000L / cps))){
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);
                timer.reset();
            }
        }
    };
}
