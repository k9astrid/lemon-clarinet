package dev.lemon.api.script;

import dev.lemon.api.setting.Setting;
import dev.lemon.api.utils.IMethods;
import jdk.nashorn.api.scripting.JSObject;
import lombok.Getter;
import lombok.Setter;

import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Script implements IMethods {

    @Getter
    private final ArrayList<Setting> settings = new ArrayList<>();

    @Getter
    private String name, author, description;

    @Getter
    private final File file;

    @Getter
    private final HashMap<String, JSObject> eventHash = new HashMap<>();

    @Getter
    private boolean initializedSettings = false;

    @Setter @Getter
    private boolean reloadable = true;

    public Script(File file) throws ScriptException {
        this.file = file;
    }

}
