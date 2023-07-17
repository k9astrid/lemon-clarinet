package dev.lemon.api.utils.player;

import dev.lemon.api.utils.IMethods;

public class MoveUtil implements IMethods {

    public static double getSpeed(){
        return Math.abs(Math.hypot(mc.player.posX - mc.player.lastTickPosX, mc.player.posZ - mc.player.lastTickPosZ)) * mc.timer.timerSpeed * 20;
    }

    public static boolean isWalking() {
        return mc.player.moveForward != 0 || mc.player.moveStrafing != 0;
    }

    public static void jump(double motion) {
        if(!mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.jump();
        }
        mc.player.motionY = motion;
    }

    private static float getPlayerDirection(){
        boolean movingForward = mc.player.moveForward > 0.0F;
        boolean movingBackward = mc.player.moveForward < 0.0F;
        boolean movingRight = mc.player.moveStrafing > 0.0F;
        boolean movingLeft = mc.player.moveStrafing < 0.0F;

        boolean isMovingSideways = movingLeft || movingRight;
        boolean isMovingStraight = movingForward || movingBackward;

        double direction = mc.player.rotationYaw;
        if(movingForward && !isMovingSideways) {

        } else if(movingBackward && !isMovingSideways)
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
            mc.player.motionX = -Math.sin(getPlayerDirection()) * speed;
            mc.player.motionZ = Math.cos(getPlayerDirection()) * speed;
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    public static void strafe(double speed){
        if(isWalking()) {
            mc.player.motionX = -Math.sin(getPlayerDirection()) * speed;
            mc.player.motionZ = Math.cos(getPlayerDirection()) * speed;
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }

    public static double getBaseSpeed(){
        return mc.player.isSprinting() ? 0.2805D : 0.216D;
    }

    public static void strafe(){
        strafe(getBaseSpeed());
    }
}
