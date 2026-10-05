# Repository guidance

## Branch layout

- `main` is the documentation hub only. Keep the version/loader branch map and repository-wide migration guidance here; do not add game source, Gradle build files, or version-specific CI here.
- Version branches use the `loader-minecraft-version` naming convention, for example `neoforge-1.21.1` and `neoforge-26.2`. All implementation and version-specific build/configuration work belongs on the matching branch.
- Before changing a version branch, read its `AGENTS.md`, `README.md`, and `TODO.md`. Those files define the target JDK, toolchain, dependencies, supported APIs, and build commands.
- Keep version branches independently buildable. Do not assume that changes can be merged or cherry-picked between Minecraft versions without adapting them to the destination APIs.

## Migration workflow

- Migrate incrementally and record completed work, known limitations, and the next steps in that version branch's `TODO.md`.
- Prefer small, focused commits with concise English Conventional Commit messages.
- Run the target branch's documented build and relevant checks after implementation changes. Do not run build commands on the documentation-only `main` branch.
- Keep this hub's version table accurate when branches are added or their migration status materially changes.

## Licensing

- Preserve `LICENSE.txt` and `TEMPLATE_LICENSE.txt` where project or template source is present.
- Retain applicable third-party notices and respect each dependency's license.
