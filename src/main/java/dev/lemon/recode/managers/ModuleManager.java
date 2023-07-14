package dev.lemon.recode.managers;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.event.IEventListener;
import dev.lemon.recode.event.annotations.Subscribe;
import dev.lemon.recode.event.impl.EventKey;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.impl.combat.*;
import dev.lemon.recode.module.impl.movement.*;
import dev.lemon.recode.module.impl.render.*;

import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public void initialize(){
        Lemon.INSTANCE.getEventBus().register(this);

        modules.add(new Sprint());
        modules.add(new HUD());
        modules.add(new Speed());
        modules.add(new Flight());
        modules.add(new Velocity());
        modules.add(new ClickGUI());
        modules.add(new KillAura());
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
