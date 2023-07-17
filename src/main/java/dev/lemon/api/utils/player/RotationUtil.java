package dev.lemon.api.utils.player;

import dev.lemon.api.utils.IMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil  implements IMethods {


    // from EntityLiving.java (in minecraft)
    public float[] getVanillaRotations(Entity entityIn)
    {
        double xDifference = entityIn.posX - mc.thePlayer.posX;
        double zDifference = entityIn.posZ - mc.thePlayer.posZ;
        double eyeDifference;

        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            eyeDifference = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        }
        else
        {
            eyeDifference = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        }

        double hypotOfPosDifference = (double) MathHelper.sqrt_double(xDifference * xDifference + zDifference * zDifference); // hypotenuse of the x and z difference

        float newYaw = (float)Math.toRadians(MathHelper.atan2(zDifference, xDifference)) - 90.0F; // 2 argument arctangent: the angle from the positive x axis (y = 0) to the line going from the center of the plane to the point (x, y)

        float newPitch = (float)Math.toRadians(-(MathHelper.atan2(eyeDifference, hypotOfPosDifference)));

        return new float[] {newYaw, newPitch};
    }

    /**
     * Arguments: current rotation, intended rotation, max increment.
     */
    private float updateRotation(float currentRotation, float nextRotation, float maxDifference) // interpolates the rotations (smooths it)
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
