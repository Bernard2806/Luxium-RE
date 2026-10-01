package com.vinlanx.luxium.sodium;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;

/** Direct integration point for the Sodium NeoForge renderer. */
public final class SodiumIntegration {
    private SodiumIntegration() {
    }

    public static SodiumWorldRenderer currentWorldRenderer() {
        return SodiumWorldRenderer.instanceNullable();
    }

    public static void scheduleChunkRebuild(int chunkX, int sectionY, int chunkZ, boolean playerChanged) {
        SodiumWorldRenderer renderer = currentWorldRenderer();
        if (renderer != null) {
            renderer.scheduleRebuildForChunk(chunkX, sectionY, chunkZ, playerChanged);
        }
    }
}
