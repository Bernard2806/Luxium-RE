# NeoForge 1.21.1 branch guidance

## Target and toolchain

- Target Minecraft `1.21.1` and NeoForge `21.1.252` unless this branch's migration plan explicitly updates the target.
- Use Java 21 for Gradle and Minecraft runs. Gradle's Java toolchain is also set to 21.
- Use the checked-in Gradle Wrapper (`./gradlew` or `gradlew.bat`); do not rely on a globally installed Gradle.
- The project uses ModDevGradle `2.0.148` and official Mojang mappings with the configured Parchment mappings.

## Workflow

- Read `README.md` and `TODO.md` before version-specific work.
- Keep this branch independently buildable and adapt any ported code to Minecraft 1.21.1 / NeoForge APIs.
- Record completed migration work, known limitations, and next steps in `TODO.md`.
- Run `./gradlew build` after implementation changes; use `./gradlew runClient` when validating client-facing changes.
- Keep version-specific source, build files, and CI on this branch, not on the documentation-only `main` branch.

## Licensing

- Preserve `LICENSE.txt` and `TEMPLATE_LICENSE.txt`.
- Retain notices for the NeoForge MDK template and any third-party code or dependencies that are added.
