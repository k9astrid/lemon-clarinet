package dev.lemon.managers;

import dev.lemon.Lemon;
import dev.lemon.event.IEventListener;
import dev.lemon.event.annotations.Subscribe;
import dev.lemon.event.impl.EventKey;
import dev.lemon.module.Module;
import dev.lemon.module.impl.combat.KillAura;
import dev.lemon.module.impl.combat.Velocity;
import dev.lemon.module.impl.movement.Flight;
import dev.lemon.module.impl.movement.Speed;
import dev.lemon.module.impl.movement.Sprint;
import dev.lemon.module.impl.render.ClickGUI;
import dev.lemon.module.impl.render.HUD;
import dev.lemon.recode.module.impl.combat.*;
import dev.lemon.recode.module.impl.movement.*;
import dev.lemon.recode.module.impl.render.*;

import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public void initialize(){
        //TODO: use reflection for adding modules so we wont put thousand of modules.add ?
        modules.add(new Sprint());
        modules.add(new HUD());
        modules.add(new Speed());
        modules.add(new Flight());
        modules.add(new Velocity());
        modules.add(new ClickGUI());
        modules.add(new KillAura());

        modules.stream().filter(m -> m.getInfo().autoEnabled()).forEach(m -> m.setToggled(true));
        modules.forEach(Module::reflectValues);

        Lemon.INSTANCE.getEventBus().register(this);
    }

    @Subscribe
    public final IEventListener<EventKey> onKey = e -> {
        for (Module m : getModules())
            if (m.getKey() == e.getKeyCode())
                m.toggle();
    };

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public Module getModuleByName(String name){
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);

    }

    public List<Module> getSortedModules() {
        List<Module> moduleList = modules;
        moduleList.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                m.getName() + ((Objects.isNull(m.getSuffix())) ? "" : " " + m.getSuffix())
        )));
        Collections.reverse(moduleList);
        return moduleList;
    }

    public List<Module> getEnabledSortedModules(){
        return getSortedModules().stream().filter(Module::isToggled).collect(Collectors.toList());
    }
}
