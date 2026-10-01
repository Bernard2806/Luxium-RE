/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager
 *  com.mojang.blaze3d.systems.RenderSystem
 *  it.unimi.dsi.fastutil.longs.Long2ReferenceMap
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap
 *  me.jellysquid.mods.sodium.client.gl.device.CommandList
 *  me.jellysquid.mods.sodium.client.gl.device.RenderDevice
 *  me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer
 *  me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices
 *  me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderer
 *  me.jellysquid.mods.sodium.client.render.chunk.RenderSection
 *  me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager
 *  me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList
 *  me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable
 *  me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion
 *  me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses
 *  me.jellysquid.mods.sodium.client.render.viewport.CameraTransform
 *  net.minecraft.core.SectionPos
 *  net.minecraft.util.Mth
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package com.vinlanx.luxium.sodium.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.vinlanx.luxium.Config;
import com.vinlanx.luxium.sodium.mixin.RenderSectionManagerAccessor;
import com.vinlanx.luxium.sodium.mixin.SodiumWorldRendererAccessor;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionFlags;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.UniformBufferManager;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.storage.SectionStorage;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import net.caffeinemc.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.SectionPos;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class SodiumLocalShadowBridge {
    private static final int GEOMETRY_MASK = 1;
    private static final double SECTION_MARGIN = 18.0;
    private int frameId;

    public boolean isAvailable() {
        return SodiumWorldRenderer.instanceNullable() != null;
    }

    public PreparedCapture prepare(double lightX, double lightY, double lightZ, float radius, RenderTarget target) {
        SodiumWorldRenderer worldRenderer = SodiumWorldRenderer.instanceNullable();
        if (worldRenderer == null || target == null) {
            return null;
        }
        RenderSectionManager manager = ((SodiumWorldRendererAccessor)worldRenderer).luxium$getRenderSectionManager();
        if (manager == null) {
            return null;
        }
        RenderSectionManagerAccessor accessor = (RenderSectionManagerAccessor)manager;
        ChunkRenderer renderer = manager.getChunkRenderer();
        SectionStorage sections = accessor.luxium$getSectionStorage();
        UniformBufferManager uniforms = ((SodiumWorldRendererAccessor)worldRenderer).luxium$getUniformBufferManager();
        if (renderer == null || sections == null || sections.size() == 0 || uniforms == null) {
            return null;
        }
        double reach = Math.max(1.0, (double)radius) + 18.0;
        int minSectionX = (int)Math.floor((lightX - reach) / 16.0);
        int minSectionY = (int)Math.floor((lightY - reach) / 16.0);
        int minSectionZ = (int)Math.floor((lightZ - reach) / 16.0);
        int maxSectionX = (int)Math.floor((lightX + reach) / 16.0);
        int maxSectionY = (int)Math.floor((lightY + reach) / 16.0);
        int maxSectionZ = (int)Math.floor((lightZ + reach) / 16.0);
        int currentFrame = ++this.frameId;
        Reference2ObjectLinkedOpenHashMap byRegion = new Reference2ObjectLinkedOpenHashMap();
        double maxDistance = reach + 14.0;
        double maxDistanceSq = maxDistance * maxDistance;
        for (int sx = minSectionX; sx <= maxSectionX; ++sx) {
            for (int sz = minSectionZ; sz <= maxSectionZ; ++sz) {
                for (int sy = minSectionY; sy <= maxSectionY; ++sy) {
                    RenderRegion region;
                    double dz;
                    double dy;
                    double dx;
                    RenderSection section = sections.getConsistent(SectionPos.asLong(sx, sy, sz));
                    if (section == null || !section.isBuilt() || section.isDisposed() || (section.getRegion().getSectionFlags(section.getSectionIndex()) & RenderSectionFlags.MASK_HAS_BLOCK_GEOMETRY) == 0 || (dx = (double)section.getCenterX() - lightX) * dx + (dy = (double)section.getCenterY() - lightY) * dy + (dz = (double)section.getCenterZ() - lightZ) * dz > maxDistanceSq || (region = section.getRegion()) == null) continue;
                    ChunkRenderList list = (ChunkRenderList)byRegion.get((Object)region);
                    if (list == null) {
                        list = new ChunkRenderList(region);
                        list.reset(currentFrame);
                        byRegion.put((Object)region, (Object)list);
                    }
                    list.add(section.getSectionIndex());
                }
            }
        }
        LocalRenderLists lists = new LocalRenderLists(new ArrayList<ChunkRenderList>((Collection<ChunkRenderList>)byRegion.values()));
        return new PreparedCapture(renderer, uniforms, lists, target, lightX, lightY, lightZ);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean renderFace(PreparedCapture capture, Matrix4f projection, Matrix4f viewRotation, boolean includeCutout) {
        if (capture == null || capture.renderer == null || capture.lists == null) {
            return false;
        }
        if (capture.lists.regionCount() == 0) {
            return false;
        }
        ChunkRenderMatrices matrices = new ChunkRenderMatrices(projection, viewRotation);
        CameraTransform camera = new CameraTransform(capture.lightX, capture.lightY, capture.lightZ);
        capture.uniforms.prepareFrame();
        capture.uniforms.update(matrices, FogParameters.NONE);
        GpuBufferSlice uniformData = capture.uniforms.getUniformBuffer();
        GpuBuffer sectionTimeInfo = capture.uniforms.getSectionTimeInfo();
        GpuSampler terrainSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
        TerrainRenderPass solid = new ShadowRenderPass(ChunkSectionLayer.SOLID, true, capture.target);
        TerrainRenderPass cutout = new ShadowRenderPass(ChunkSectionLayer.CUTOUT, true, capture.target);
        capture.renderer.render(matrices, capture.lists, solid, camera, FogParameters.NONE, false, terrainSampler, uniformData, sectionTimeInfo);
        if (includeCutout) {
            capture.renderer.render(matrices, capture.lists, cutout, camera, FogParameters.NONE, false, terrainSampler, uniformData, sectionTimeInfo);
        }
        return true;
    }

    public static final class LocalRenderLists
    implements ChunkRenderListIterable {
        private final List<ChunkRenderList> lists;

        private LocalRenderLists(List<ChunkRenderList> lists) {
            this.lists = lists;
        }

        public Iterator<ChunkRenderList> iterator(boolean reverse) {
            if (!reverse) {
                return this.lists.iterator();
            }
            final ListIterator<ChunkRenderList> iterator = this.lists.listIterator(this.lists.size());
            return new Iterator<ChunkRenderList>(){

                @Override
                public boolean hasNext() {
                    return iterator.hasPrevious();
                }

                @Override
                public ChunkRenderList next() {
                    return (ChunkRenderList)iterator.previous();
                }
            };
        }

        public int regionCount() {
            return this.lists.size();
        }
    }

    private static final class ShadowRenderPass extends TerrainRenderPass {
        private final RenderTarget target;

        private ShadowRenderPass(ChunkSectionLayer layer, boolean allowFragmentDiscard, RenderTarget target) {
            super(layer, false, allowFragmentDiscard);
            this.target = target;
        }

        @Override
        public RenderTarget getTarget() {
            return this.target;
        }
    }

    public record PreparedCapture(ChunkRenderer renderer, UniformBufferManager uniforms, LocalRenderLists lists, RenderTarget target, double lightX, double lightY, double lightZ) {
    }
}
