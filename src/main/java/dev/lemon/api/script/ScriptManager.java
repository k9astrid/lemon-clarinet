package dev.lemon.api.script;

import dev.lemon.api.module.Module;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.main.Lemon;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class ScriptManager implements IMethods {

    private final Logger scriptLogger = LogManager.getLogger();
    private final File directory = new File(mc.mcDataDir, "/Lemon/Scripts");
    private final List<Script> scripts = new ArrayList<>();

    public ScriptManager() {
        if (!directory.exists())
            directory.mkdirs();
    }

    public void reload(boolean init) {
        HashMap<Object, Module> moduleList = Lemon.INSTANCE.getModuleManager().getModulesMap();

        if (!init)
            scripts.removeIf(Script::isReloadable);

        File[] scriptFiles = directory.listFiles(((dir, name) -> name.endsWith(".js")));

        if (scriptFiles == null)
            return;

        if (!init)
            for (Module module : moduleList.values()) {
                if (module.getCategory().equals(Module.Category.SCRIPTS) && module.isToggled()) {
                    if (((ScriptModule) module).isReloadable())
                        module.toggle();
                }
            }

        if (!init)
            moduleList.values().removeIf(clazz -> clazz.getCategory().equals(Module.Category.SCRIPTS) &&
                    ((ScriptModule) clazz).isReloadable());

        for (File scriptFile : scriptFiles) {
            if (scripts.stream().anyMatch(s -> s.getFile().equals(scriptFile)))
                continue;

            try {
                scripts.add(new Script(scriptFile));
            } catch (Exception e) {
                scriptLogger.error("Failed to load " + scriptFile);
                e.printStackTrace();
            }
        }

        scripts.forEach(s -> moduleList.put(s.getName(), s.getScriptModule()));

        scriptLogger.info("Reloaded!");
    }

}
