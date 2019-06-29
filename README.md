# Simple Yarn Deobf
A simple deobfuscator for minecraft mods built for Fabric's Yarn mappings

## Usage

The deobfusactor requires the following arguments be provided.

- Input, the input jar to deobfuscate
- Output, where the deobfuscated jar should be saved
- Mappings, the version of the yarn mappings that should be used, `1.14.3+build.9` for example
- From, the current mapping type the jar is using; either OFFICIAL, INTERMEDIARY, or NAMED
- To, the target mapping type.

Example command `java -jar ./build/libs/simple-yarn-deobf-0.1.0.jar --input ./working/OptiFine-1.14.3_HD_U_F1.jar --output ./working/OptiFine-1.14.3_HD_U_F1.dev.jar --mappings 1.14.3+build.9 --from OFFICIAL --to NAMED`