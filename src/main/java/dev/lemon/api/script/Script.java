package dev.lemon.api.script;

import dev.lemon.api.setting.Setting;
import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.other.FileUtil;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.Getter;
import lombok.Setter;

import javax.script.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

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

        final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        final ScriptEngine scriptEngine = factory.getScriptEngine(new ScriptFilter());
        final Bindings manager = new SimpleBindings();

        manager.put("initialize", new Initialize());

        scriptEngine.setBindings(manager, ScriptContext.GLOBAL_SCOPE);

        String scriptContent = FileUtil.readFile(file);

        scriptEngine.eval(scriptContent);

        if (name == null || author == null || description == null)
            throw new ScriptException("Script is missing name, author or description!");
    }

    private class Initialize implements Function<JSObject, Script> {

        @Override
        public Script apply(JSObject jsObject) {
            name = (String) jsObject.getMember("name");
            author = (String) jsObject.getMember("author");
            description = (String) jsObject.getMember("description");
            return Script.this;
        }
    }

}
