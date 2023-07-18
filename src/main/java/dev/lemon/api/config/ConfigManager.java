package dev.lemon.api.config;

import dev.lemon.api.utils.IMethods;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements IMethods {

    @Getter
    private final Map<String, Config> configs = new HashMap<>();

    @Getter @Setter
    private boolean loadVisuals;

    private final File configFolder = new File(mc.mcDataDir, "/Lemon/Configs");

    @Getter
    private Config activeConfig;

    public void initialize() {
        if (!configFolder.exists())
            configFolder.mkdirs();

        if (getConfig("default") == null) {
            Config config = new Config("default", true);
            config.write();
            configs.put(config.getName(), config);
        } else getConfig("default").read();
    }

    public void stop() {
        if (getConfig("default") == null) {
            Config config = new Config("default", true);
            config.write();
        } else getConfig("default").write();
    }

    public Config getConfig(String name) {
        return configs.keySet().stream().filter(key -> key.equalsIgnoreCase(name)).findFirst().map(configs::get).orElse(null);
    }
}
