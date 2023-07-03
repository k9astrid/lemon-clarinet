package dev.lemon.recode.utils.player;

import net.minecraft.client.Minecraft;

public class MoveUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double getSpeed(){
        return Math.abs(Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)) * 20;
    }

    private static float getPlayerDirection(){
        float newYaw = mc.thePlayer.rotationYaw;
        float strafeAngle = 45;

        if (mc.thePlayer.moveForward < 0){
            newYaw += 180;
            strafeAngle -= 45;
        }

        if (mc.thePlayer.moveStrafing > 0){
            newYaw -= strafeAngle;

            if (mc.thePlayer.moveForward == 0){
                newYaw -= 45;
            }
        } else if (mc.thePlayer.moveStrafing < 0){
            newYaw += strafeAngle;
            if (mc.thePlayer.moveForward == 0){
                newYaw += 45;
            }
        }
        return newYaw;
    }

    public static void setSpeed(float speed){
        mc.thePlayer.motionX = Math.sin(getPlayerDirection()) * speed;
        mc.thePlayer.motionZ = -Math.cos(getPlayerDirection()) * speed;
    }


}
