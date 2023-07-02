package mc.clpz.base.module.impl.other;

import mc.clpz.base.BaseClient;
import mc.clpz.base.module.Module;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;


public class Cape extends Module {

    public Cape() {
        super("Cape", Module.Category.PLAYER, 0xffaEfdEe);
        setHidden(true);
    }

    public ResourceLocation getCape() {
        return new ResourceLocation("textures/client/cape/cape.png");
    }

    public boolean canRender(AbstractClientPlayer player) {
        if(player == getMc().thePlayer) return true;
        return isEnabled() && BaseClient.INSTANCE.getFriendManager().isFriend(player.getName());
    }
}
