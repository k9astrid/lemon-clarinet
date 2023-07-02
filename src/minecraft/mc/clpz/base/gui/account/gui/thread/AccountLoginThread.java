package mc.clpz.base.gui.account.gui.thread;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import mc.clpz.base.BaseClient;
import mc.clpz.base.gui.account.gui.GuiAltManager;
import mc.clpz.base.gui.account.gui.impl.GuiAlteningLogin;
import mc.clpz.base.gui.account.system.Account;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.UUID;


/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class AccountLoginThread extends Thread {

    private String email, password;

    private String status = "Waiting for login...";

    public AccountLoginThread(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || (Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected && GuiDisconnected.niggaButton)) BaseClient.INSTANCE.switchToTheAltening();
        else BaseClient.INSTANCE.switchToMojang();
        status = "Logging in...";

        YggdrasilAuthenticationService yService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        UserAuthentication userAuth = yService.createUserAuthentication(Agent.MINECRAFT);

        if (userAuth == null) {
            status = "\2474Unknown error.";
            return;
        }

        userAuth.setUsername(email);
        userAuth.setPassword(password);
        try {
            userAuth.logIn();


            Session session = new Session(userAuth.getSelectedProfile().getName(), userAuth.getSelectedProfile().getId().toString(), userAuth.getAuthenticatedToken(), email.contains("@") ? "mojang" : "legacy");

            Minecraft.getMinecraft().setSession(session);

            Account account = new Account(email,password,email);
            account.setName(session.getUsername());
            if (!(Minecraft.getMinecraft().currentScreen instanceof GuiAlteningLogin || Minecraft.getMinecraft().currentScreen instanceof GuiDisconnected))
                BaseClient.INSTANCE.getAccountManager().setLastAlt(account);
            BaseClient.INSTANCE.getAccountManager().save();
            GuiAltManager.currentAccount = account;
            status = String.format("\247aLogged in as %s.", account.getName());
        } catch (AuthenticationException exception) {
            status = "\2474Login failed.";
        } catch (NullPointerException exception) {
            status = "\2474Unknown error.";
        }
    }

    public String getStatus() {
        return status;
    }

}


