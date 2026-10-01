/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ReferenceMap
 *  me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderer
 *  me.jellysquid.mods.sodium.client.render.chunk.RenderSection
 *  me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package com.vinlanx.luxium.sodium.mixin;

import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.storage.SectionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RenderSectionManager.class}, remap=false)
public interface RenderSectionManagerAccessor {
    @Accessor(value="renderSections", remap=false)
    SectionStorage luxium$getSectionStorage();
}
