package dev.lemon.module;

import dev.lemon.Lemon;
import dev.lemon.setting.Setting;
import dev.lemon.utils.IMethods;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumChatFormatting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Module implements IMethods {

    @Getter
    private final Info info = this.getClass().getAnnotation(Info.class);

    @Getter
    private final String name = info.name();

    @Setter @Getter
    private int key = info.key();

    @Getter
    private final Category category = info.category();

    @Setter @Getter
    private boolean toggled, expanded;

    @Setter @Getter
    private String suffix = "";

    @Getter
    private final ArrayList<Setting<?>> settings = new ArrayList<>();

    public void reflectValues() {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.get(this) instanceof Setting) {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    settings.add((Setting<?>) field.get(this));
                }
            } catch (Exception ignored) {
            }
        }
    }

    public <T extends Setting<?>> T getValueByName(String name) {
        return (T) settings.stream().filter(value -> value.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) {
            onEnable();
            Lemon.INSTANCE.getEventBus().register(this);
        } else {
            Lemon.INSTANCE.getEventBus().unregister(this);
            onDisable();
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
            name += " " + EnumChatFormatting.GRAY + suffix;

        return name;
    }

    protected void onEnable()  { }
    protected void onDisable() { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        int key() default 0;
        Category category();
        boolean autoEnabled() default false;
    }

    @Getter
    public enum Category {
        COMBAT("Combat"),
        EXPLOITS("Exploits"),
        MISC("Misc"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        RENDER("Render"),
        WORLD("World");

        private final String name;

        public boolean expanded;

        Category(String name) {
            this.name = name;
        }
    }
}
