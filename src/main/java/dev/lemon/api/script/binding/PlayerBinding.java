package dev.lemon.api.script.binding;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.player.MoveUtil;

public class PlayerBinding implements IMethods {

    public void respawn() {
        mc.thePlayer.respawnPlayer();
    }

    public void swingItem() {
        mc.thePlayer.swingItem();
    }

    public void setPitch(double pitch) {
        mc.thePlayer.rotationPitch = (float) pitch;
    }

    public void setYaw(double yaw) {
        mc.thePlayer.rotationYaw = (float) yaw;
    }

    public void setMotionX(double x) {
        mc.thePlayer.motionZ = x;
    }

    public void setMotionY(double y) {
        mc.thePlayer.motionY = y;
    }

    public void setMotionZ(double z) {
        mc.thePlayer.motionZ = z;
    }

    public void setPosition(double x, double y, double z) {
        mc.thePlayer.setPosition(x, y, z);
    }

    public void jump() {
        mc.thePlayer.jump();
    }

    public void setSneaking(boolean state) {
        mc.thePlayer.setSneaking(state);
    }

    public void setSprinting(boolean state) {
        mc.thePlayer.setSprinting(state);
    }

    public void sendMessage(String message) {
        mc.thePlayer.sendChatMessage(message);
    }

    public boolean collidedHorizontally() {
        return mc.thePlayer.isCollidedHorizontally;
    }

    public boolean collidedVertically() {
        return mc.thePlayer.isCollidedVertically;
    }

    public boolean collided() {
        return mc.thePlayer.isCollided;
    }

    public boolean moving() {
        return MoveUtil.isWalking();
    }

    public boolean eating() {
        return mc.thePlayer.isEating();
    }

    public boolean onGround() {
        return mc.thePlayer.onGround;
    }

    public boolean airBorne() {
        return mc.thePlayer.isAirBorne;
    }

    public boolean onLadder() {
        return mc.thePlayer.isOnLadder();
    }

    public boolean inWater() {
        return mc.thePlayer.isInWater();
    }

    public boolean inLava() {
        return mc.thePlayer.isInLava();
    }

    public boolean inWeb() {
        return mc.thePlayer.isInWeb;
    }

    public boolean inPortal() {
        return mc.thePlayer.inPortal;
    }

    public boolean usingItem() {
        return mc.thePlayer.isUsingItem();
    }

    public boolean burning() {
        return mc.thePlayer.isBurning();
    }

    public boolean dead() {
        return mc.thePlayer.isDead;
    }

    public boolean isPotionActive(int potionId) {
        return mc.thePlayer.isPotionActive(potionId);
    }

    public String name() {
        return mc.thePlayer.getName();
    }

    public int hurtTime() {
        return mc.thePlayer.hurtTime;
    }

    public int heldItemSlot() {
        return mc.thePlayer.inventory.currentItem;
    }

    public float pitch() {
        return mc.thePlayer.rotationPitch;
    }

    public float yaw() {
        return mc.thePlayer.rotationYaw;
    }

    public double x() {
        return mc.thePlayer.posX;
    }

    public double y() {
        return mc.thePlayer.posY;
    }

    public double z() {
        return mc.thePlayer.posZ;
    }

    public double motionX() {
        return mc.thePlayer.motionX;
    }

    public double motionY() {
        return mc.thePlayer.motionY;
    }

    public double motionZ() {
        return mc.thePlayer.motionZ;
    }

    public String ip() {
        return mc.isSingleplayer() || mc.getCurrentServerData() == null ? "singleplayer" : mc.getCurrentServerData().serverIP;
    }

}