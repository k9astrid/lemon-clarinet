package dev.lemon.api.bot.entity;

import com.mojang.authlib.GameProfile;
import dev.lemon.api.bot.network.BotPlayClient;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;

@Getter @Setter
public class BotPlayer extends AbstractClientPlayer {
    public String currentContainerName;

    private String serverBrand;

    public final BotPlayClient connection;

    public MovementInput movementInput;

    private boolean hasValidHealth;

    public BotPlayer(BotPlayClient client) {
        super(client.getWorld(), client.getProfile());

        this.currentContainerName = "";
        this.connection = client;
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(null);
        super.closeScreen();
    }

    public void setXPStats(float currentXP, int maxXP, int level)
    {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }

    public void setPlayerSPHealth(float health)
    {
        if (this.hasValidHealth)
        {
            float f = this.getHealth() - health;

            if (f <= 0.0F)
            {
                this.setHealth(health);

                if (f < 0.0F)
                {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else
            {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                this.hurtTime = this.maxHurtTime = 10;
            }
        }
        else
        {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }

    public void sendChatMessage(String string) {
        this.connection.sendPacket(new C01PacketChatMessage(string));
    }
}
