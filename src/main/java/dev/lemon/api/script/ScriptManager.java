package dev.lemon.api.script;

import dev.lemon.api.utils.IMethods;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
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
        if (!init)
            scripts.removeIf(Script::isReloadable);

        File[] scriptFiles = directory.listFiles(((dir, name) -> name.endsWith(".js")));

        if (scriptFiles == null)
            return;

        for (File scriptFile : scriptFiles) {
            if (scripts.stream().anyMatch(s -> s.getFile().equals(scriptFile)))
                continue;

            try {
                scripts.add(new Script(scriptFile));
            } catch (Exception e) {
                scriptLogger.error("Failed to load " + scriptFile);
                scriptLogger.error(e.getMessage().replace("\r", "").replace("<eval>", scriptFile.getName()));
            }
        }

        scriptLogger.info("Reloaded!");
    }

}
