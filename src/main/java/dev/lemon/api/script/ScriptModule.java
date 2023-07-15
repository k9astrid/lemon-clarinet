package dev.lemon.api.script;

import dev.lemon.api.module.Module;
import jdk.nashorn.api.scripting.JSObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

public class ScriptModule extends Module {

    private final Logger scriptLogger = LogManager.getLogger();
    private final HashMap<String, JSObject> eventMap;

    @Getter
    private final File file;

    @Getter @Setter
    private boolean reloadable = true;

    public ScriptModule(String name, HashMap<String, JSObject> events, String author, File file) {
        super(name, Category.SCRIPTS);
        eventMap = events;
        this.file = file;
        setAuthor(author);
    }

    @Override
    protected void onEnable() {
        if (eventMap.containsKey("enable")) {
            try {
                eventMap.get("enable").call(null);
            } catch (Exception e) {
                scriptLogger.error("Error " + this);
                e.printStackTrace();
            }
        }
        super.onEnable();
    }
}
