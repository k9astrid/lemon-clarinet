package dev.lemon.api.module;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.client.events.other.KeyboardInputEvent;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.combat.KillAura;
import dev.lemon.client.modules.combat.Velocity;
import dev.lemon.client.modules.exploits.Disabler;
import dev.lemon.client.modules.movement.Flight;
import dev.lemon.client.modules.movement.Speed;
import dev.lemon.client.modules.movement.Sprint;
import dev.lemon.client.modules.player.Stealer;
import dev.lemon.client.modules.render.ClickGUI;
import dev.lemon.client.modules.render.HUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ModuleManager {
    private HashMap<Object, Module> modules = new HashMap<>();

    public void initialize(){
        modules.put(Sprint.class, new Sprint());
        modules.put(HUD.class, new HUD());
        modules.put(Speed.class, new Speed());
        modules.put(Flight.class, new Flight());
        modules.put(Velocity.class, new Velocity());
        modules.put(ClickGUI.class, new ClickGUI());
        modules.put(KillAura.class, new KillAura());
        modules.put(Disabler.class, new Disabler());
        modules.put(Stealer.class, new Stealer());

        modules.values().stream().filter(Module::isAutoEnabled).forEach(m -> m.setToggled(true));
        modules.values().forEach(Module::reflectValues);

        Lemon.INSTANCE.getEventBus().register(this);
    }

    @Subscribe
    public final IEventListener<KeyboardInputEvent> onKey = e -> {
        for (Module m : modules.values())
            if (m.getKey() == e.getKeyCode())
                m.toggle();
    };

    public HashMap<Object, Module> getModulesMap() {
        return modules;
    }

    public List<Module> getModulesFromCategory(Module.Category category) {
        List<Module> modules = new ArrayList<>();

        for (Module m : this.modules.values())
            if (m.getCategory() == category)
                modules.add(m);

        return modules;
    }

    public Module getModuleByName(String name) {

        for (Module m : this.modules.values())
            if (Objects.equals(m.getName(), name))
                return m;

        return null;
    }
}
