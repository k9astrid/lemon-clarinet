package dev.lemon.client.modules.combat;

import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.math.Vector2f;
import dev.lemon.api.utils.other.RayCastUtil;
import dev.lemon.api.utils.player.RotationUtil;
import dev.lemon.client.events.motion.PostMotionEvent;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.api.module.Module;
import dev.lemon.api.event.IEventListener;

import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.events.other.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", "Single", "Single", "Multi");
    public final ModeSetting autoblock = new ModeSetting("Auto Block", "None", "None", "Fake", "Vanilla", "NCP", "New NCP");
    public final ModeSetting clickDelay = new ModeSetting("Click Delay", "Normal", "Normal", "1.9+");
    public final NumberSetting range = new NumberSetting("Range", 3, 3, 6, 0.1);
    public final NumberSetting maxCPS = new NumberSetting("Max CPS", 15, 1, 20, 1);
    public final NumberSetting minCPS = new NumberSetting("Min CPS", 10, 1, 20, 1);
    public final NumberSetting rotationSpeed = new NumberSetting("Rotation Speed", 80, 10, 120, 5);
    public final static BooleanSetting kokscraftMoment = new BooleanSetting("KoksCraft Moment", false);
    public final BooleanSetting keepSprint = new BooleanSetting("Keep sprint", false, () -> !kokscraftMoment.isToggled());
    public final BooleanSetting rayCast = new BooleanSetting("Ray Cast", false);
    public final BooleanSetting closestPoint = new BooleanSetting("Closest Point", false);
    public final BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", false);
    public final BooleanSetting player = new BooleanSetting("Players", true);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", false);
    public final BooleanSetting passives = new BooleanSetting("Passives", false);
    public final BooleanSetting neutrals = new BooleanSetting("Neutrals", false);
    public final NumberSetting ticks = new NumberSetting("Ticks Existed", 15, 1, 200, 1);

    private float randomYaw, randomPitch;
    private int hitTicks, expanded;
    private long next;
    public static boolean blocking = false, swing;

    public TimerUtil attack = new TimerUtil();
    public TimerUtil click = new TimerUtil();

    public static List<EntityLivingBase> targets;

    public static Entity target;

    public KillAura() {
        super("Kill Aura", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        blocking = false;
    }

    @Override
    protected void onDisable() {
        blocking = false;
        target = null;
    }

    @Subscribe
    private final IEventListener<TickEvent> onTick = e -> {
        if (minCPS.getVal() > maxCPS.getVal())
            minCPS.setValue(maxCPS.getVal());
    };

    @Subscribe
    private final IEventListener<PacketEvent> onPacket = e -> {
        if (e.getPacket() instanceof C0APacketAnimation)
            swing = true;
        else if (e.getPacket() instanceof C03PacketPlayer)
            swing = false;
    };

    @Subscribe
    private final IEventListener<PostMotionEvent> onPostMotion = e -> {
        if (target != null && this.canBlock())
            this.postBlock();
    };

    private void postBlock() {
        switch (autoblock.getMode()) {
            case "NCP":
            case "New NCP":
                this.block(true, false);
                break;
        }
    }

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion2 = e -> {
        this.setSuffix(mode.getMode());

        if (mc.player.getHealth() <= 0.0 && this.autoDisable.isToggled())
            toggle();

        if (mc.player.ticksExisted % 20 == 0)
            expanded = 0;

        targets = findTargets(range.getVal() + expanded);

        if (autoblock.is("Fake"))
            blocking = target != null && canBlock();

        if (targets.isEmpty()) {
            randomYaw += (float) (Math.random() - 0.5f);
            randomPitch += (float) (Math.random() - 0.5f) * 2;
            target = null;
            return;
        }

        target = targets.get(0);

        if (target == null || mc.player.isDead) {
            randomYaw += (float) (Math.random() - 0.5f);
            randomPitch += (float) (Math.random() - 0.5f) * 2;
            return;
        }

        if (this.canBlock())
            this.preBlock();

        this.rotate();

        this.doAttack(targets);

        if (this.canBlock())
            this.postAttackBlock();
    };

    private void preBlock() {
        switch (autoblock.getMode()) {
            case "NCP":
                this.unblock(false);
                break;

            case "New NCP":
                if (blocking) {
                    mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.player.inventory.currentItem % 8 + 1));
                    mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.player.inventory.currentItem));
                    blocking = false;
                }
                break;
        }
    }

    private void unblock(final boolean swingCheck) {
        if (blocking && (!swingCheck || !swing)) {
            if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            } else {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
            blocking = false;
        }
    }


    private void block(final boolean check, final boolean interact) {
        if (!blocking || !check) {
            if (interact && target != null && mc.objectMouseOver.entityHit == target) {
                mc.playerController.interactWithEntitySendPacket(mc.player, target);
            }

            mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.player.inventoryContainer.getSlot(mc.player.inventory.currentItem + 36).getStack()));
            blocking = true;
        }
    }

    private void postAttackBlock() {
        switch (autoblock.getMode()) {
            case "Vanilla":
                if (this.hitTicks != 0)
                    this.block(false, true);
                break;
        }
    }

    private void doAttack(List<EntityLivingBase> targets) {
        double delay = -1;

        switch (clickDelay.getMode()) {
            case "1.9+":
                double speed = 4;

                if (mc.player.getHeldItem() != null) {
                    final Item item = mc.player.getHeldItem().getItem();

                    if (item instanceof ItemSword) {
                        speed = 1.6;
                    } else if (item instanceof ItemSpade) {
                        speed = 1;
                    } else if (item instanceof ItemPickaxe) {
                        speed = 1.2;
                    } else if (item instanceof ItemAxe) {
                        switch (((ItemAxe) item).getToolMaterial()) {
                            case WOOD:
                            case STONE:
                                speed = 0.8;
                                break;

                            case IRON:
                                speed = 0.9;
                                break;

                            default:
                                speed = 1;
                                break;
                        }
                    } else if (item instanceof ItemHoe) {
                        switch (((ItemHoe) item).theToolMaterial) {
                            case WOOD:
                            case GOLD:
                                speed = 1;
                                break;

                            case STONE:
                                speed = 2;
                                break;

                            case IRON:
                                speed = 3;
                                break;
                        }
                    }
                }

                delay = 1 / speed * 20 - 1;
                break;
        }

        if (attack.hasTimeElapsed(this.next) && target != null && (click.hasTimeElapsed((long) (delay * 50)))) {
            final long clicks = MathHelper.getRandomNumberBetween((int) this.minCPS.getVal(), (int) this.maxCPS.getVal());
            this.next = 1000 / clicks;

            if (Math.sin(next) + 1 > Math.random() || attack.hasTimeElapsed(this.next + 500) || Math.random() > .5) {
                final double range = this.range.getVal();
                final MovingObjectPosition movingObjectPosition = mc.objectMouseOver;

                switch (this.mode.getMode()) {
                    case "Single":
                        if ((mc.player.getDistanceToEntity(target) <= range && !rayCast.isToggled()) ||
                                (rayCast.isToggled() && movingObjectPosition != null && movingObjectPosition.entityHit == target))
                            this.attack(target);
                        else if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
                            this.attack(movingObjectPosition.entityHit);
                        else {
                            switch (clickDelay.getMode()) {
                                case "Normal":
                                    mc.player.sendQueue.addToSendQueue(new C0APacketAnimation());
                                    this.click.reset();
                                    this.hitTicks = 0;
                                    break;
                            }
                        }
                        break;

                    case "Multi":
                        targets.removeIf(target -> mc.player.getDistanceToEntity(target) > range);

                        if (!targets.isEmpty())
                            targets.forEach(this::attack);
                        break;
                }

                this.attack.reset();
            }
        }
    }

    private void attack(final Entity target) {
        if (!this.noSwing.isToggled())
            mc.player.swingItem();
        else mc.player.sendQueue.addToSendQueue(new C0APacketAnimation());

        if (this.keepSprint.isToggled() && !kokscraftMoment.isToggled()) {
            mc.playerController.syncCurrentPlayItem();

            mc.player.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

            if (mc.player.fallDistance > 0 && !mc.player.onGround && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(Potion.blindness) && mc.player.ridingEntity == null) {
                mc.player.onCriticalHit(target);
            }
        } else {
            mc.playerController.attackEntity(mc.player, target);
        }

        this.click.reset();
        this.hitTicks = 0;
    }

    private boolean canBlock() {
        return mc.player.inventoryContainer.getSlot(mc.player.inventory.currentItem + 36).getStack() != null
                && mc.player.inventoryContainer.getSlot(mc.player.inventory.currentItem + 36).getStack().getItem() instanceof ItemSword;
    }

    @Subscribe
    private final IEventListener<PreMotionEvent> onPreMotion = e -> {
        this.hitTicks++;

        if (target == null || mc.player.isDead) {
            target = null;
        }
    };

    public void rotate() {
        final Vector2f targetRotations = RotationUtil.calculateRotationsToEntity(target, closestPoint.isToggled(), range.getVal());

        randomYaw += (float) (Math.random() - 0.5f);
        randomPitch += (float) (Math.random() - 0.5f) * 2;

        targetRotations.x += randomYaw;
        targetRotations.y += randomPitch;

        if (RayCastUtil.rayCast(targetRotations, range.getVal(), 0, mc.player).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
            randomYaw = randomPitch = 0;

        RotationUtil.rotate(targetRotations, rotationSpeed.getVal() + Math.random());
    }

    private List<EntityLivingBase> findTargets(double range) {
        final List<EntityLivingBase> targets = mc.world.loadedEntityList.stream()
                .filter(e -> e instanceof EntityLivingBase)
                .map(e -> ((EntityLivingBase) e))
                .filter(e -> {
                    if (e instanceof EntityPlayer && !player.isToggled())
                        return false;

                    if (e instanceof EntityMob && !hostiles.isToggled())
                        return false;

                    if ((e instanceof EntityAgeable || e instanceof EntityWaterMob) && !passives.isToggled())
                        return false;

                    if (e instanceof EntityAnimal && neutrals.isToggled())
                        return false;

                    if (e.ticksExisted <= ticks.getVal())
                        return false;

                    if (e instanceof EntityArmorStand)
                        return false;

                    if (e instanceof EntityPlayer) {
                        if (e.getName().contains("SHOP") || e.getName().contains("UPGRADES"))
                            return false;
                    }

                    return mc.player != e;
                })
                .filter(e -> mc.player.getDistanceToEntity(e) < range)
                .collect(Collectors.toList());

        return targets;
    }
}
