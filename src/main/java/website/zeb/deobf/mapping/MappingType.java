package website.zeb.deobf.mapping;

import java.nio.file.Path;

import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.TinyUtils;

/**
 * @author Zeb.
 * @since 0.1.0
 */
public enum MappingType {

    OFFICIAL,
    INTERMEDIARY,
    NAMED;

    public static IMappingProvider createMappingProvider(Path mappingsFile, MappingType from, MappingType to) {
        return TinyUtils.createTinyMappingProvider(mappingsFile, from.toString(), to.toString());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}