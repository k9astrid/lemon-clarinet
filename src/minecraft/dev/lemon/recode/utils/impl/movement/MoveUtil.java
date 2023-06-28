package dev.lemon.recode.utils.impl.movement;

import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import dev.lemon.recode.event.impl.MovementEvent;
import dev.lemon.recode.utils.Util;

public class MoveUtil {
	public class MovementUtils implements Util {
		
		public double getBaseMoveSpeed() {
	        double baseSpeed = 0.2873;
	        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
	        }
	        return baseSpeed;
	    }
		
		public void speedBoost(float speed) {
			float f = mc.thePlayer.rotationYaw * 0.017453292F;
	        
	        mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * speed);
	        mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * speed);
		}
		
		public void motionMult(double mult) {
			mc.thePlayer.motionX *= mult;
			mc.thePlayer.motionZ *= mult;
		}
		
		public boolean isGoingDiagonally() {
			return Math.abs(mc.thePlayer.motionX) > 0.1 && Math.abs(mc.thePlayer.motionZ) > 0.1;
		}

		public void setSpeed(MovementEvent event, String mode) {
			
		}

	}
}
