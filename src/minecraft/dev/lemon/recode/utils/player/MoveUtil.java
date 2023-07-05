package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.Util;
import net.minecraft.client.Minecraft;

public class MoveUtil implements Util {

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
        mc.thePlayer.motionX = Math.cos(Math.toRadians(getPlayerDirection())) * speed;
        mc.thePlayer.motionZ = -Math.sin(Math.toRadians(getPlayerDirection())) * speed;
    }


}
