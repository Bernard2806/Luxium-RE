package com.vinlanx.luxium;

import com.vinlanx.luxium.Config;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(LuxiumREMod.MOD_ID)
public final class LuxiumREMod {
    public static final String MOD_ID = "luxium_re";
    public static final String VERSION = "2.8.0-pre-alpha";

    public LuxiumREMod(ModContainer container) {
        Config.register(container);
    }
}
