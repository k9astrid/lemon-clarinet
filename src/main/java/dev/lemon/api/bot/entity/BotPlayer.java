package dev.lemon.api.bot.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.world.World;

public class BotPlayer extends AbstractClientPlayer {
    public BotPlayer(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
}
