package dev.lemon.api.utils.player;

import dev.lemon.api.utils.IMethods;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil  implements IMethods {


    public static float[] getVanillaRotations(Entity entityIn) // from EntityLiving, originally called faceEntity
    {
        EntityPlayerSP entity = mc.thePlayer; // the player (your character)

        double deltaX = entityIn.posX - entity.posX;
        double deltaZ = entityIn.posZ - entity.posZ;
        double deltaY;

        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            deltaY = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        }
        else
        {
            deltaY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (entity.posY + (double)entity.getEyeHeight());
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
