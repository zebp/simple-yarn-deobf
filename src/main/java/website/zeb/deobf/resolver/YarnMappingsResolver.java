package website.zeb.deobf.resolver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import net.fabricmc.stitch.commands.CommandProposeFieldNames;

/**
 * @author Zeb
 * @since 0.1.0
 */
public class YarnMappingsResolver {

    public static Path resolve(String mappingsVersion, Path minecraftJar) throws IOException {
        Path mappingsTemp;
        String name = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if (name.contains("win")) {
            byte[] array = new byte[16];
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));

            mappingsTemp = Paths.get(String.format("mappings-%s.gz", generatedString));
        } else {
            mappingsTemp = Files.createTempFile("mappings", mappingsVersion);
        }

        FileOutputStream outputStream = new FileOutputStream(mappingsTemp.toFile());

        GZIPInputStream gzis = new GZIPInputStream(getMappingsFromMaven(mappingsVersion));

        byte[] buffer = new byte[1024];

        int len;
        while ((len = gzis.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        gzis.close();
        outputStream.close();

        Path mappings = Files.createTempFile("mappings", mappingsVersion);

        CommandProposeFieldNames fieldNames = new CommandProposeFieldNames();
        try {
            fieldNames.run(new String[] { minecraftJar.normalize().toString(), mappingsTemp.toFile().getAbsolutePath(), mappings.toFile().getAbsolutePath(), "--writeAll" });
        } catch (Exception e) {}

        return mappings;
    }

    private static InputStream getMappingsFromMaven(String mappingsVersion) throws IOException {
        URL url = new URL(String.format("https://maven.fabricmc.net/net/fabricmc/yarn/%s/yarn-%s-tiny.gz", mappingsVersion, mappingsVersion));
        return url.openConnection().getInputStream();
    }
    
}