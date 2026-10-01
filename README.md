# Luxium RE

Proyecto base de mod para Minecraft 26.2 con NeoForge y ModDevGradle.

## Requisitos

- JDK 25 de 64 bits
- Git

## Desarrollo

```bash
./gradlew build
./gradlew runClient
```

El JAR generado queda en `build/libs/`. La clase principal del mod es
`com.vinlanx.luxium.re.LuxiumREMod` y los metadatos del mod se configuran desde
`gradle.properties`.

Este proyecto parte del [MDK oficial de NeoForge para 26.2 con ModDevGradle](https://github.com/NeoForgeMDKs/MDK-26.2-ModDevGradle).
