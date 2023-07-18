package dev.lemon.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.Setting;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.ModeSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.other.FileUtil;
import dev.lemon.client.main.Lemon;
import lombok.Getter;
import lombok.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config implements IMethods {

    private final File directory = new File(mc.mcDataDir, "/Lemon/Configs");

    @Getter
    private final String name;

    private boolean saveKeybinds;

    public Config(String name, boolean saveBinds) {
        this.name = name;
        this.saveKeybinds = saveBinds;
    }

    public void write() {
        JsonObject jsonObject = new JsonObject();
        Lemon.INSTANCE.getModuleManager().getModulesMap().values().forEach(m -> {
            JsonObject mObject = new JsonObject();
            mObject.addProperty("state", m.isToggled());
            if (saveKeybinds)
                mObject.addProperty("bind", m.getKey());

            JsonObject vObject = new JsonObject();
            m.getSettings().forEach(v -> {
                if (v instanceof BooleanSetting)
                    vObject.addProperty(v.name, ((BooleanSetting) v).isToggled());

                if (v instanceof ModeSetting)
                    vObject.addProperty(v.name, ((ModeSetting) v).getMode());

                if (v instanceof NumberSetting)
                    vObject.addProperty(v.name, ((NumberSetting) v).getVal());
            });

            mObject.add("values", vObject);
            jsonObject.add(m.getName(), mObject);
        });
        FileUtil.writeJsonToFile(jsonObject, new File(directory, name + ".json").getAbsolutePath());
    }

    public void read() {
        JsonObject jsonObject = FileUtil.readJsonFromFile(new File(directory, name + ".json").getAbsolutePath());
        List<Module> ok = new ArrayList<>(Lemon.INSTANCE.getModuleManager().getModulesMap().values());

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            for (Module m : ok) {
                if (entry.getKey().equalsIgnoreCase(m.getName())) {
                    JsonObject jsonObject1 = (JsonObject) entry.getValue();
                    m.setToggled(jsonObject1.get("state").getAsBoolean());

                    if (jsonObject1.has("bind"))
                        m.setKey(jsonObject1.get("bind").getAsInt());

                    JsonObject values = jsonObject1.get("values").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> value : values.entrySet()) {
                        if (m.getValueByName(value.getKey()) != null) {
                            try {
                                Setting v = m.getValueByName(value.getKey());

                                if (v instanceof BooleanSetting)
                                    ((BooleanSetting) v).setToggled(value.getValue().getAsBoolean());

                                if (v instanceof ModeSetting)
                                    ((ModeSetting) v).setMode(value.getValue().getAsString());

                                if (v instanceof NumberSetting)
                                    ((NumberSetting) v).setValue(value.getValue().getAsDouble());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
