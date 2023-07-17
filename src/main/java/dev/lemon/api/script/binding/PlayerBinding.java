package dev.lemon.api.script.binding;

import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.player.MoveUtil;

public class PlayerBinding implements IMethods {

    public void respawn() {
        mc.player.respawnPlayer();
    }

    public void swingItem() {
        mc.player.swingItem();
    }

    public void setPitch(double pitch) {
        mc.player.rotationPitch = (float) pitch;
    }

    public void setYaw(double yaw) {
        mc.player.rotationYaw = (float) yaw;
    }

    public void setMotionX(double x) {
        mc.player.motionZ = x;
    }

    public void setMotionY(double y) {
        mc.player.motionY = y;
    }

    public void setMotionZ(double z) {
        mc.player.motionZ = z;
    }

    public void setPosition(double x, double y, double z) {
        mc.player.setPosition(x, y, z);
    }

    public void jump() {
        mc.player.jump();
    }

    public void setSneaking(boolean state) {
        mc.player.setSneaking(state);
    }

    public void setSprinting(boolean state) {
        mc.player.setSprinting(state);
    }

    public void sendMessage(String message) {
        mc.player.sendChatMessage(message);
    }

    public boolean collidedHorizontally() {
        return mc.player.isCollidedHorizontally;
    }

    public boolean collidedVertically() {
        return mc.player.isCollidedVertically;
    }

    public boolean collided() {
        return mc.player.isCollided;
    }

    public boolean moving() {
        return MoveUtil.isWalking();
    }

    public boolean eating() {
        return mc.player.isEating();
    }

    public boolean onGround() {
        return mc.player.onGround;
    }

    public boolean airBorne() {
        return mc.player.isAirBorne;
    }

    public boolean onLadder() {
        return mc.player.isOnLadder();
    }

    public boolean inWater() {
        return mc.player.isInWater();
    }

    public boolean inLava() {
        return mc.player.isInLava();
    }

    public boolean inWeb() {
        return mc.player.isInWeb;
    }

    public boolean inPortal() {
        return mc.player.inPortal;
    }

    public boolean usingItem() {
        return mc.player.isUsingItem();
    }

    public boolean burning() {
        return mc.player.isBurning();
    }

    public boolean dead() {
        return mc.player.isDead;
    }

    public boolean isPotionActive(int potionId) {
        return mc.player.isPotionActive(potionId);
    }

    public String name() {
        return mc.player.getName();
    }

    public int hurtTime() {
        return mc.player.hurtTime;
    }

    public int heldItemSlot() {
        return mc.player.inventory.currentItem;
    }

    public float pitch() {
        return mc.player.rotationPitch;
    }

    public float yaw() {
        return mc.player.rotationYaw;
    }

    public double x() {
        return mc.player.posX;
    }

    public double y() {
        return mc.player.posY;
    }

    public double z() {
        return mc.player.posZ;
    }

    public double motionX() {
        return mc.player.motionX;
    }

    public double motionY() {
        return mc.player.motionY;
    }

    public double motionZ() {
        return mc.player.motionZ;
    }

    public String ip() {
        return mc.isSingleplayer() || mc.getCurrentServerData() == null ? "singleplayer" : mc.getCurrentServerData().serverIP;
    }

}