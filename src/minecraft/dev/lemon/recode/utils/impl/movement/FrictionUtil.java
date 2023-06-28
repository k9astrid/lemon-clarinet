package dev.lemon.recode.utils.impl.movement;

import dev.lemon.recode.utils.Util;
import net.minecraft.client.Minecraft;
import dev.lemon.recode.Lemon;
import dev.lemon.recode.event.impl.MovementEvent;
import dev.lemon.recode.utils.Util;

public class FrictionUtil implements Util {

    private double speed;
    private boolean prevOnGround;
    private boolean strafing;
    
    private float lastDirection;
    
    private double lastMotionX, lastMotionZ;
    
    public void updateNCPFriction(MovementEvent e, double jumpMotionY, double jumpSpeed, double friction, double lastGroundMult) {
        if(mc.thePlayer.onGround) {
        	if(MovementUtils.isWalking()) {
        		e.setMotionY(mc.thePlayer.motionY = jumpMotionY);
        		speed = MovementUtils.getBaseMoveSpeed() + jumpSpeed;
                prevOnGround = true;
        	}
        } else {
            if (prevOnGround) {
                speed *= lastGroundMult;
                prevOnGround = false;
            }
            else {
            	if(speed > MovementUtils.getBaseMoveSpeed() / 2) {
            		speed -= speed / friction;
            	}
            }
        }
        MovementUtils.setSpeed(e, speed);
    }
    
}
