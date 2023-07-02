package mc.clpz.base.command.impl;


import java.util.Objects;

import mc.clpz.base.BaseClient;
import mc.clpz.base.command.Command;
import mc.clpz.base.module.Module;
import mc.clpz.base.utils.Printer;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle",new String[]{"t","toggle"});
	}

	@Override
	public void onRun(final String[] s) {
		if (s.length <= 1) {
			Printer.print("Not enough args.");
			return;
		}
			for (Module m : BaseClient.INSTANCE.getModuleManager().getModuleMap().values()) {
				if (m.getLabel().toLowerCase().equals(s[1])) {
					m.toggle();
					BaseClient.INSTANCE.getNotificationManager().addNotification("Toggled " + (Objects.nonNull(m.getRenderLabel()) ? m.getRenderLabel():m.getLabel()), 2000);
                    Printer.print("Toggled " + m.getLabel());
					break;
				}
			}
	}
}
