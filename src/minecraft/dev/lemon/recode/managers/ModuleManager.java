package dev.lemon.recode.managers;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.impl.movement.Sprint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public void initialize(){
        Lemon.INSTANCE.getEventBus().subscribe(this);
        modules.add(new Sprint());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
    }
}
