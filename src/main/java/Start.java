import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

import net.minecraft.client.main.Main;

public class Start
{
    public static void main(String[] args)
    {
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;
        switch (getOsType()) {
            case LINUX:
                workingDirectory = new File(userHome, ".minecraft/");
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, ".minecraft/");
                break;
            case MAC:
                workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                break;
            default:
                workingDirectory = new File(userHome, "minecraft/");
        }

        Main.main(concat(new String[]{
                "--version", "Lemon",
                "--accessToken", "0",
                "--assetIndex", "1.8.8",
                "--userProperties", "{}",
                "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static OS getOsType() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return OS.WINDOWS;
        } else if (os.contains("mac")) {
            return OS.MAC;
        } else if (os.contains("solaris") || os.contains("sunos")) {
            return OS.SOLARIS;
        } else if (os.contains("linux") || os.contains("unix")) {
            return OS.LINUX;
        } else {
            return OS.OTHER;
        }
    }

    public enum OS {
        LINUX, MAC, SOLARIS, OTHER, WINDOWS
    }
}
