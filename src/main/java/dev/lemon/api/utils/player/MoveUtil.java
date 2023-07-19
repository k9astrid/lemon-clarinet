package dev.lemon.api.utils.player;

import dev.lemon.api.utils.IMethods;
import lombok.experimental.UtilityClass;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;

@UtilityClass
public class MoveUtil implements IMethods {

    public static final double WALK_SPEED = .221;
    public static final double WEB_SPEED = .105 / WALK_SPEED;
    public static final double SWIM_SPEED = .115f / WALK_SPEED;
    public static final double SNEAK_SPEED = .3f;
    public static final double SPRINTING_SPEED = 1.3f;

    public static final double[] DEPTH_STRIDER = {
            1.f,
            .1645f / SWIM_SPEED / WALK_SPEED,
            .1995f / SWIM_SPEED / WALK_SPEED,
            1.f / SWIM_SPEED
    };

    public boolean moving() {
        return mc.player.moveForward != 0 || mc.player.moveStrafing != 0;
    }

    public void jump(double motion) {
        if (!mc.gameSettings.keyBindJump.isKeyDown())
            mc.player.jump();
        mc.player.motionY = motion;
    }

    public double direction() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0)
            rotationYaw += 180;

        float forward = 1;

        if (mc.player.moveForward < 0)
            forward = -.5f;
        else if (mc.player.moveForward > 0)
            forward = .5f;

        if (mc.player.moveStrafing > 0)
            rotationYaw -= 70 * forward;

        if (mc.player.moveStrafing < 0)
            rotationYaw += 70 * forward;

        return Math.toRadians(rotationYaw);
    }

    public double direction(float yaw, final double forward, final double strafing) {
        if (forward < 0)
            yaw += 180f;

        float moveForward = 1f;

        if (forward < 0)
            moveForward = -.5f;
        else if (forward > 0)
            moveForward = .5f;

        if (strafing > 0)
            yaw -= 90 * moveForward;

        if (strafing < 0)
            yaw += 90 * moveForward;

        return Math.toRadians(yaw);
    }

    public void setSpeed(double speed) {
        if (moving()) {
            mc.player.motionX = -Math.sin(direction()) * speed;
            mc.player.motionZ = Math.cos(direction()) * speed;
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    public void strafe(double speed) {
        setSpeed(speed);
    }

    public void strafe(){
        strafe(baseSpeed());
    }

    public boolean isInLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }

    public boolean canSprint() {
        return mc.player.moveForward >= .8f && !mc.player.isCollidedHorizontally &&
                (mc.player.getFoodStats().getFoodLevel() > 6 || mc.player.capabilities.allowFlying) &&
                !mc.player.isPotionActive(Potion.blindness) &&
                !mc.player.isUsingItem() &&
                !mc.player.isSneaking();
    }

    public boolean enoughMovementForSprinting() {
        return Math.abs(mc.player.moveForward) >= .8f || Math.abs(mc.player.moveStrafing) >= .8f;
    }

    public double speed() {
        return Math.hypot(mc.player.motionX, mc.player.motionZ);
    }

    public double baseSpeed() {
        double speed;
        boolean useModifiers = false;

        if (mc.player.isInWeb)
            speed = WEB_SPEED * WALK_SPEED;
        else if (MoveUtil.isInLiquid()) {
            speed = SWIM_SPEED * WALK_SPEED;

            final int level = EnchantmentHelper.getDepthStriderModifier(mc.player);

            if (level > 0) {
                speed *= DEPTH_STRIDER[level];
                useModifiers = true;
            }
        } else if (mc.player.isSneaking()) {
            speed = SNEAK_SPEED * WALK_SPEED;
        } else {
            speed = WALK_SPEED;
            useModifiers = true;
        }

        if (useModifiers) {
            if (enoughMovementForSprinting())
                speed *= SPRINTING_SPEED;

            if (mc.player.isPotionActive(Potion.moveSpeed))
                speed *= 1 + (.2 * (mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1));

            if (mc.player.isPotionActive(Potion.moveSlowdown))
                speed = .29;
        }

        return speed;
    }
}
