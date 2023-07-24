package dev.lemon.client.commands;

import dev.lemon.api.bot.BotStarter;
import dev.lemon.api.command.Command;
import dev.lemon.api.command.CommandInfo;
import dev.lemon.api.utils.player.ChatUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

@CommandInfo(name = "Bot", description = ":)")
public class Bot extends Command {

    @Override
    public void onExecute(String[] args) {
        super.onExecute(args);
        if (args[1].equalsIgnoreCase("join")) {
            (new Thread(() -> {
                for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                    if (args.length > 3)
                        try {
                            Thread.sleep(Long.parseLong(args[3]));
                        } catch (InterruptedException ignored) { }

                    BotStarter.run("Lemon" + RandomUtils.nextInt(1111, 9999), false, RandomStringUtils.randomAlphabetic(6).toLowerCase());
                    ChatUtil.send("sex " + "Lemon" + RandomUtils.nextInt(1111, 9999));
                }
            })).start();
        }
    }

}
