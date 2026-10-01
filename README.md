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

## Options

**All options are managed through Sodium's options screen, rendered by Reese's Sodium Options.** Luxium-RE has no options screen of its own.

This is a deliberate change from the Forge 1.20.1 release, which split settings across two menus: its own `VideoSettingsHubScreen` presented the vanilla/Embeddium Video Settings and a separate Luxium settings screen as two buttons. That layout is not being ported.

Instead:

- [Reese's Sodium Options](https://modrinth.com/mod/reeses-sodium-options) is a **required** client dependency and hosts the options frontend.
- `LuxiumSodiumConfig` registers the Luxium option pages through [Sodium's public config API](https://github.com/CaffeineMC/sodium/wiki/CaffeineMC-Maven-%26-Config-API). RSO discovers that entry point and shows Luxium-RE as its own tab.
- The full set of ~150 original options is exposed across pages for upscaling, lighting, sky, atmosphere, reflections and post effects, wet surfaces and rain puddles, vegetation, and performance.

Every option is bound to the same value used by the config file, so `config/luxium_re-client.toml` and the options screen never disagree.

## Port status

Migration is in progress. The original source and visual assets are being brought forward from Forge 1.20.1; Forge hooks and Embeddium-specific rendering integrations are being replaced with NeoForge 26.2 and Sodium equivalents.

The original renderer, lighting engines, and mixins are retained as migration references. The current NeoForge build only activates the mod bootstrap, the config, and the Sodium config API integration; legacy rendering source stays excluded until each subsystem is ported to the 26.2 graphics pipeline and Sodium 0.9.x APIs. The legacy options screens are excluded on purpose and are not part of the port.

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
