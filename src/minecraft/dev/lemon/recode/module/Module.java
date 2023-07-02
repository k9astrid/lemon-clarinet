package dev.lemon.recode.module;

import dev.lemon.recode.Lemon;
import net.minecraft.client.Minecraft;

public class Module {
    private final String name = this.getClass().getDeclaredAnnotation(ModuleInfo.class).name();
    private int key = this.getClass().getDeclaredAnnotation(ModuleInfo.class).key();
    private final Category category = this.getClass().getDeclaredAnnotation(ModuleInfo.class).category();
    private boolean toggled = this.getClass().getDeclaredAnnotation(ModuleInfo.class).toggled();
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
}
