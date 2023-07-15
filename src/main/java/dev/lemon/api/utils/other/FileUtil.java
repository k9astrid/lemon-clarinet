package dev.lemon.api.utils.other;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@UtilityClass
public class FileUtil {

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
