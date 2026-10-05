# Luxium-RE

Luxium-RE is the NeoForge continuation of Luxium, a performance-focused client graphics and lighting mod for Minecraft. This repository organizes development **by Minecraft version and mod loader**, so each port can use the APIs and toolchain appropriate to its target without blocking work on other versions.

## Version branches

| Branch | Minecraft | Loader | Status |
| --- | --- | --- | --- |
| [`neoforge-1.21.1`](https://github.com/Bernard2806/Luxium-RE/tree/neoforge-1.21.1) | 1.21.1 | NeoForge | Initialized; migration not started |
| [`neoforge-26.2`](https://github.com/Bernard2806/Luxium-RE/tree/neoforge-26.2) | 26.2 | NeoForge | Migration in progress; preserves the existing project history |

`main` is the documentation hub. Version-specific source code, build files, dependencies, CI workflows, and migration notes belong in their corresponding `neoforge-*` branches.

## Scaled migration approach

Each Minecraft/loader target has an independent branch and build configuration. Migration proceeds in manageable version steps: establish and validate a port on its target APIs, record its status in that branch's `TODO.md`, then use the working implementation and lessons as references for the next target. Changes should be adapted to the destination APIs rather than assumed to be directly mergeable across versions.

The initial targets are NeoForge 1.21.1 and NeoForge 26.2. The 1.21.1 branch is intentionally empty so its port can begin from the appropriate NeoForge MDK. The 26.2 branch retains the existing work and commit history.

## Development

Switch to the branch for the Minecraft and loader combination you want to work on. Follow that branch's `README.md`, `AGENTS.md`, and `TODO.md` for its JDK, build commands, dependencies, migration boundaries, and current status. The hub branch has no Gradle project and is not a build target.

## Project lineage and licensing

The [`Luxium-decompiled` repository](https://github.com/Bernard2806/Luxium-decompiled) contains the previous Forge 1.20.1 implementation and is used as a migration reference. Luxium-RE is distributed under the [Luxium Custom License](LICENSE.txt); the [template license](TEMPLATE_LICENSE.txt) applies to NeoForge MDK template files. Third-party components remain under their respective licenses.
