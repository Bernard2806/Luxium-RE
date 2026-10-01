# Luxium-RE Migration TODO

This list records the current port state for whoever continues the work. Keep it updated as each small migration commit lands.

- [x] Require Reese's Sodium Options (`mc26.2-2.2.4+neoforge`) as a client dependency. RSO reads the pages registered through Sodium's config API, so Luxium-RE does not compile against it and does not need its own options screen.
- [x] Expand the Sodium config registration from 10 toggles to the full 148-option Luxium set across 9 pages (upscaling, lighting, sky, atmosphere, reflections/post effects, wet surfaces/rain puddles, vegetation/terrain, performance). Every option is bound to its existing `Config.CLIENT` value, so the TOML file and the option screen stay in sync.
- [x] Verify the client reaches resource reload and the sound engine with all pages registered (RSO mixin applied, no config registration errors).

## Completed baseline

- [x] Initialize the project with the NeoForge 26.2 ModDevGradle MDK, Java 25 toolchain, and mod ID `luxium_re`.
- [x] Import the previous Luxium code and visual assets from the sibling decompilation repository (190 Java files and 176 resource files at import time).
- [x] Preserve the Luxium custom license and include it in the built JAR.
- [x] Configure Sodium 0.9.2 (`0.9.2+mc26.2`) from the CaffeineMC Maven repository. The local client run receives Sodium through `localRuntime`.
- [x] Add a Sodium config API entry point backed by the existing NeoForge client config.
- [x] Add `SodiumIntegration` and a Sodium 0.9.2 local shadow bridge using `SodiumWorldRenderer`, `RenderSectionManager`, `SectionStorage`, `ChunkRenderer`, `TerrainRenderPass`, and current GPU uniform/sampler data.
- [x] Register the Sodium renderer accessors through `luxiumre.mixins.json` and `neoforge.mods.toml`; the existing `GpuShadowCache` caller is still excluded and the bridge is not wired into active gameplay yet.
- [x] Verify `./gradlew clean build` succeeds for the active bootstrap/config/Sodium integration. A development client run loaded Luxium-RE and Sodium through the resource reload stage.
- [x] Commit work incrementally using small English Conventional Commits.

## Current build boundary

The renderer has **not** been fully migrated yet. `build.gradle` currently excludes the old `client`, `mixin`, `rtx`, and `Testing` packages, and excludes `assets/luxium/shaders/**` from packaged resources. The current successful build and client launch validate the bootstrap and Sodium integration, not the legacy visual features.

## Next migration steps

### P0 — Make the current Sodium terrain bridge an active feature

- [ ] Port `GpuShadowCache` and `NeoGpuVanilla` resource management from direct OpenGL/FBO/`RenderTarget` usage to the 26.2 `GpuDevice`, `RenderPass`, and texture-view APIs.
- [ ] Connect the compiled `SodiumLocalShadowBridge` to that capture path and verify rendered depth on a real client world.
- [ ] Adapt `NeoSkyCelestia` cascades to the Sodium chunk render lists and current chunk renderer without the old Embeddium bridge.

### P1 — Migrate the renderer hooks

- [ ] Port the remaining Sodium terrain mixins from 1.20.1 targets to the 0.9.2 NeoForge targets. Replace obsolete `me.jellysquid`/Forge class descriptors and verify each accessor/injection against the shipped Sodium 0.9.2 module.
- [ ] Migrate plant-wave/material encoding hooks to the current `BlockRenderer`, mutable quad, and `ChunkMeshBufferBuilder` APIs.
- [ ] Port fluid/water terrain hooks and the Sodium shader compile customization.
- [ ] Add/register only mixin classes whose targets are valid for Minecraft 26.2 and Sodium 0.9.2.

### P1 — Rebuild Luxium shaders for the 26.2 graphics pipeline

- [ ] Replace legacy `ShaderInstance` registration and JSON shader descriptors with 26.2 `RenderPipeline`/`RenderPass` setup.
- [ ] Port the core post effects (tonemapping, fog, rays, reflections, water, puddles, bloom, temporal reconstruction) to the new GPU API and backend-neutral resource model.
- [ ] Rebuild Sodium terrain shaders against its 0.9.2 shader includes and vertex formats; remove the old `shaders/embeddium` paths and unresolved `#moj_import`/`#import` resources.
- [ ] Re-enable shader resource packaging only after client resource reload succeeds with Sodium and the migrated pipelines compile.

### P2 — Port remaining client behavior

- [x] **Decided: options stay in Sodium/RSO.** The Forge 1.20.1 two-menu layout (`VideoSettingsHubScreen` presenting separate vanilla and Luxium screens) is abandoned and will not be ported. Luxium-RE keeps no options screen of its own; new options are added to `LuxiumSodiumConfig` instead of a new screen class. NeoForge's default `ConfigurationScreen` stays registered only as a mod-list fallback.
- [ ] Build out the remaining option pages that are still config-file only, if the ported subsystems justify exposing them (`sky` physical bake and sun/moon size, full `debugHud` layout options, the three cloud layers' remaining ~15 sliders each).
- [ ] Port F3/debug overlays, clouds, weather/wet surfaces, SSR, entity shadows, custom sky, and keybindings/events to NeoForge 26.2 APIs.
- [ ] Review the imported experimental `Testing` package; port only experiments that are intended to ship.
- [ ] Re-enable migrated source packages incrementally and remove their matching temporary source exclusions.

### Verification gate

- [ ] Run `./gradlew build` after each source-scope change.
- [ ] Run `./gradlew runClient` with Sodium 0.9.2 after each graphics/mixin/shader change; check resource reload and logs, not only Java compilation.
- [ ] Test a dedicated server startup to ensure all client-only renderer classes remain isolated.
- [ ] Update this file after every milestone and commit that milestone separately with an English Conventional Commit message.
