package com.vinlanx.luxium;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LuxiumREMod.MOD_ID, dist = Dist.CLIENT)
public final class LuxiumREModClient {
    public LuxiumREModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> new ConfigurationScreen(container, parent));
    }
}
