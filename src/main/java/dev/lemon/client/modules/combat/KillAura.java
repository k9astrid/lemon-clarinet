package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.RandomUtil;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.events.other.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

import javax.vecmath.Vector2f;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KillAura extends Module {
    public NumberSetting maxReach = new NumberSetting("Max Reach", 4.0, 1.0, 6.0, 0.1);
    public NumberSetting minReach = new NumberSetting("Min Reach", 3.0, 1.0, 6.0, 0.1);
    public NumberSetting maxCPS = new NumberSetting("Max CPS", 15, 0, 20, 0.5);
    public NumberSetting minCps = new NumberSetting("Min CPS", 10, 0, 20, 0.5);
    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);
    public ModeSetting rotationMode = new ModeSetting("Rotations", "None", "None", "Vanilla", "Randomized");
    public ModeSetting sortingMode = new ModeSetting("Sort", "Health", "Health", "Distance", "Hurt Time");
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    public static ModeSetting autoblockMode = new ModeSetting("Autoblock", "None", "Vanilla", "Fake", "None");

    private final TimerUtil timer = new TimerUtil();
    public static Entity currentTarget;

    public KillAura() {
        super("Kill Aura", Category.COMBAT);
    }

    @Subscribe
    public final IEventListener<TickEvent> onTick = e -> {
        if (minCps.getVal() > maxCPS.getVal())
            minCps.setValue(maxCPS.getVal());

        if (minReach.getVal() > maxReach.getVal())
            minReach.setValue(maxReach.getVal());

        currentTarget = null;
    };

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        List<Entity> entityList = mc.world.loadedEntityList.stream()
                .filter(this::checkEntity)
                .sorted(getSortingMode())
                .collect(Collectors.toList());


        this.setSuffix(minReach.getVal() + "-" + maxReach.getVal());

        for (Entity target : entityList) {
            //fuck y'all do not remove this thing.
            currentTarget = target;

            this.setSuffix(target.getName() + " - " + minReach.getVal()+"-"+ maxReach.getVal());
            Vector2f rotations = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);

            switch (rotationMode.getMode()) {
                case "Vanilla":
                    rotations = RotationUtil.getVanillaRotations(target);
                    break;
                case "Randomized":
                    rotations = RotationUtil.getVanillaRotations(target);
                    rotations.x += Math.random() / 1000;
                    rotations.y -= Math.random() / 200;
                    break;
            }

            if (!rotationMode.is("None"))
                RotationUtil.rotate(rotations, 80 + Math.random());

            switch (autoblockMode.getMode()){
                case "Vanilla":
                    mc.playerController.sendUseItem(mc.player, mc.world, mc.player.getCurrentEquippedItem());
                    break;

            }
            if (timer.hasTimeElapsed((long) (1000L / RandomUtil.getRandomDoubleInRange(minCps.getVal(), maxCPS.getVal())))) {

                if (!(noSwing.isToggled()))
                    mc.player.swingItem();
                else mc.player.sendQueue.addToSendQueueSilent(new C0APacketAnimation());

                if (keepSprint.isToggled())
                    mc.player.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                else
                    mc.playerController.attackEntity(mc.player, target);

                timer.reset();
            }
        }
    };

    private boolean checkEntity(Entity entity){
        return !(mc.player.getEntityId() == entity.getEntityId())
                && !entity.isDead
                && (entity instanceof EntityPlayer || entity instanceof EntityCreature)
                && (invisibles.isToggled() || !entity.isInvisible())
                && entity.getDistanceToEntity(mc.player) <= RandomUtil.getRandomDoubleInRange(minReach.getVal(), maxReach.getVal());
    }

    private Comparator<Entity> getSortingMode(){
        switch (sortingMode.getMode()) {
            default:
            case "Health":
                return Comparator.comparingInt(ent -> (int) ((EntityLivingBase) ent).getHealth());
            case "Distance":
                return Comparator.comparingInt(ent -> (int) ent.getDistanceToEntity(mc.player));
            case "Hurt Time":
                return Comparator.comparingInt(ent -> ((EntityLivingBase) ent).hurtTime);
        }
    }

}
