package dev.lemon.recode.managers;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.module.Module;
import dev.lemon.recode.module.impl.movement.Speed;
import dev.lemon.recode.module.impl.movement.Sprint;
import dev.lemon.recode.module.impl.render.HUD;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();
    private Minecraft mc = Minecraft.getMinecraft();
    public void initialize(){
        Lemon.INSTANCE.getEventBus().subscribe(this);
        modules.add(new Sprint());
        modules.add(new HUD());
        modules.add(new Speed());

    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public List<Module> getSortedModules() {
        List<Module> moduleList = modules;
        moduleList.sort((m, m1) -> mc.fontRendererObj.getStringWidth(m.getName()+" "+m.getSuffix()));
        return moduleList;
    }

    public List<Module> getEnabledSortedModules(){
        return getSortedModules().stream().filter(Module::isToggled).collect(Collectors.toList());
    }
}
