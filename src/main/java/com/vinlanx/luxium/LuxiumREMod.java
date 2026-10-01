package com.vinlanx.luxium;

import com.vinlanx.luxium.Config;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(LuxiumREMod.MOD_ID)
public final class LuxiumREMod {
    public static final String MOD_ID = "luxium_re";

    public LuxiumREMod(ModContainer container) {
        Config.register(container);
    }
}
