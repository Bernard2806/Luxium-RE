package com.vinlanx.luxiumre;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LuxiumRe.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = LuxiumRe.MOD_ID, value = Dist.CLIENT)
public class LuxiumReClient {
    public LuxiumReClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        LuxiumRe.LOGGER.info("Luxium RE client setup for {}", Minecraft.getInstance().getUser().getName());
    }
}
