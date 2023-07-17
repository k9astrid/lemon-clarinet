package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.RandomUtil;
import dev.lemon.client.events.EventPreMotion;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.IMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {
    public NumberSetting reach = new NumberSetting("Reach", 3.0, 1.0, 6.0, 0.1);
    public NumberSetting minCps = new NumberSetting("Min CPS", 10, 0, 20, 0.5);
    public NumberSetting maxCPS = new NumberSetting("Max CPS", 10, 0, 20, 0.5);

    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);
   public ModeSetting rotationMode = new ModeSetting("Rotations", "None", "None");
    public ModeSetting sortingMode = new ModeSetting("Sort", "Health", "Health", "Distance");


    private TimerUtil timer = new TimerUtil();

    public KillAura() {
        super("Kill Aura", Category.COMBAT);
    }

    private boolean checkEntity(Entity entity){
        return !entity.isDead && (entity instanceof EntityPlayer || entity instanceof EntityCreature) && entity.getDistanceToEntity(IMethods.mc.thePlayer) <= reach.getVal() && !(IMethods.mc.thePlayer.getEntityId() == entity.getEntityId());
    }

    private Comparator<Entity> getSortingMode(){
        switch (sortingMode.getMode()) {
            default:
            case "Health":
                return Comparator.comparingInt(ent -> (int) ((EntityLivingBase) ent).getHealth());
            case "Distance":
                return Comparator.comparingInt(ent -> (int) ent.getDistanceToEntity(mc.thePlayer));
        }
    }

    @Subscribe
    public final IEventListener<EventPreMotion> onPacket = e -> {
        List<Entity> entityList = IMethods.mc.theWorld.loadedEntityList.stream()
                .filter(this::checkEntity)
                .sorted(getSortingMode())
                .collect(Collectors.toList());

        for (Entity target : entityList){
            if (timer.hasTimeElapsed((long) (1000L / RandomUtil.getRandomDoubleInRange(minCps.getVal(), maxCPS.getVal())))){
                if (!(noSwing.isToggled()))
                    IMethods.mc.thePlayer.swingItem();

                if (keepSprint.isToggled()){
                    IMethods.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                } else {
                    IMethods.mc.playerController.attackEntity(IMethods.mc.thePlayer, target);
                }
                timer.reset();
            }
        }
    };
}
