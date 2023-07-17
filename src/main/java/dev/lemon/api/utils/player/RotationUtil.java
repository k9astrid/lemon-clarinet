package dev.lemon.api.utils.player;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.events.motion.PreMotionEvent;
import dev.lemon.client.events.motion.PreUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector2f;

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
            //TODO: for luca: add "rotationPitchHead"

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

    public static void smoothRotations() {
        if (!haveSmoothed)
            rotations = fixRotations(
                    new Vector2f(last.x, last.y),
                    new Vector2f(target.x, target.y),
                    speed
            );

        haveSmoothed = true;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    public static float[] getVanillaRotations(Entity entityIn) // from EntityLiving, originally called faceEntity
    {
        EntityPlayerSP entity = mc.player; // the player (your character)

        double deltaX = entityIn.posX - entity.posX;
        double deltaZ = entityIn.posZ - entity.posZ;
        double deltaY;

        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            deltaY = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (entity.posY +
                    (double)entity.getEyeHeight());
        }
        else
        {
            deltaY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D -
                    (entity.posY + (double)entity.getEyeHeight());
        }

        double hypotXZ = Math.hypot(deltaX,  deltaZ);
        float yaw = (float) Math.toDegrees(MathHelper.atan2(deltaZ, deltaX)) - 90.0F;
        float pitch = (float) Math.toDegrees(-(MathHelper.atan2(deltaY, hypotXZ)));
        return new float[]{yaw, pitch};
    }

    /**
     * Arguments: current rotation, intended rotation, max increment.
     */
    public static float updateRotation(float currentRotation, float nextRotation, float maxDifference) // interpolates the rotations (smooths it)
    {
        float f = MathHelper.wrapAngleTo180_float(nextRotation - currentRotation);

        if (f > maxDifference) // clamp to max
        {
            f = maxDifference;
        }

        if (f < -maxDifference)// clamp to min (-max)
        {
            f = -maxDifference;
        }

        return currentRotation + f;
    }
}
