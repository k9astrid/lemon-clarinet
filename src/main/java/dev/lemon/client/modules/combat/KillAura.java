package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.RandomUtil;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.IMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

import javax.vecmath.Vector2f;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {
    public NumberSetting reach = new NumberSetting("Reach", 3.0, 1.0, 6.0, 0.1);
    public NumberSetting minCps = new NumberSetting("Min CPS", 10, 0, 20, 0.5);
    public NumberSetting maxCPS = new NumberSetting("Max CPS", 10, 0, 20, 0.5);
    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);
    public ModeSetting rotationMode = new ModeSetting("Rotations", "None", "None", "Vanilla", "Randomized", "Smooth");
    public ModeSetting sortingMode = new ModeSetting("Sort", "Health", "Health", "Distance", "Hurt Time");
    public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting creatures = new BooleanSetting("Creatures", true);
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

    private final TimerUtil timer = new TimerUtil();

    public KillAura() {
        super("Kill Aura", Category.COMBAT);
    }

    @Subscribe
    public final IEventListener<PreMotionEvent> eventPreMotionListener = e -> {
        List<Entity> entityList = mc.world.loadedEntityList.stream()
                .filter(this::checkEntity)
                .sorted(getSortingMode())
                .collect(Collectors.toList());

        for (Entity target : entityList) {
            float[] rotations;
            switch (rotationMode.getMode()) {
                case "Smooth":
                    RotationUtil.rotate(RotationUtil.rotations, 360);
                    break;
                case "Vanilla":
                    rotations = RotationUtil.getVanillaRotations(target);
                    mc.player.rotationYawHead = rotations[0];
                    break;
                case "Randomized":
                    rotations = RotationUtil.getVanillaRotations(target);
                    mc.player.rotationYawHead = (float) (rotations[0] - Math.random());
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
        return !entity.isDead
                && (entity instanceof EntityPlayer == players.isToggled() || entity instanceof EntityCreature == creatures.isToggled())
                && (invisibles.isToggled() || !entity.isInvisible())
                && entity.getDistanceToEntity(IMethods.mc.player) <= reach.getVal()
                && !(IMethods.mc.player.getEntityId() == entity.getEntityId());
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
