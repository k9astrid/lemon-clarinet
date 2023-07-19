package dev.lemon.api.module;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.input.KeyboardInputEvent;
import dev.lemon.client.main.Lemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ModuleManager extends HashMap<Object, Module> {

    public void initialize(){
        this.values().stream().filter(Module::isAutoEnabled).forEach(m -> m.setToggled(true));
        this.values().forEach(Module::reflectValues);

        Lemon.INSTANCE.getEventBus().register(this);
    }

    @Subscribe
    public final IEventListener<KeyboardInputEvent> onKey = e -> {
        for (Module m : this.values())
            if (m.getKey() == e.getKeyCode())
                m.toggle();
    };

    public HashMap<Object, Module> getModulesMap() {
        return this;
    }

    public List<Module> getModulesFromCategory(Module.Category category) {
        List<Module> modules = new ArrayList<>();

        for (Module m : this.values())
            if (m.getCategory() == category)
                modules.add(m);

        return modules;
    }

    public Module getModuleByName(String name) {
        for (Module m : this.values())
            if (Objects.equals(m.getName(), name))
                return m;

        return null;
    }
}
