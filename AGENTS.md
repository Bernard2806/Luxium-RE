# Agent Guide

## Project goal

Luxium-RE is the NeoForge continuation of Luxium targeting Minecraft 26.2. The migration source is the sibling repository `../Luxium-decompiled`, which contains the Forge 1.20.1 decompilation. Use it as a read-only reference; make all implementation changes in this repository.

The target toolchain is Java 25, NeoForge 26.2, and ModDevGradle. Sodium 0.9.2 for NeoForge 26.2 is a required client dependency, and Reese's Sodium Options (`mc26.2-2.2.4+neoforge`) is a second required client dependency that provides the options frontend. Integrate directly with Sodium; do not add Embeddium as a dependency or restore Embeddium runtime hooks.

## Options are managed through Sodium and RSO

**This replaces the original two-menu options layout. Do not port it back.** The Forge 1.20.1 version intercepted the vanilla Video Settings button with its own `VideoSettingsHubScreen` and offered two separate screens: vanilla/Embeddium Video Settings on one side, and the Luxium settings screen on the other. That design is intentionally abandoned.

Reese's Sodium Options is now a **required** client dependency and Luxium-RE has **no options screen of its own**. The single user-facing options surface is Sodium's options screen rendered by RSO, where Luxium-RE appears as its own tab:

1. `LuxiumSodiumConfig` is a Sodium `ConfigEntryPoint` annotated with `@ConfigEntryPointForge("luxium_re")`.
2. RSO discovers that entry point through Sodium's config API and renders its pages as a mod tab.
3. Every option binds to the existing `Config.CLIENT` value, so `luxium_re-client.toml` and the options screen always agree.

Consequences to respect:

- Adding a new user-facing option means adding it in `LuxiumSodiumConfig`, never in a new screen class.
- `VideoSettingsHubScreen`, `LuxiumConfigScreen`, `ConfigScreenModel`, `ConfigWidgets`, `ConfigOption`, `GlassPanelRenderer`, `ConfigPreviewImages` and `OptionsScreenMixin` are abandoned design. They stay excluded from the build as migration history and must not be resurrected, ported, or re-enabled. The in-game preview images under `assets/luxium/configpic` are only useful for those abandoned screens.
- `LuxiumREModClient` registers NeoForge's default `ConfigurationScreen` as a fallback for the mod list. That is a safety net, not the intended UX; do not build a custom screen on top of it.
- Never compile against RSO or Sodium UI classes. RSO is `localRuntime` only; the entire integration surface is Sodium's `net.caffeinemc.mods.sodium.api.config` package.
- Sodium rejects an option that lists itself as an enable dependency (`IllegalArgumentException: Option cannot depend on itself`). A feature master toggle must use the `alwaysOn` helper; its dependent sliders and sub-toggles use `under(<option id>)`. Never pass a toggle's own identifier to `under`.
- `IntegerOptionBuilder` requires a value formatter and enum options require either a `TextProvider` enum or an explicit `setElementNameProvider`. Omitting either fails client startup.
- Every option key needs matching `luxium_re.sodium.option.*` and `luxium_re.sodium.tooltip.*` entries in `assets/luxium_re/lang/en_us.json`.

## Migration status and boundaries

- The original source tree and visual assets have been imported as a migration baseline.
- `src/main/java/com/vinlanx/luxium/sodium` contains the compiled Sodium config API entry point, renderer bridge, and mixin accessors. The local-shadow bridge is not connected to the legacy `GpuShadowCache` yet.
- The original `client`, `mixin`, `rtx`, and `Testing` source packages are temporarily excluded from compilation in `build.gradle`. They still contain Forge 1.20.1/SRG-era code and are not part of the current runtime build.
- The legacy shader resource tree `assets/luxium/shaders/**` is temporarily excluded from packaged resources. Its Forge/Embeddium shader hooks and imports have not yet been ported to the Minecraft 26.2 graphics pipeline.
- Do not remove these exclusions until the corresponding subsystem compiles and has been checked in a NeoForge 26.2 client with Sodium.
- The current successful build validates the NeoForge bootstrap/config and compiles the standalone Sodium integration classes; it does not mean the original rendering features are active.

## Required commit policy

**Every completed work step must be recorded in its own small commit.** Commit messages must be written in English and follow Conventional Commits, for example:

- `feat: add Sodium terrain shadow capture`
- `fix: update sky shader imports for Minecraft 26.2`
- `refactor: migrate chunk hooks to Sodium 0.9`
- `build: enable migrated NeoForge renderer sources`

Keep each commit focused on one migration step. Do not accumulate unrelated changes into a large commit.

## Development and verification

Run commands from the repository root. The local Java installation used for this project must be a 64-bit JDK 25.

```bash
./gradlew build
./gradlew runClient
```

Sodium's unwrapped mod module is declared as `compileOnly`; the full NeoForge artifact is added as `localRuntime` for development runs. The `sodium_version` property in `gradle.properties` is the source of truth for both.

When porting a subsystem:

1. Read its decompiled implementation and its resources in `../Luxium-decompiled`.
2. Port it to Minecraft 26.2/NeoForge and, where terrain rendering is involved, Sodium 0.9.2 APIs.
3. Re-enable only the source/resource scope that is ready to compile.
4. Run `./gradlew build`; for client rendering changes, test `./gradlew runClient` with Sodium loaded.
5. Record the result and remaining blockers in `TODO.md`, then create a small Conventional Commit in English.

Minecraft 26.2 has a substantially redesigned rendering API. Prefer `Identifier`, `RenderPipeline`, `RenderPass`, `GpuDevice`, and feature submission APIs over the removed `ResourceLocation`, `ShaderInstance`, `Tesselator`, `MultiBufferSource`, and direct OpenGL state paths. Sodium's current internal packages use `net.caffeinemc.mods.sodium`; its chunk renderer receives current GPU buffers, sampler, fog parameters, and terrain pass data.

## Licensing and assets

Follow `LICENSE.txt` for Luxium material. `TEMPLATE_LICENSE.txt` applies to the upstream NeoForge MDK template files. Preserve required third-party notices and licenses. Do not modify `../Luxium-decompiled` as part of work in this repository.
