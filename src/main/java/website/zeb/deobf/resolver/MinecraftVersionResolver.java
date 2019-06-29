package website.zeb.deobf.resolver;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;

/**
 * @author Zeb.
 * @since 0.1.0
 */
public class MinecraftVersionResolver {

    public static Path resolve(String mappingsVersion) {
        String minecraftVersion = mappingsVersion.split("\\+")[0];

        return getMinecraftDirectory().resolve("versions").resolve(minecraftVersion).resolve(minecraftVersion + ".jar");
    }

    private static Path getMinecraftDirectory() {
        String home = System.getProperty("user.home", ".");
        File dir = null;

        String name = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((name.contains("mac")) || (name.contains("darwin"))) {
            dir = new File(home, "Library/Application Support/minecraft");
        } else if (name.contains("win")) {
            String appDataFolder = System.getenv("APPDATA");
            dir = new File((appDataFolder != null) ? appDataFolder : home, ".minecraft/");
        } else if (name.contains("nux")) {
            dir = new File(home, ".minecraft/");
        }

        return dir.toPath();
    }

}