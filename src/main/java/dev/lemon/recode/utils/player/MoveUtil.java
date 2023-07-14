package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.IMethods;

public class MoveUtil implements IMethods {

    public static double getSpeed(){
        return Math.abs(Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)) * mc.timer.timerSpeed * 20;
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

        if(movingBackward && !isMovingSideways)
            direction += 180;
        else if(movingForward && movingLeft)
            direction += 45;
        else if(movingForward)
            direction -= 45;
        else if(!isMovingStraight && movingLeft)
            direction += 90;
        else if(!isMovingStraight && movingRight)
            direction -= 90;
        else if(movingBackward && movingRight)
            direction -= 135;
        else if(movingBackward)
            direction += 135;

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

    public static void strafe(double speed){
        if(isWalking()) {
            mc.thePlayer.motionX = -Math.sin(getPlayerDirection()) * speed;
            mc.thePlayer.motionZ = Math.cos(getPlayerDirection()) * speed;
        } else {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }
    }

    public static double getBaseSpeed(){
        return mc.thePlayer.isSprinting() ? 0.2805D : 0.216D;
    }

    public static void strafe(){
        strafe(getBaseSpeed());
    }
}
