package dev.lemon.recode.utils.player;

import dev.lemon.recode.utils.math.MathUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
public final class JumpUtil {

    public static double getJumpHeight(final EntityPlayerSP player) {
        final double base = 0.42F;
        final PotionEffect effect = player.getActivePotionEffect(Potion.jump);
        return effect == null ? base : base + ((effect.getAmplifier() + 1) * 0.1F);
    }

    public static float getMinFallDist(final EntityPlayerSP player) {
        final float baseFallDist = 3.0F;
        final PotionEffect effect = player.getActivePotionEffect(Potion.jump);
        final int amp = effect != null ? effect.getAmplifier() + 1 : 0;
        return baseFallDist + amp;
    }
}

