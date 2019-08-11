package website.zeb.deobf.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.tinyremapper.IMappingProvider;

/**
 * @author Zeb.
 * @since 0.1.0
 */
public class OptifineMappingProviderFactory {

    /**
     * All of the currently conflicting field mappings between Yarn and OptiFine.
     */
    private static final Set<String> CONFLICTING_FIELDS = ImmutableSet.of("CLOUDS", "renderDistance");

    /**
     * A regex to parse the keys of field mappings.
     */
    private static final Pattern KEY_PATTERN = Pattern.compile("(.+)\\/(.+);;([\\w/;]+)");

    /**
     * Fixes the name conflicts between yarn's named mappings and OptiFine's mappings.
     * 
     * @param provider A {@link IMappingProvider} that provides mappings to have it's conflicts removed.
     */
    public static IMappingProvider fixConflicts(IMappingProvider provider) {
        return (classMap, fieldMap, methodMap) -> {
            provider.load(classMap, fieldMap, methodMap); // Load all of the mappings from the given provider into our new provider

            addOptifineEntry(fieldMap); // Rename all of the fields that conflict in optifine to their name prepended to "_OF"
        };
    }

    private static void addOptifineEntry(Map<String, String> fieldMap) {
        Map<String, String> newEntries = new HashMap<>();

        fieldMap.entrySet().stream().filter(entry -> CONFLICTING_FIELDS.contains(entry.getValue())).forEach(entry -> {
            Matcher matcher = KEY_PATTERN.matcher(entry.getKey());

            if (!matcher.matches()) {
                System.err.printf("%s does not match regex\n", entry.getKey());
                return;
            }

            String className = matcher.group(1);
            String descriptor = matcher.group(3);

            String newKey = String.format("%s/%s;;%s", className, entry.getValue(), descriptor); // The new key for the optifine entry
            newEntries.put(newKey, entry.getValue() + "_OF");
        });

        fieldMap.putAll(newEntries);
    }
    
}