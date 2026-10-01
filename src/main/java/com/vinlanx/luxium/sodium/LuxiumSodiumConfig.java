package com.vinlanx.luxium.sodium;

import com.vinlanx.luxium.Config;
import com.vinlanx.luxium.LuxiumREMod;
import com.vinlanx.luxium.compat.ForgeConfigSpec;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;
import net.caffeinemc.mods.sodium.api.config.ConfigState;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.IntegerOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ModOptionsBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Registers the Luxium-RE option pages on Sodium's public config API. Reese's Sodium Options
 * renders these pages as a tab inside its own frontend, so Luxium-RE does not need its own screen.
 */
@ConfigEntryPointForge(LuxiumREMod.MOD_ID)
public final class LuxiumSodiumConfig implements ConfigEntryPoint {
    private static final String NAMESPACE = LuxiumREMod.MOD_ID;
    private static final String GROUP = "luxium_re.sodium.group.";
    private static final String OPTION = "luxium_re.sodium.option.";
    private static final String TOOLTIP = "luxium_re.sodium.tooltip.";
    private static final OptionFlag[] NO_FLAGS = new OptionFlag[0];

    private static final Identifier MASTER_SWITCH = id("enabled");
    private static final Identifier GPU_LIGHTING = id("gpu_lighting");
    private static final Identifier SKY_LIGHTING = id("sky_lighting");
    private static final Identifier CLOUDS = id("realistic_clouds");
    private static final Identifier WATER = id("realistic_water");
    private static final Identifier TONEMAP = id("tonemap");
    private static final Identifier REFLECTIONS = id("screen_space_reflections");
    private static final Identifier LSR = id("lsr");
    private static final Identifier TFR = id("tfr");
    private static final Identifier WET = id("wet");
    private static final Identifier VOLUMETRIC = id("volumetric_god_rays");
    private static final Identifier PLANT_WAVES = id("plant_waves");
    private static final Identifier GOD_RAYS = id("god_rays");
    private static final Identifier FOG = id("fog");

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        ModOptionsBuilder mod = builder.registerOwnModOptions()
                .setName("Luxium-RE")
                .setVersion(LuxiumREMod.VERSION)
                .setIcon(id("icon.png"));

        mod.addPage(upscalingPage(builder));
        mod.addPage(lightingPage(builder));
        mod.addPage(skyPage(builder));
        mod.addPage(atmospherePage(builder));
        mod.addPage(reflectionsPage(builder));
        mod.addPage(surfacesPage(builder));
        mod.addPage(vegetationPage(builder));
        mod.addPage(performancePage(builder));
    }

    /**
     * Luxium Spatial Resolution and temporal reconstruction. These two options change how the
     * frame itself is produced rather than how it is shaded, which is the main difference between
     * Luxium and a conventional shader pack.
     */
    private static OptionPageBuilder upscalingPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.upscaling"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("lsr"))
                        .addOption(alwaysOn(toggle(builder, "lsr", Config.CLIENT.lsrEnabled), OptionFlag.REQUIRES_RENDERER_RELOAD))
                        .addOption(under(decimal(builder, "lsr_render_scale", Config.CLIENT.lsrRenderScale, 0.5, 0.85, 0.01), LSR))
                        .addOption(under(decimal(builder, "lsr_sharpness", Config.CLIENT.lsrSharpness, 0.0, 1.0, 0.01), LSR)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("tfr"))
                        .addOption(alwaysOn(toggle(builder, "tfr", Config.CLIENT.tfrEnabled), OptionFlag.REQUIRES_RENDERER_RELOAD))
                        .addOption(under(decimal(builder, "tfr_reprojection", Config.CLIENT.tfrReprojectionStrength, 0.0, 1.0, 0.01), TFR))
                        .addOption(under(decimal(builder, "tfr_history_stability", Config.CLIENT.tfrHistoryStability, 0.0, 0.35, 0.01), TFR))
                        .addOption(under(decimal(builder, "tfr_camera_cut_distance", Config.CLIENT.tfrCameraCutDistance, 0.25, 16.0, 0.25), TFR))
                        .addOption(under(decimal(builder, "tfr_camera_cut_angle", Config.CLIENT.tfrCameraCutAngle, 5.0, 90.0, 1.0), TFR)));
    }

    private static OptionPageBuilder lightingPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.lighting"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("lighting"))
                        .addOption(alwaysOn(toggle(builder, "enabled", Config.CLIENT.luxiumEnabled), OptionFlag.REQUIRES_GAME_RESTART))
                        .addOption(alwaysOn(toggle(builder, "gpu_lighting", Config.CLIENT.gpuShadowsEnabled), OptionFlag.REQUIRES_RENDERER_RELOAD))
                        .addOption(under(slider(builder, "gpu_lighting_distance", Config.CLIENT.gpuLocalLightDistance, 4, 64, 1), GPU_LIGHTING))
                        .addOption(under(choice(builder, "gpu_lighting_mode", Config.CLIENT.gpuLocalLightingMode, Config.GpuLocalLightingMode.class), GPU_LIGHTING))
                        .addOption(under(choice(builder, "gpu_shadow_mode", Config.CLIENT.gpuLocalShadowMode, Config.GpuLocalShadowMode.class), GPU_LIGHTING))
                        .addOption(slider(builder, "rtx_block_lighting", Config.CLIENT.floodRadiusCap, 16, 36, 1)));
    }

    private static OptionPageBuilder skyPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.sky"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("sky"))
                        .addOption(alwaysOn(toggle(builder, "sky_lighting", Config.CLIENT.skyLightEnabled), OptionFlag.REQUIRES_RENDERER_UPDATE))
                        .addOption(under(toggle(builder, "sky_lighting_colors", Config.CLIENT.skyLightColorsEnabled), SKY_LIGHTING))
                        .addOption(under(toggle(builder, "sky_cloud_shadows", Config.CLIENT.skyCloudShadowsEnabled), SKY_LIGHTING))
                        .addOption(under(toggle(builder, "sky_entity_shadows", Config.CLIENT.skyEntityShadowsEnabled), SKY_LIGHTING))
                        .addOption(under(toggle(builder, "sky_soft_shadows", Config.CLIENT.skyShadowSoftShadowsEnabled), SKY_LIGHTING)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("sky_quality"))
                        .addOption(under(slider(builder, "sky_shadow_near_resolution", Config.CLIENT.skyShadowNearResolution, 256, 4096, 128), SKY_LIGHTING))
                        .addOption(under(slider(builder, "sky_shadow_far_resolution", Config.CLIENT.skyShadowFarResolution, 256, 4096, 128), SKY_LIGHTING))
                        .addOption(under(slider(builder, "sky_shadow_near_radius", Config.CLIENT.skyShadowNearRadius, 16, 128, 1), SKY_LIGHTING))
                        .addOption(under(slider(builder, "sky_shadow_far_radius", Config.CLIENT.skyShadowFarRadius, 64, 384, 1), SKY_LIGHTING))
                        .addOption(under(slider(builder, "sky_shadow_samples", Config.CLIENT.skyShadowFilterSamples, 1, 4, 1), SKY_LIGHTING)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("celestial"))
                        .addOption(under(percent(builder, "sun_strength", Config.CLIENT.skyLightSunStrength), SKY_LIGHTING))
                        .addOption(under(percent(builder, "moon_strength", Config.CLIENT.skyLightMoonStrength), SKY_LIGHTING))
                        .addOption(under(percent(builder, "ambient_strength", Config.CLIENT.skyLightAmbientStrength), SKY_LIGHTING)));
    }

    private static OptionPageBuilder atmospherePage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.atmosphere"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("water"))
                        .addOption(alwaysOn(toggle(builder, "realistic_water", Config.CLIENT.waterEnabled), OptionFlag.REQUIRES_RENDERER_RELOAD))
                        .addOption(under(toggle(builder, "water_detail_waves", Config.CLIENT.waterDetailWaves), WATER))
                        .addOption(under(toggle(builder, "water_depth_aware_refraction", Config.CLIENT.waterDepthAwareRefraction), WATER))
                        .addOption(under(decimal(builder, "water_render_scale", Config.CLIENT.waterRenderScale, 0.25, 1.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_wave_strength", Config.CLIENT.waterWaveStrength, 0.0, 5.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_wave_scale", Config.CLIENT.waterWaveScale, 0.25, 10.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_wave_speed", Config.CLIENT.waterWaveSpeed, 0.0, 6.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_detail_strength", Config.CLIENT.waterDetailStrength, 0.0, 1.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_refraction_strength", Config.CLIENT.waterRefractionStrength, 0.0, 4.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_absorption_strength", Config.CLIENT.waterAbsorptionStrength, 0.0, 3.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_visibility_depth", Config.CLIENT.waterVisibilityDepth, 2.0, 48.0, 0.5), WATER))
                        .addOption(under(decimal(builder, "water_fresnel_strength", Config.CLIENT.waterFresnelStrength, 0.0, 1.5, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_specular_strength", Config.CLIENT.waterSpecularStrength, 0.0, 2.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_specular_sharpness", Config.CLIENT.waterSpecularSharpness, 16.0, 512.0, 1.0), WATER)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("water_reflections"))
                        .addOption(under(toggle(builder, "water_ssr", Config.CLIENT.waterSsrEnabled), WATER))
                        .addOption(under(decimal(builder, "water_ssr_strength", Config.CLIENT.waterSsrStrength, 0.0, 2.0, 0.01), WATER))
                        .addOption(under(decimal(builder, "water_ssr_max_distance", Config.CLIENT.waterSsrMaxDistance, 4.0, 128.0, 1.0), WATER)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("clouds"))
                        .addOption(alwaysOn(toggle(builder, "realistic_clouds", Config.CLIENT.cloudsEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(toggle(builder, "cloud_layer_low", Config.CLIENT.cloudLayer1Enabled), CLOUDS))
                        .addOption(under(toggle(builder, "cloud_layer_main", Config.CLIENT.cloudLayer2Enabled), CLOUDS))
                        .addOption(under(toggle(builder, "cloud_layer_high", Config.CLIENT.cloudLayer3Enabled), CLOUDS))
                        .addOption(under(decimal(builder, "cloud_low_height", Config.CLIENT.cloudLayer1Height, 64.0, 384.0, 1.0), CLOUDS)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("tonemap"))
                        .addOption(alwaysOn(toggle(builder, "tonemap", Config.CLIENT.tonemapEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "tonemap_exposure", Config.CLIENT.tonemapExposure, -2.0, 2.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_contrast", Config.CLIENT.tonemapContrast, 0.0, 1.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_highlight_compression", Config.CLIENT.tonemapHighlightCompression, 0.0, 1.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_shadow_depth", Config.CLIENT.tonemapShadowDepth, 0.0, 1.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_saturation", Config.CLIENT.tonemapSaturation, 0.0, 2.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_vibrance", Config.CLIENT.tonemapVibrance, 0.0, 1.0, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_gamma", Config.CLIENT.tonemapGamma, 1.6, 2.8, 0.01), TONEMAP))
                        .addOption(under(decimal(builder, "tonemap_strength", Config.CLIENT.tonemapStrength, 0.0, 1.0, 0.01), TONEMAP)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("fog"))
                        .addOption(alwaysOn(toggle(builder, "fog", Config.CLIENT.fogEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "fog_density", Config.CLIENT.fogDensity, 0.0, 2.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_scattering_brightness", Config.CLIENT.fogScatteringBrightness, 0.0, 2.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_max_brightness", Config.CLIENT.fogMaxBrightness, 0.0, 2.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_scattering_strength", Config.CLIENT.fogScatteringStrength, 0.0, 5.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_start_distance", Config.CLIENT.fogStartDistance, 0.0, 160.0, 1.0), FOG))
                        .addOption(under(decimal(builder, "fog_near_fade", Config.CLIENT.fogNearFade, 0.0, 64.0, 0.5), FOG))
                        .addOption(under(decimal(builder, "fog_max_opacity", Config.CLIENT.fogMaxOpacity, 0.0, 1.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_sky_tint", Config.CLIENT.fogSkyTint, 0.0, 1.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_distance_curve", Config.CLIENT.fogDistanceCurve, 0.35, 3.0, 0.01), FOG))
                        .addOption(under(decimal(builder, "fog_height", Config.CLIENT.fogHeight, 50.0, 384.0, 1.0), FOG))
                        .addOption(under(toggle(builder, "fog_celestial_scattering", Config.CLIENT.fogCelestialScatteringEnabled), FOG))
                        .addOption(under(toggle(builder, "fog_dynamic_celestial_color", Config.CLIENT.fogDynamicCelestialColor), FOG)));
    }

    private static OptionPageBuilder reflectionsPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.reflections"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("reflections"))
                        .addOption(alwaysOn(toggle(builder, "screen_space_reflections", Config.CLIENT.reflectionEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(slider(builder, "reflection_distance", Config.CLIENT.reflectionDistance, 5, 200, 1), REFLECTIONS))
                        .addOption(under(decimal(builder, "reflection_render_scale", Config.CLIENT.reflectionRenderScale, 0.1, 1.0, 0.01), REFLECTIONS)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("post_effects"))
                        .addOption(alwaysOn(toggle(builder, "kawase_bloom", Config.CLIENT.kawaseBloomEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(decimal(builder, "bloom_intensity", Config.CLIENT.kawaseBloomIntensity, 0.0, 3.0, 0.01))
                        .addOption(slider(builder, "bloom_levels", Config.CLIENT.kawaseBloomLevels, 2, 7, 1))
                        .addOption(alwaysOn(toggle(builder, "god_rays", Config.CLIENT.skyGodRaysEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "god_ray_intensity", Config.CLIENT.skyGodRaysRayIntensity, 0.0, 2.5, 0.01), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_sun_size", Config.CLIENT.skyGodRaysSunSize, 0.5, 2.0, 0.01), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_moon_size", Config.CLIENT.skyGodRaysMoonSize, 0.5, 2.0, 0.01), GOD_RAYS))
                        .addOption(under(toggle(builder, "god_ray_disc", Config.CLIENT.skyGodRaysCelestialDiscEnabled), GOD_RAYS))
                        .addOption(under(toggle(builder, "god_ray_halo", Config.CLIENT.skyGodRaysHaloEnabled), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_halo_size", Config.CLIENT.skyGodRaysHaloSize, 0.5, 2.5, 0.01), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_disc_size", Config.CLIENT.skyGodRaysDiscSize, 0.5, 1.8, 0.01), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_weather_influence", Config.CLIENT.skyGodRaysWeatherInfluence, 0.0, 2.0, 0.01), GOD_RAYS))
                        .addOption(under(decimal(builder, "god_ray_center_suppression", Config.CLIENT.skyGodRaysCenterSuppression, 0.0, 2.0, 0.01), GOD_RAYS)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("volumetric"))
                        .addOption(alwaysOn(toggle(builder, "volumetric_god_rays", Config.CLIENT.skyVolumetricGodRaysEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "volumetric_intensity", Config.CLIENT.skyVolumetricGodRaysIntensity, 0.0, 3.0, 0.01), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_density", Config.CLIENT.skyVolumetricGodRaysDensity, 0.0, 3.0, 0.01), VOLUMETRIC))
                        .addOption(under(slider(builder, "volumetric_samples", Config.CLIENT.skyVolumetricGodRaysSamples, 8, 32, 1), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_uniformity", Config.CLIENT.skyVolumetricGodRaysUniformity, 0.0, 100.0, 1.0), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_side_visibility", Config.CLIENT.skyVolumetricGodRaysSideVisibility, 0.0, 100.0, 1.0), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_haze_suppression", Config.CLIENT.skyVolumetricGodRaysHazeSuppression, 0.0, 100.0, 1.0), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_max_distance", Config.CLIENT.skyVolumetricGodRaysMaxDistance, 16.0, 384.0, 1.0), VOLUMETRIC))
                        .addOption(under(decimal(builder, "volumetric_anisotropy", Config.CLIENT.skyVolumetricGodRaysAnisotropy, 0.0, 0.95, 0.01), VOLUMETRIC))
                        .addOption(under(toggle(builder, "volumetric_entity_occlusion", Config.CLIENT.skyVolumetricGodRaysEntityOcclusion), VOLUMETRIC)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("lens_flare"))
                        .addOption(alwaysOn(toggle(builder, "lens_flare", Config.CLIENT.lensFlareEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(decimal(builder, "lens_flare_intensity", Config.CLIENT.lensFlareIntensity, 0.0, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_streak_intensity", Config.CLIENT.lensFlareStreakIntensity, 0.0, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_streak_length", Config.CLIENT.lensFlareStreakLength, 0.25, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_streak_width", Config.CLIENT.lensFlareStreakWidth, 0.25, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_chromatic_spread", Config.CLIENT.lensFlareChromaticSpread, 0.0, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_ghost_intensity", Config.CLIENT.lensFlareGhostIntensity, 0.0, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_ghost_size", Config.CLIENT.lensFlareGhostSize, 0.35, 3.0, 0.01))
                        .addOption(decimal(builder, "lens_flare_spread", Config.CLIENT.lensFlareSpread, 0.35, 3.0, 0.01)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("shared_quality"))
                        .addOption(decimal(builder, "ssr_quality", Config.CLIENT.ssrQuality, 0.25, 1.0, 0.01))
                        .addOption(decimal(builder, "post_effects_render_scale", Config.CLIENT.postEffectsRenderScale, 0.1, 1.0, 0.01)));
    }

    /**
     * Rain puddles and the global wet-film material. Both are screen-space passes that reuse the
     * already rendered scene color and depth instead of rendering the world a second time.
     */
    private static OptionPageBuilder surfacesPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.surfaces"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("wet"))
                        .addOption(alwaysOn(toggle(builder, "wet", Config.CLIENT.wetEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "wet_max_depth", Config.CLIENT.wetMaxDepth, 0.005, 0.2, 0.005), WET))
                        .addOption(under(decimal(builder, "wet_darkening", Config.CLIENT.wetDarkening, 0.0, 0.6, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_edge_softness", Config.CLIENT.wetEdgeSoftness, 0.0, 1.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_wave_strength", Config.CLIENT.wetWaveStrength, 0.0, 2.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_wave_scale", Config.CLIENT.wetWaveScale, 0.25, 10.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_wave_speed", Config.CLIENT.wetWaveSpeed, 0.0, 4.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_ripple_strength", Config.CLIENT.wetRippleStrength, 0.0, 2.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_refraction_strength", Config.CLIENT.wetRefractionStrength, 0.0, 4.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_sheen_floor", Config.CLIENT.wetSheenFloor, 0.0, 0.25, 0.01), WET)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("wet_reflections"))
                        .addOption(under(toggle(builder, "wet_ssr", Config.CLIENT.wetSsrEnabled), WET))
                        .addOption(under(decimal(builder, "wet_ssr_strength", Config.CLIENT.wetSsrStrength, 0.0, 2.0, 0.01), WET))
                        .addOption(under(decimal(builder, "wet_ssr_max_distance", Config.CLIENT.wetSsrMaxDistance, 4.0, 96.0, 1.0), WET)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("rain_puddles"))
                        .addOption(decimal(builder, "puddle_max_depth", Config.CLIENT.puddleMaxDepth, 0.005, 0.2, 0.005))
                        .addOption(decimal(builder, "puddle_depth_curve", Config.CLIENT.puddleDepthCurve, 0.35, 3.0, 0.01))
                        .addOption(decimal(builder, "puddle_wet_darkening", Config.CLIENT.puddleWetDarkening, 0.0, 0.6, 0.01))
                        .addOption(decimal(builder, "puddle_edge_softness", Config.CLIENT.puddleEdgeSoftness, 0.0, 1.0, 0.01))
                        .addOption(decimal(builder, "puddle_wave_strength", Config.CLIENT.puddleWaveStrength, 0.0, 2.0, 0.01))
                        .addOption(decimal(builder, "puddle_ripple_strength", Config.CLIENT.puddleRippleStrength, 0.0, 2.0, 0.01))
                        .addOption(decimal(builder, "puddle_refraction_strength", Config.CLIENT.puddleRefractionStrength, 0.0, 3.0, 0.01)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("puddle_reflections"))
                        .addOption(toggle(builder, "puddle_ssr", Config.CLIENT.puddleSsrEnabled))
                        .addOption(decimal(builder, "puddle_ssr_strength", Config.CLIENT.puddleSsrStrength, 0.0, 2.0, 0.01))
                        .addOption(decimal(builder, "puddle_ssr_max_distance", Config.CLIENT.puddleSsrMaxDistance, 4.0, 96.0, 1.0)));
    }

    /**
     * Per-vertex plant motion. The wave data is baked into the chunk mesh, which is why it survives
     * chunk rebuilds and works independently of the terrain shader.
     */
    private static OptionPageBuilder vegetationPage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.vegetation"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("plant_waves"))
                        .addOption(alwaysOn(toggle(builder, "plant_waves", Config.CLIENT.plantsWaveEnabled), OptionFlag.REQUIRES_ASSET_RELOAD))
                        .addOption(under(decimal(builder, "plant_wave_strength", Config.CLIENT.plantsWaveStrength, 0.0, 2.0, 0.01), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_speed", Config.CLIENT.plantsWaveSpeed, 0.0, 3.0, 0.01), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_gust", Config.CLIENT.plantsWaveGustStrength, 0.0, 2.0, 0.01), PLANT_WAVES))
                        .addOption(under(slider(builder, "plant_wave_distance", Config.CLIENT.plantsWaveDistance, 16, 192, 1), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_grass", Config.CLIENT.plantsWaveGrassStrength, 0.0, 2.0, 0.01), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_leaves", Config.CLIENT.plantsWaveLeavesStrength, 0.0, 2.0, 0.01), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_aquatic", Config.CLIENT.plantsWaveAquaticStrength, 0.0, 2.0, 0.01), PLANT_WAVES))
                        .addOption(under(decimal(builder, "plant_wave_bend", Config.CLIENT.plantsWaveBendStrength, 0.0, 2.0, 0.01), PLANT_WAVES)))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("terrain"))
                        .addOption(alwaysOn(toggle(builder, "opaque_ice", Config.CLIENT.opaqueIceEnabled), OptionFlag.REQUIRES_ASSET_RELOAD)));
    }

    private static OptionPageBuilder performancePage(ConfigBuilder builder) {
        return builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.performance"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(group("quality"))
                        .addOption(toggle(builder, "debug_hud", Config.CLIENT.debugHudEnabled, NO_FLAGS))
                        .addOption(toggle(builder, "block_light_test", Config.CLIENT.blockLightTestEnabled, OptionFlag.REQUIRES_RENDERER_RELOAD))
                        .addOption(slider(builder, "rtx_workers", Config.CLIENT.rtxWorldTracingWorkers, 1, Config.MAX_RTX_WORLD_TRACING_WORKERS, 1))
                        .addOption(slider(builder, "flood_radius", Config.CLIENT.floodRadiusCap, 16, 36, 1))
                        .addOption(slider(builder, "flood_update_budget", Config.CLIENT.floodUpdateBudget, 1, 64, 1))
                        .addOption(toggle(builder, "geometry_updates", Config.CLIENT.neoGpuVanillaGeometryUpdates, NO_FLAGS))
                        .addOption(toggle(builder, "debug", Config.CLIENT.neoGpuVanillaDebug, NO_FLAGS)));
    }

    private static BooleanOptionBuilder toggle(ConfigBuilder builder, String name, ForgeConfigSpec.BooleanValue value, OptionFlag... flags) {
        return builder.createBooleanOption(id(name))
                .setName(Component.translatable(OPTION + name))
                .setTooltip(Component.translatable(TOOLTIP + name))
                .setDefaultValue(value.getDefault())
                .setBinding(value::set, value::get)
                .setStorageHandler(value::save)
                .setFlags(flags);
    }

    private static BooleanOptionBuilder toggle(ConfigBuilder builder, String name, ForgeConfigSpec.BooleanValue value) {
        return toggle(builder, name, value, NO_FLAGS);
    }

    /**
     * Marks a feature master toggle. These never depend on another option because Sodium rejects
     * an option that declares itself as one of its own enable dependencies.
     */
    private static BooleanOptionBuilder alwaysOn(BooleanOptionBuilder option, OptionFlag flag) {
        return option;
    }

    private static BooleanOptionBuilder under(BooleanOptionBuilder option, Identifier requires) {
        return option.setEnabledProvider(state -> state.readBooleanOption(requires), MASTER_SWITCH, requires);
    }

    private static IntegerOptionBuilder slider(ConfigBuilder builder, String name, ForgeConfigSpec.IntValue value, int min, int max, int step) {
        return builder.createIntegerOption(id(name))
                .setName(Component.translatable(OPTION + name))
                .setTooltip(Component.translatable(TOOLTIP + name))
                .setDefaultValue(value.getDefault())
                .setRange(min, max, step)
                .setBinding(value::set, value::get)
                .setStorageHandler(value::save)
                .setValueFormatter(v -> Component.literal(Integer.toString(v)))
                .setEnabledProvider(state -> state.readBooleanOption(MASTER_SWITCH), MASTER_SWITCH);
    }

    private static IntegerOptionBuilder decimal(ConfigBuilder builder, String name, ForgeConfigSpec.DoubleValue value, double min, double max, double step) {
        return builder.createIntegerOption(id(name))
                .setName(Component.translatable(OPTION + name))
                .setTooltip(Component.translatable(TOOLTIP + name))
                .setDefaultValue(scale(value.getDefault()))
                .setRange(scale(min), scale(max), scale(step))
                .setBinding(v -> value.set(v / 100.0), () -> scale(value.get()))
                .setStorageHandler(value::save)
                .setValueFormatter(v -> Component.literal(String.format("%.2f", v / 100.0)))
                .setEnabledProvider(state -> state.readBooleanOption(MASTER_SWITCH), MASTER_SWITCH);
    }

    private static int scale(double value) {
        return (int)Math.round(value * 100.0);
    }

    private static IntegerOptionBuilder under(IntegerOptionBuilder option, Identifier requires) {
        return option.setEnabledProvider(state -> state.readBooleanOption(requires), MASTER_SWITCH, requires);
    }

    private static IntegerOptionBuilder percent(ConfigBuilder builder, String name, ForgeConfigSpec.DoubleValue value) {
        return builder.createIntegerOption(id(name))
                .setName(Component.translatable(OPTION + name))
                .setTooltip(Component.translatable(TOOLTIP + name))
                .setDefaultValue((int)Math.round(value.getDefault()))
                .setRange(0, 200, 1)
                .setBinding(v -> value.set(v.doubleValue()), () -> (int)Math.round(value.get()))
                .setStorageHandler(value::save)
                .setValueFormatter(v -> Component.literal(v + "%"))
                .setEnabledProvider(state -> state.readBooleanOption(MASTER_SWITCH), MASTER_SWITCH);
    }

    private static <E extends Enum<E>> EnumOptionBuilder<E> choice(ConfigBuilder builder, String name, ForgeConfigSpec.EnumValue<E> value, Class<E> type) {
        return builder.createEnumOption(id(name), type)
                .setName(Component.translatable(OPTION + name))
                .setTooltip(Component.translatable(TOOLTIP + name))
                .setDefaultValue(value.getDefault())
                .setElementNameProvider(v -> Component.translatable(OPTION + name + "." + v.name().toLowerCase(java.util.Locale.ROOT)))
                .setBinding(value::set, value::get)
                .setStorageHandler(value::save)
                .setEnabledProvider((ConfigState state) -> state.readBooleanOption(MASTER_SWITCH), MASTER_SWITCH);
    }

    private static <E extends Enum<E>> EnumOptionBuilder<E> under(EnumOptionBuilder<E> option, Identifier requires) {
        return option.setEnabledProvider(state -> state.readBooleanOption(requires), MASTER_SWITCH, requires);
    }

    private static Component group(String name) {
        return Component.translatable(GROUP + name);
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}