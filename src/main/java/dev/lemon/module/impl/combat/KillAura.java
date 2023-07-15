package dev.lemon.module.impl.combat;

import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventPreMotion;
import dev.lemon.module.Module;
import dev.lemon.event.IEventListener;

import dev.lemon.utils.math.TimerUtil;
import dev.lemon.utils.IMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Module.Info(name = "Kill Aura", category = Module.Category.COMBAT)
public class KillAura extends Module {
    private double reach = 6.0;
    private double cps = 10.0;
    private TimerUtil timer = new TimerUtil();

    private boolean checkEntity(Entity entity){
        return !entity.isDead && (entity instanceof EntityPlayer || entity instanceof EntityCreature) && entity.getDistanceToEntity(IMethods.mc.thePlayer) <= reach && !(IMethods.mc.thePlayer.getEntityId() == entity.getEntityId());
    }

    @Subscribe
    public final IEventListener<EventPreMotion> onPacket = e -> {
        List<Entity> entityList = IMethods.mc.theWorld.loadedEntityList.stream()
                .filter(this::checkEntity)
                .sorted(Comparator.comparingInt(ent -> (int) ((EntityLivingBase) ent).getHealth()))
                .collect(Collectors.toList());

        for (Entity target : entityList){
            if (timer.hasTimeElapsed((long) (1000L / cps))){
                IMethods.mc.thePlayer.swingItem();
                IMethods.mc.playerController.attackEntity(IMethods.mc.thePlayer, target);
                timer.reset();
            }
        }
    };
}
