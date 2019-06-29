package website.zeb.deobf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import website.zeb.deobf.mapping.MappingType;
import website.zeb.deobf.mapping.OptifineMappingProviderFactory;
import website.zeb.deobf.resolver.MinecraftVersionResolver;
import website.zeb.deobf.resolver.YarnMappingsResolver;

/**
 * @author Zeb.
 * @since 0.1.0
 */
public class SimpleYarnDeobfuscator {

    public static void run(Path input, Path output, String mappings, MappingType from, MappingType to) throws IOException {
        Path minecraftJar = MinecraftVersionResolver.resolve(mappings);

        // Remap the minecraft jar incase we aren't coming from official mappings.
        if (from != MappingType.OFFICIAL) {
            System.out.printf("Remapping minecraft to %s from official...\n", to.toString().toLowerCase());

            Path remappedMinecraft = Files.createTempFile("minecraft-", String.format("%s.%s.jar", mappings, from.toString()));
            remap(minecraftJar, remappedMinecraft, mappings, ImmutableSet.of(), from, MappingType.OFFICIAL);

            minecraftJar = remappedMinecraft;
        }

        System.out.printf("Remapping %s...\n", input.toString());
        remap(input, output, mappings, ImmutableSet.of(minecraftJar), to, from);
    }

    public static void remap(Path input, Path output, String mappings, Set<Path> libraries, MappingType to, MappingType from) throws IOException {
        Files.deleteIfExists(output);

        Path minecraftJar = MinecraftVersionResolver.resolve(mappings);
        Path mappingsFile = YarnMappingsResolver.resolve(mappings, minecraftJar);

        IMappingProvider mappingProvider = MappingType.createMappingProvider(mappingsFile, from, to);
        mappingProvider = OptifineMappingProviderFactory.fixConflicts(mappingProvider);

        TinyRemapper remapper = createTinyRemapper(mappingProvider);

        OutputConsumerPath outputConsumer = new OutputConsumerPath(output);
        outputConsumer.addNonClassFiles(input);
        remapper.readInputs(input);

        remapper.readClassPath(libraries.toArray(new Path[libraries.size()]));

        remapper.apply(outputConsumer);
        outputConsumer.close();
        remapper.finish();
    }

    private static TinyRemapper createTinyRemapper(IMappingProvider mappingProvider) {
        return TinyRemapper.newRemapper()
            .withMappings(mappingProvider)
            .renameInvalidLocals(true)
            .rebuildSourceFilenames(true)
            .build();
    }

}