# NeoForge 1.21.1 migration

## Completed

- [x] Initialize an independent Gradle project from the official NeoForge 1.21.1 ModDevGradle MDK.
- [x] Configure Minecraft 1.21.1, NeoForge 21.1.252, Java 21, and Luxium RE mod metadata.
- [x] Add a basic mod entry point, client entry point, and example registry/config scaffolding.
- [x] Document the toolchain, build commands, branch workflow, and template licensing.

## Next steps

- [ ] Build the starter project and resolve any environment- or dependency-related issues.
- [ ] Review the previous Luxium implementation and map its features to the NeoForge 1.21.1 rendering and graphics APIs.
- [ ] Replace the MDK demonstration content with Luxium's actual mod initialization and ported features incrementally.
- [ ] Add focused runtime checks for client startup and the rendering features as they are migrated.

## Current limitations

- The project currently contains the MDK demonstration block, item, creative tab, and sample configuration; Luxium functionality has not yet been ported.
- The starter has not yet been verified with a successful Gradle build in the target JDK environment.
