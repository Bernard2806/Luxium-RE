# Luxium RE — NeoForge 1.21.1

This branch contains the NeoForge 1.21.1 project for Luxium RE, a performance-focused client graphics and lighting mod for Minecraft. It starts from the official [NeoForge 1.21.1 ModDevGradle MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle).

## Toolchain

- Minecraft: `1.21.1`
- NeoForge: `21.1.252`
- Java: `21` (required for compiling and running Minecraft 1.21.1)
- Gradle: `9.2.1`, provided by the Gradle Wrapper
- Mod development plugin: ModDevGradle `2.0.148`

## Build and run

Use a JDK 21 installation and run these commands from the repository root:

```sh
./gradlew build
./gradlew runClient
./gradlew runServer
./gradlew runData
```

On Windows, use `gradlew.bat` instead of `./gradlew`. The first run downloads Gradle and Minecraft/NeoForge development dependencies.

The starter mod includes an example block, item, creative tab, common config, and client entry point to demonstrate the 1.21.1 APIs. These are scaffolding and can be replaced as Luxium is ported.

## Licensing

Project-specific work is covered by [`LICENSE.txt`](LICENSE.txt). The NeoForge MDK starter files are additionally covered by [`TEMPLATE_LICENSE.txt`](TEMPLATE_LICENSE.txt). Mojang mappings are subject to the license referenced in the upstream MDK README.

## References

- [NeoForge documentation](https://docs.neoforged.net/)
- [NeoForge 1.21.1 ModDevGradle MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle)
