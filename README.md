# Luxium-RE

Luxium-RE is the NeoForge continuation of Luxium, updated to run on **Minecraft 26.2**. It carries the original performance-focused graphics mod forward from its Forge 1.20.1 base to the current Minecraft and NeoForge APIs.

The [`Luxium-decompiled` repository](https://github.com/Bernard2806/Luxium-decompiled) contains the previous implementation and is used as a migration reference for Luxium-RE.

## Target platform

- Minecraft 26.2
- NeoForge 26.2
- Java 25
- ModDevGradle
- [Sodium](https://modrinth.com/mod/sodium) 0.9.2 for NeoForge 26.2

Sodium is a required **client-side** dependency. Its unwrapped NeoForge artifact is resolved from the [CaffeineMC Maven repository](https://maven.caffeinemc.net/). The version is pinned in `gradle.properties` as `sodium_version`.

## Port status

Migration is in progress. The original source and visual assets are being brought forward from Forge 1.20.1; Forge hooks and Embeddium-specific rendering integrations are being replaced with NeoForge 26.2 and Sodium equivalents.

## Development

Install a 64-bit JDK 25, then use the Gradle wrapper:

```bash
./gradlew build
./gradlew runClient
```

The built mod is written to `build/libs/`. The run configuration includes Sodium for local client testing.

## Licensing

Luxium-RE is distributed under the [Luxium Custom License](LICENSE.txt). The separate [template license](TEMPLATE_LICENSE.txt) applies to the NeoForge MDK template files. Sodium and other third-party components remain under their respective licenses.

## References

- [NeoForge 26.2 ModDevGradle MDK](https://github.com/NeoForgeMDKs/MDK-26.2-ModDevGradle)
- [NeoForge documentation](https://docs.neoforged.net/)
- [Sodium source and Maven dependency information](https://github.com/CaffeineMC/sodium/wiki/CaffeineMC-Maven-%26-Config-API)
