package dev.lemon.api.script;

import dev.lemon.api.utils.IMethods;
import lombok.Getter;

import java.io.File;

@Getter
public class ScriptManager implements IMethods {

    private final File directory = new File(mc.mcDataDir, "/Solar/Scripts");

    public ScriptManager() {
        if (!directory.exists())
            directory.mkdirs();
    }

}
