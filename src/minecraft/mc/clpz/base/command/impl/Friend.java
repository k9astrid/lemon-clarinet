package mc.clpz.base.command.impl;

import mc.clpz.base.BaseClient;
import mc.clpz.base.command.Command;
import mc.clpz.base.utils.Printer;

public class Friend extends Command {

	public Friend() {
		super("Friend",new String[]{"friends","friend","f"});
	}

	@Override
	public void onRun(final String[] args) {
		if (args.length == 1) {
			Printer.print(".friend add <name>");
			return;
		}
		switch (args[1]) {
			case "add":
			case "a":
			case "Add":
			case "Ad":
			case "ad":
				if (args.length > 1) {
					if (BaseClient.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print(args[2] + " is already your friend.");
						return;
					}
					if (args.length < 4) {
						BaseClient.INSTANCE.getNotificationManager().addNotification("Added " + args[2] + " to your friends list without an alias.", 2000);
                        Printer.print("Added " + args[2] + " to your friends list without an alias.");
						BaseClient.INSTANCE.getFriendManager().addFriend(args[2]);
					} else {
						BaseClient.INSTANCE.getNotificationManager().addNotification("Added " + args[2] + " to your friends list.", 2000);
                        Printer.print("Added " + args[2] + " to your friends list with the alias " + args[3] + ".");
						BaseClient.INSTANCE.getFriendManager().addFriendWithAlias(args[2], args[3]);
					}
				}
				break;
			case "del":
			case "delete":
			case "d":
			case "rem":
			case "remove":
			case "r":
				if (args.length > 1) {
					if (!BaseClient.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print(args[2] + " is not your friend.");
						return;
					}
					if (BaseClient.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print("Removed " + args[2] + " from your friends list.");
						BaseClient.INSTANCE.getFriendManager().removeFriend(args[2]);
					}
				}
				break;
			case "c":
			case "clear":
				if (BaseClient.INSTANCE.getFriendManager().getFriends().isEmpty()) {
					Printer.print("Your friends list is already empty.");
					return;
				}
				Printer.print("Your have cleared your friends list. Friends removed: " + BaseClient.INSTANCE.getFriendManager().getFriends().size());
				BaseClient.INSTANCE.getFriendManager().clearFriends();
				break;
			case "list":
			case "l":
				if (BaseClient.INSTANCE.getFriendManager().getFriends().isEmpty()) {
					Printer.print("Your friends list is empty.");
					return;
				}
				Printer.print("Your current friends are: ");
				BaseClient.INSTANCE.getFriendManager().getFriends().forEach(friend -> {
					Printer.print("Username: " + friend.getName() + (friend.getAlias() != null ? (" - Alias: " + friend.getAlias()) : ""));
				});
				break;
		}
	}
}
