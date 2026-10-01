/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer
 *  me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.vinlanx.luxium.sodium.mixin;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.UniformBufferManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SodiumWorldRenderer.class}, remap=false)
public interface SodiumWorldRendererAccessor {
    @Accessor(value="renderSectionManager", remap=false)
    public RenderSectionManager luxium$getRenderSectionManager();

    @Accessor(value="uniformBufferManager", remap=false)
    UniformBufferManager luxium$getUniformBufferManager();
}
