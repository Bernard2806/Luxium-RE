package com.vinlanx.luxiumre;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block during common setup")
            .define("logDirtBlock", false);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("Example integer config value")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("Example list of item identifiers")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "",
                    Config::isRegisteredItem);

    static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }

    private static boolean isRegisteredItem(Object value) {
        return value instanceof String itemId
                && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemId));
    }
}
