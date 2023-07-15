package dev.lemon.api.script;

import dev.lemon.api.script.binding.ClientBinding;
import dev.lemon.api.script.binding.GuiBinding;
import dev.lemon.api.script.binding.PlayerBinding;
import dev.lemon.api.script.binding.WorldBinding;
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
    private String name, author;

    @Getter
    private final File file;

    @Getter
    private ScriptModule scriptModule;

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

        manager.put("client", new ClientBinding());
        manager.put("world", new WorldBinding());
        manager.put("player", new PlayerBinding());
        manager.put("gui", new GuiBinding());
        manager.put("initialize", new Initialize());

        scriptEngine.setBindings(manager, ScriptContext.GLOBAL_SCOPE);

        String scriptContent = FileUtil.readFile(file);

        scriptEngine.eval(scriptContent);

        if (name == null || author == null)
            throw new ScriptException("Script is missing name or author!");

        // Registering Module
        scriptModule = new ScriptModule(name, eventHash, author, file);
        settings.forEach(s -> scriptModule.addSettings(s));
        initializedSettings = true;

        scriptEngine.eval(scriptContent);
    }

    public void onEnable(JSObject handle) {
        eventHash.put("enable", handle);
    }

    public void onDisable(JSObject handle) {
        eventHash.put("disable", handle);
    }

    public void onTick(JSObject handle) {
        eventHash.put("tick", handle);
    }

    public void onRender2D(JSObject handle) {
        eventHash.put("render2D", handle);
    }

    //public void onPacket(JSObject handle) {
     //   eventHash.put("packet", handle);
    //}

    public void onPreMotion(JSObject handle) {
        eventHash.put("preMotion", handle);
    }

    public void onPostMotion(JSObject handle) {
        eventHash.put("postMotion", handle);
    }

    public void onChat(JSObject handle) {
        eventHash.put("chat", handle);
    }

    public void onKey(JSObject handle) {
        eventHash.put("key", handle);
    }

    private class Initialize implements Function<JSObject, Script> {

        @Override
        public Script apply(JSObject jsObject) {
            name = (String) jsObject.getMember("name");
            author = (String) jsObject.getMember("author");
            return Script.this;
        }
    }
}
