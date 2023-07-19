package dev.lemon.api.bot.entity;

import com.mojang.authlib.GameProfile;
import dev.lemon.api.bot.network.BotPlayClient;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Getter @Setter
public class BotPlayer extends AbstractClientPlayer {
    public String currentContainerName;

    private String serverBrand;

    public final BotPlayClient connection;

    public BotPlayer(BotPlayClient client) {
        super(client.getWorld(), client.getProfile());

        this.currentContainerName = "";
        this.connection = client;
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(null);
        super.closeScreen();
    }
}
