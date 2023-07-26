package dev.lemon.api.module;

import dev.lemon.api.notification.NotificationType;
import dev.lemon.client.main.Lemon;
import dev.lemon.api.setting.Setting;
import dev.lemon.api.utils.IMethods;
import dev.lemon.client.modules.render.HUD;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumChatFormatting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Module implements IMethods {

    @Getter
    private String name;

    @Setter @Getter
    private int key;

    @Getter
    private Category category;

    @Setter @Getter
    private boolean toggled, expanded, autoEnabled = false;

    @Setter @Getter
    private String suffix = "", author = "";

    @Getter
    private final ArrayList<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.key = 0;
    }

    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public void reflectValues() {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.get(this) instanceof Setting) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    settings.add((Setting) field.get(this));
                }
            } catch (Exception ignored) {
            }
        }
    }

    public <T extends Setting> T getValueByName(String name) {
        return (T) settings.stream().filter(value -> value.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) {
            onEnable();
            Lemon.INSTANCE.getEventBus().register(this);

            if (HUD.toggleNotifications.isToggled())
                Lemon.INSTANCE.getNotificationManager().call("Enabled " + this.getName(), NotificationType.SUCCESS);
        } else {
            Lemon.INSTANCE.getEventBus().unregister(this);
            onDisable();

            if (HUD.toggleNotifications.isToggled())
                Lemon.INSTANCE.getNotificationManager().call("Disabled " + this.getName(), NotificationType.ERROR);
        }
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if (this.toggled) {
            onEnable();
            Lemon.INSTANCE.getEventBus().register(this);
        } else {
            Lemon.INSTANCE.getEventBus().unregister(this);
            onDisable();
        }
    }

    public String getDisplayName() {
        String name = getName();

        if (!suffix.isEmpty() || !suffix.equals(""))
            name += " " + EnumChatFormatting.WHITE + suffix;

        return name;
    }

    protected void onEnable()  { }
    protected void onDisable() { }

    @Getter
    public enum Category {
        COMBAT("Combat"),
        EXPLOITS("Exploits"),
        MISC("Misc"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        RENDER("Render"),
        WORLD("World"),
        SCRIPTS("Scripts");

        private final String name;

        public boolean expanded;

        Category(String name) {
            this.name = name;
        }
    }
}
