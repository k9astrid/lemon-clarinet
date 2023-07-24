package dev.lemon.api.utils.player;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.math.Vector2f;
import dev.lemon.api.utils.math.Vector3d;
import dev.lemon.api.utils.other.RayCastUtil;
import dev.lemon.client.events.input.MoveInputEvent;
import dev.lemon.client.events.motion.JumpEvent;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.PreUpdateEvent;
import dev.lemon.client.events.motion.StrafeEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.misc.MovementCorrection;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RotationUtil implements IMethods {

    public static Vector2f rotations, last, target;
    private static double speed;
    private static boolean active, haveSmoothed;

    public static void rotate(final Vector2f rotations, final double speed) {
        target = rotations;
        RotationUtil.speed = speed;
        active = true;

        smoothRotations();
    }

    @Subscribe
    public final IEventListener<PreUpdateEvent> onPreUpdate = e -> {
        if (!active || rotations == null || last == null || target == null)
            rotations = last = target = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);

        if (active)
            smoothRotations();
    };

    @Subscribe
    public final IEventListener<PreMotionEvent> onPreMotion = e -> {
        if (active && rotations != null) {
            e.setYaw(rotations.x);
            e.setPitch(rotations.y);

            mc.player.renderYawOffset = rotations.x;
            mc.player.rotationYawHead = rotations.x;
            mc.player.rotationPitchHead = rotations.y;

            if (Math.abs((rotations.x - mc.player.rotationYaw) % 360) < 1 &&
                Math.abs((rotations.y - mc.player.rotationPitch))     < 1) {
                active = false;

                final Vector2f rotations = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
                final Vector2f fixed = reset(applySensitivity(rotations, last));

                mc.player.rotationYaw = fixed.x;
                mc.player.rotationPitch = fixed.y;
            }

            last = rotations;
        } else last = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);

        target = new Vector2f(mc.player.rotationYaw, mc.player.rotationPitch);
        haveSmoothed = false;
    };

    /////////////////////////////////////////////Movement Fix/////////////////////////////////////////////////////

    @Subscribe
    private final IEventListener<StrafeEvent> onStrafe = e -> {
        if (active && Lemon.INSTANCE.getModuleManager().getModuleByName("Movement Correction").isToggled() && rotations != null) {
            e.setYaw(rotations.x);
        }
    };

    @Subscribe
    private final IEventListener<JumpEvent> onJump = e -> {
        if (active && Lemon.INSTANCE.getModuleManager().getModuleByName("Movement Correction").isToggled() && rotations != null) {
            e.setYaw(rotations.x);
        }
    };

    @Subscribe
    private final IEventListener<MoveInputEvent> onMoveInput = e -> {
        if (active && Lemon.INSTANCE.getModuleManager().getModuleByName("Movement Correction").isToggled()
                && MovementCorrection.mode.is("Silent") && rotations != null) {

            final float yaw = rotations.x;

            final float forward = e.getForward();
            final float strafe = e.getStrafe();

            final double angle = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction(mc.player.rotationYaw, forward, strafe)));

            if (forward == 0 && strafe == 0)
                return;

            float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

            for (float predictedForward = -1f; predictedForward <= 1f; predictedForward += 1f) {
                for (float predictedStrafe = -1f; predictedStrafe <= 1f; predictedStrafe += 1f) {
                    if (predictedStrafe == 0 && predictedForward == 0)
                        continue;

                    final double predictedAngle = MathHelper.wrapAngleTo180_double(Math.toDegrees(MoveUtil.direction(yaw, predictedForward, predictedStrafe)));
                    final double difference = Math.abs(angle - predictedAngle);

                    if (difference < closestDifference) {
                        closestDifference = (float) difference;
                        closestForward = predictedForward;
                        closestStrafe = predictedStrafe;
                    }
                }
            }

            e.setForward(closestForward);
            e.setStrafe(closestStrafe);
        }
    };

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void smoothRotations() {
        if (!haveSmoothed)
            rotations = fixRotations(
                    new Vector2f(last.x, last.y),
                    new Vector2f(target.x, target.y),
                    speed
            );

        haveSmoothed = true;
    }

    public static Vector2f reset(final Vector2f rot) {
        if (rot == null)
            return null;

        return new Vector2f(
                rot.x + MathHelper.wrapAngleTo180_float(mc.player.rotationYaw - rotations.x),
                mc.player.rotationPitch
        );
    }

    public static Vector2f applySensitivity(final Vector2f rot) {
        final Vector2f previous = new Vector2f(mc.player.prevRotationYaw, mc.player.prevRotationPitch);
        final float sens = (float) (mc.gameSettings.mouseSensitivity * (1 + Math.random() / 10000000) * .6f + .2f);
        final double multiplier = sens * sens * sens * 8 * .15;

        final float yaw = previous.x + (float) (Math.round((rot.x - previous.x) / multiplier) * multiplier);
        final float pitch = previous.y + (float) (Math.round((rot.y - previous.y) / multiplier) * multiplier);

        return new Vector2f(yaw, MathHelper.clamp_float(pitch, -90, 90));
    }

    public static Vector2f applySensitivity(final Vector2f rot, final Vector2f last) {
        final float sens = (float) (mc.gameSettings.mouseSensitivity * (1 + Math.random() / 10000000) * .6f + .2f);
        final double multiplier = sens * sens * sens * 8 * .15;

        final float yaw = last.x + (float) (Math.round((rot.x - last.x) / multiplier) * multiplier);
        final float pitch = last.y + (float) (Math.round((rot.y - last.y) / multiplier) * multiplier);

        return new Vector2f(yaw, MathHelper.clamp_float(pitch, -90, 90));
    }

    public static Vector2f fixRotations(final Vector2f lastRotations, final Vector2f targetRotations, final double speed) {
        float targetYaw = targetRotations.x;
        float targetPitch = targetRotations.y;
        final float lastYaw = lastRotations.x;
        final float lastPitch = lastRotations.y;

        final double deltaYaw = MathHelper.wrapAngleTo180_float(targetYaw - lastYaw);
        final double deltaPitch = targetPitch - lastPitch;

        final double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
        final double distYaw = Math.abs(deltaYaw / distance);
        final double distPitch = Math.abs(deltaPitch / distance);

        final double maxYaw = speed * distYaw;
        final double maxPitch = speed * distPitch;

        final float moveYaw = (float) Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
        final float movePitch = (float) Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);

        targetYaw = lastYaw + moveYaw;
        targetPitch = lastPitch + movePitch;

        for (int i = 1; i <= (int) (Minecraft.getDebugFPS() / 20 + Math.random() * 10); i++) {
            if (Math.abs(moveYaw) + Math.abs(movePitch) > 1) {
                targetYaw += (Math.random() - .5) / 1000;
                targetPitch -= Math.random() / 200;
            }

            final Vector2f rotations = new Vector2f(targetYaw, targetPitch);
            final Vector2f fixed = applySensitivity(rotations);

            targetYaw = fixed.x;
            targetPitch = Math.max(-90, Math.min(90, fixed.y));
        }

        return new Vector2f(targetYaw, targetPitch);
    }

    public static Vector2f getRotations(Vec3 origin, Vec3 position) {
        Vec3 org = new Vec3(origin.xCoord, origin.yCoord, origin.zCoord);
        Vec3 difference = position.subtract(org);
        double distance = difference.flat().lengthVector();
        float yaw = ((float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));

        return new Vector2f(yaw, pitch);
    }

    public static Vector2f getRotations(Entity entity) {
        return getRotations(mc.player.getPositionVector().addVector(0.0D,
                mc.player.getEyeHeight(), 0.0D), entity.getPositionVector().addVector(0.0D, entity.getEyeHeight() / 2, 0.0D));
    }

    public static Vector2f calculateRotationsFromTo(final Vector3d from, final Vector3d to) {
        final Vector3d diff = to.subtract(from);

        final double distance = Math.hypot(diff.getX(), diff.getZ());

        final float yaw = (float) (MathHelper.atan2(diff.getZ(), diff.getX()) * 180 / Math.PI) - 90.0F;
        final float pitch = (float) (-(MathHelper.atan2(diff.getY(), distance) * 180 / Math.PI));

        return new Vector2f(yaw, pitch);
    }

    public static Vector2f calculateNormalRotationsToEntity(final Entity e) {
        return calculateRotationsTo(e.getCustomPositionVector().add(0, Math.max(0, Math.min(
                mc.player.posY - e.posY + mc.player.getEyeHeight(), (e.getEntityBoundingBox().maxY - e.getEntityBoundingBox().minY)
                * .9)), 0));
    }

    public static Vector2f calculateRotationsTo(final Vector3d to) {
        return calculateRotationsFromTo(mc.player.getCustomPositionVector().add(0, mc.player.getEyeHeight(), 0), to);
    }

    public static Vector2f calculateRotationsToEntity(final Entity e, final boolean closestPoint, final double range) {
        Vector2f normal = calculateNormalRotationsToEntity(e);

        if (!closestPoint || RayCastUtil.rayCast(normal, range, 0, mc.player).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return normal;
        }

        for (double y = 1; y >= 0; y -= .25) {
            for (double x = 1; x >= -.5; x -= .5) {
                for (double z = 1; z >= -.5; z -= .5) {
                    Vector2f closestRotations = calculateRotationsTo(e.getCustomPositionVector().add(
                            (e.getEntityBoundingBox().maxX - e.getEntityBoundingBox().minX) * x,
                            (e.getEntityBoundingBox().maxY - e.getEntityBoundingBox().minY) * y,
                            (e.getEntityBoundingBox().maxZ - e.getEntityBoundingBox().minZ) * z
                    ));

                    if (RayCastUtil.rayCast(closestRotations, range, 0, mc.player) == null) {
                        ChatUtil.addMessage("normal");
                        return normal;
                    }

                    if (RayCastUtil.rayCast(closestRotations, range, 0, mc.player).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
                        return closestRotations;
                }
            }
        }

        return normal;
    }
}
