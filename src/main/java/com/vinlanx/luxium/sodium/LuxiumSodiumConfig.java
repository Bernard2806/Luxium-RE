package com.vinlanx.luxium.sodium;

import com.vinlanx.luxium.Config;
import com.vinlanx.luxium.LuxiumREMod;
import com.vinlanx.luxium.compat.ForgeConfigSpec;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;
import net.caffeinemc.mods.sodium.api.config.structure.BooleanOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@ConfigEntryPointForge(LuxiumREMod.MOD_ID)
public final class LuxiumSodiumConfig implements ConfigEntryPoint {
    private static final String NAMESPACE = LuxiumREMod.MOD_ID;

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        var page = builder.createOptionPage()
                .setName(Component.translatable("luxium_re.sodium.page.general"))
                .addOptionGroup(builder.createOptionGroup()
                        .setName(Component.translatable("luxium_re.sodium.group.rendering"))
                        .addOption(toggle(builder, "enabled", Config.CLIENT.luxiumEnabled))
                        .addOption(toggle(builder, "neo_gpu_vanilla", Config.CLIENT.neoGpuVanillaEnabled))
                        .addOption(toggle(builder, "gpu_lighting", Config.CLIENT.gpuShadowsEnabled))
                        .addOption(toggle(builder, "sky_lighting", Config.CLIENT.skyLightEnabled))
                        .addOption(toggle(builder, "sky_lighting_colors", Config.CLIENT.skyLightColorsEnabled))
                        .addOption(toggle(builder, "realistic_clouds", Config.CLIENT.cloudsEnabled))
                        .addOption(toggle(builder, "realistic_water", Config.CLIENT.waterEnabled))
                        .addOption(toggle(builder, "tonemap", Config.CLIENT.tonemapEnabled))
                        .addOption(toggle(builder, "screen_space_reflections", Config.CLIENT.reflectionEnabled)));

        builder.registerOwnModOptions()
                .setName("Luxium-RE")
                .addPage(page);
    }

    private static BooleanOptionBuilder toggle(ConfigBuilder builder, String name, ForgeConfigSpec.BooleanValue value) {
        Identifier id = Identifier.fromNamespaceAndPath(NAMESPACE, "sodium/" + name);
        return builder.createBooleanOption(id)
                .setName(Component.translatable("luxium_re.sodium.option." + name))
                .setTooltip(Component.translatable("luxium_re.sodium.tooltip." + name))
                .setDefaultValue(value.getDefault())
                .setBinding(value::set, value::get)
                .setStorageHandler(value::save);
    }
}
