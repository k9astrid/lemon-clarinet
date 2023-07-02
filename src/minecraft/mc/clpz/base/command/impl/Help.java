package mc.clpz.base.command.impl;

import org.apache.commons.lang3.text.WordUtils;

import mc.clpz.base.BaseClient;
import mc.clpz.base.command.Command;
import mc.clpz.base.utils.Printer;

public class Help extends Command {

	public Help() {
		super("Help", new String[]{"h", "help"});
	}

	@Override
	public void onRun(final String[] s) {
		BaseClient.INSTANCE.getCommandManager().getCommandMap().values().forEach(command -> {
			Printer.print(WordUtils.capitalizeFully(command.getLabel()));
		});
	}
}
