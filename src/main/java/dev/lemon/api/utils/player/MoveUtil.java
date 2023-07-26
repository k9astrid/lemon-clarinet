package dev.lemon.api.utils.player;

import dev.lemon.api.utils.IMethods;
import dev.lemon.client.modules.world.Scaffold;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public static float speedBoost(float times) {
        float boost = (float) ((MoveUtil.baseSpeed() - 0.2875F) * times);

        if(0 > boost)
            boost = 0;

        return boost;
    }

    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.world.getBlockState(new BlockPos(mc.player.posX + offsetX, mc.player.posY + offsetY, mc.player.posZ + offsetZ)).getBlock();
    }

    public static Block block(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
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

    public Vec3 getPlacePossibility(double offsetX, double offsetY, double offsetZ) {
        final List<Vec3> possibilities = new ArrayList<>();
        final int range = (int) (5 + (Math.abs(offsetX) + Math.abs(offsetZ)));

        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = blockRelativeToPlayer(x, y, z);

                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2)
                            possibilities.add(new Vec3(mc.player.posX + x + x2, mc.player.posY + y, mc.player.posZ + z));

                        for (int y2 = -1; y2 <= 1; y2 += 2)
                            possibilities.add(new Vec3(mc.player.posX + x, mc.player.posY + y + y2, mc.player.posZ + z));

                        for (int z2 = -1; z2 <= 1; z2 += 2)
                            possibilities.add(new Vec3(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z + z2));
                    }
                }
            }
        }

        possibilities.removeIf(vec3 -> mc.player.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord) > 5 || !(block(vec3.xCoord, vec3.yCoord, vec3.zCoord) instanceof BlockAir));

        if (possibilities.isEmpty()) return null;

        possibilities.sort(Comparator.comparingDouble(vec3 -> {
            final double d0 = (mc.player.posX + offsetX) - vec3.xCoord;
            final double d1 = (mc.player.posY - 1 + offsetY) - vec3.yCoord;
            final double d2 = (mc.player.posZ + offsetZ) - vec3.zCoord;

            return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
        }));

        return possibilities.get(0);
    }

    public Vec3 getPlacePossibility() {
        return getPlacePossibility(0, 0, 0);
    }

    public Scaffold.EnumFacingOffset getEnumFacing(final Vec3 position) {
        for (int x2 = -1; x2 <= 1; x2 += 2) {
            if (!(block(position.xCoord + x2, position.yCoord, position.zCoord) instanceof BlockAir)) {
                if (x2 > 0) {
                    return new Scaffold.EnumFacingOffset(EnumFacing.WEST, new Vec3(x2, 0, 0));
                } else {
                    return new Scaffold.EnumFacingOffset(EnumFacing.EAST, new Vec3(x2, 0, 0));
                }
            }
        }

        for (int y2 = -1; y2 <= 1; y2 += 2) {
            if (!(block(position.xCoord, position.yCoord + y2, position.zCoord) instanceof BlockAir)) {
                if (y2 < 0) {
                    return new Scaffold.EnumFacingOffset(EnumFacing.UP, new Vec3(0, y2, 0));
                }
            }
        }

        for (int z2 = -1; z2 <= 1; z2 += 2) {
            if (!(block(position.xCoord, position.yCoord, position.zCoord + z2) instanceof BlockAir)) {
                if (z2 < 0) {
                    return new Scaffold.EnumFacingOffset(EnumFacing.SOUTH, new Vec3(0, 0, z2));
                } else {
                    return new Scaffold.EnumFacingOffset(EnumFacing.NORTH, new Vec3(0, 0, z2));
                }
            }
        }

        return null;
    }
}
