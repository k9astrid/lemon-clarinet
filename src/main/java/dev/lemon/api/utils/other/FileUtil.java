package dev.lemon.api.utils.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class FileUtil {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public JsonObject readJsonFromFile(String path) {
        try {
            return GSON.fromJson(new FileReader(path), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeJsonToFile(JsonObject json, String path) {
        try {
            FileWriter writer = new FileWriter(path);
            GSON.toJson(json, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}
