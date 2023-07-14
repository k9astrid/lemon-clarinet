package dev.lemon.recode.module;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module {
    private final String name = this.getClass().getDeclaredAnnotation(ModuleInfo.class).name();
    private int key = this.getClass().getDeclaredAnnotation(ModuleInfo.class).key();
    private final Category category = this.getClass().getDeclaredAnnotation(ModuleInfo.class).category();
    private boolean toggled;
    private String suffix = this.getClass().getDeclaredAnnotation(ModuleInfo.class).suffix();
    private final List<Setting> settings = new ArrayList<>();
    protected Minecraft mc = Minecraft.getMinecraft();
    protected Lemon lemon = Lemon.INSTANCE;

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public Category getCategory() {
        return category;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void toggle(){
        this.toggled = !toggled;
        if (toggled){
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void onEnable(){
        lemon.getEventBus().subscribe(this);
    }

    public void onDisable(){
        lemon.getEventBus().unsubscribe(this);
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
       settings.add(setting);
    }

    public void addSettings(Setting... settingArray) {
        settings.addAll(Arrays.asList(settingArray));
    }

    public List<Setting> getSettingsByName(String name) {
        return this.getSettings().stream().filter(s -> s.getName().equals(name)).collect(Collectors.toList());
    }
}
