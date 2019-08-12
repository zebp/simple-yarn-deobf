package website.zeb.deobf;

import java.io.IOException;
import java.nio.file.Paths;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import website.zeb.deobf.mapping.MappingType;

/**
 * @author Zeb.
 * @since 0.1.0
 */
public class SimpleYarnMain {

   public static void main(String[] args) throws IOException {
      OptionParser optionParser = new OptionParser();

      OptionSpec<String> input = optionParser.accepts("input").withRequiredArg().ofType(String.class);
      OptionSpec<String> output = optionParser.accepts("output").withRequiredArg().ofType(String.class);

      OptionSpec<String> mappings = optionParser.accepts("mappings").withRequiredArg().ofType(String.class);
      OptionSpec<MappingType> from = optionParser.accepts("from").withRequiredArg().ofType(MappingType.class);
      OptionSpec<MappingType> to = optionParser.accepts("to").withRequiredArg().ofType(MappingType.class);

      OptionSet options = optionParser.parse(args);

      SimpleYarnDeobfuscator.run(Paths.get(getOption(options, input)), Paths.get(getOption(options, output)), getOption(options, mappings), getOption(options, from), getOption(options, to));
   }

   @SuppressWarnings("all")
   private static <T> T getOption(OptionSet optionSet, OptionSpec<T> optionSpec) {
      try {
         return optionSet.valueOf(optionSpec);
      } catch (Throwable throwable) {
         throw throwable;
      }
   }

}