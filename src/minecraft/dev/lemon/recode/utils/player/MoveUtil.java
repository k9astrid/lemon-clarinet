package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.Util;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class MoveUtil implements Util {
    public static final double SPRINTING_MOD = 1.0 / 1.3F;
    public static final double SNEAK_MOD = 0.3F;
    public static final double ICE_MOD = 2.5F;
    public static final double WALK_SPEED = 0.221;
    private static final double SWIM_MOD = 0.115F / WALK_SPEED;
    private static final double[] DEPTH_STRIDER_VALUES = {
            1.0F,
            0.1645F / SWIM_MOD / WALK_SPEED,
            0.1995F / SWIM_MOD / WALK_SPEED,
            1.0F / SWIM_MOD,
    };
    public static final double MIN_DIST = 1.0E-3;

    public static double getSpeed(){
        return Math.abs(Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)) * 20;
    }
    public static double getBaseMoveSpeed(final EntityPlayerSP player) {
        double base = player.isSneaking() ? WALK_SPEED * SNEAK_MOD : canSprint(player) ? WALK_SPEED / SPRINTING_MOD : WALK_SPEED;

        final PotionEffect speed = player.getActivePotionEffect(Potion.moveSpeed);
        final int moveSpeedAmp = speed == null || speed.getDuration() < 3 ? 0 : speed.getAmplifier() + 1;

        if (moveSpeedAmp > 0)
            base *= 1.0 + 0.2 * moveSpeedAmp;

        if (player.isInWater()) {
            base *= SWIM_MOD;
            final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(player);
            if (depthStriderLevel > 0) {
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }

            return base * SWIM_MOD;
        } else if (player.isInLava()) {
            return base * SWIM_MOD;
        } else {
            return base;
        }
    }
    public static boolean isBlockUnder(final Minecraft mc) {
        return mc.theWorld.checkBlockCollision(mc.thePlayer.getEntityBoundingBox().addCoord(0.0, -1.0, 0.0));
    }
    public static boolean isOverVoid(final Minecraft mc) {
        final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox();
        final double height = bb.maxY - bb.minY;

        double offset = height;

        AxisAlignedBB bbPos;

        while (!mc.theWorld.checkBlockCollision((bbPos = bb.addCoord(0, -offset, 0)))) {
            if (bbPos.minY <= 0.0) return true;

            offset += height;
        }

        return false;
    }
    public static boolean canSprint(final EntityPlayerSP player, final boolean omni) {
        return (player.movementInput.moveForward >= 0.8F || (omni && isMoving(player))) &&
                (player.getFoodStats().getFoodLevel() > 6.0F || player.capabilities.allowFlying) &&
                !player.isPotionActive(Potion.blindness) &&
                !player.isCollidedHorizontally &&
                !player.isSneaking();
    }

    public static boolean canSprint(final EntityPlayerSP player) {
        return canSprint(player, true);
    }
    public static boolean isOnGround(final World world,
                                     final EntityPlayerSP player,
                                     final double offset) {
        return world.checkBlockCollision(player.getEntityBoundingBox().addCoord(0, -offset, 0));
    }

    public static boolean isWalking() {
        return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
    }

    public static void jump(double motion) {
        if(!mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.jump();
        }
        mc.thePlayer.motionY = motion;
    }

    private static float getPlayerDirection(){
        boolean movingForward = mc.thePlayer.moveForward > 0.0F;
        boolean movingBackward = mc.thePlayer.moveForward < 0.0F;
        boolean movingRight = mc.thePlayer.moveStrafing > 0.0F;
        boolean movingLeft = mc.thePlayer.moveStrafing < 0.0F;

        boolean isMovingSideways = movingLeft || movingRight;
        boolean isMovingStraight = movingForward || movingBackward;

        double direction = mc.thePlayer.rotationYaw;

        if(movingForward && !isMovingSideways) {

        } else if(movingBackward && !isMovingSideways) {
            direction += 180;
        } else if(movingForward && movingLeft) {
            direction += 45;
        } else if(movingForward) {
            direction -= 45;
        } else if(!isMovingStraight && movingLeft) {
            direction += 90;
        } else if(!isMovingStraight && movingRight) {
            direction -= 90;
        } else if(movingBackward && movingRight) {
            direction -= 135;
        } else if(movingBackward) {
            direction += 135;
        }

        return (float) Math.toRadians(direction);
    }
    public static void setSpeed(double speed) {
        if(isWalking()) {
            mc.thePlayer.motionX = -Math.sin(getPlayerDirection()) * speed;
            mc.thePlayer.motionZ = Math.cos(getPlayerDirection()) * speed;
        } else {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }
    public static boolean isMoving(final EntityPlayerSP player) {
        return player.moveForward != 0.0F || player.moveStrafing != 0.0F;
    }
}
