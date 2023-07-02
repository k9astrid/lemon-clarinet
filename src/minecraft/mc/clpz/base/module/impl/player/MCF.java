package mc.clpz.base.module.impl.player;

import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

import mc.clpz.base.BaseClient;
import mc.clpz.base.event.bus.Handler;
import mc.clpz.base.event.impl.input.MouseEvent;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.Printer;

/**
 * made by oHare for eclipse
 *
 * @since 9/27/2019
 **/
public class MCF extends Module {
    public MCF() {
        super("MCF", Category.PLAYER, new Color(200,200,0).getRGB());
    }
    @Handler
    public void onMouse(MouseEvent event) {
        if (event.getButton() == 2 && getMc().objectMouseOver != null && getMc().objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) getMc().objectMouseOver.entityHit;
            String name = player.getName();
            if (BaseClient.INSTANCE.getFriendManager().isFriend(name)) {
                BaseClient.INSTANCE.getFriendManager().removeFriend(name);
                Printer.print("Removed " + name + " as a friend!");
            } else {
                BaseClient.INSTANCE.getFriendManager().addFriend(name);
                Printer.print("Added " + name + " as a friend!");
            }
        }
    }
}
