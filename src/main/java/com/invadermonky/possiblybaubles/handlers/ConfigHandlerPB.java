package com.invadermonky.possiblybaubles.handlers;

import com.invadermonky.possiblybaubles.PossiblyBaubles;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = PossiblyBaubles.MOD_ID)
public class ConfigHandlerPB {
    public static final ConfigToggles TOGGLES = new ConfigToggles();
    public static final ConfigSettings SETTINGS = new ConfigSettings();

    public static class ConfigToggles {
        @Config.RequiresMcRestart
        @Config.Name("Battery")
        @Config.Comment("Allows all battery variants to be equipped as charm baubles.")
        public boolean battery = true;

        @Config.RequiresMcRestart
        @Config.Name("Engineer's Goggles")
        @Config.Comment("Allows the Engineer's Goggles and Engineer's Infrared Goggles to be equipped as head baubles.")
        public boolean goggles = true;

        @Config.RequiresMcRestart
        @Config.Name("Liquid Banning Ring")
        @Config.Comment("Allows the liquid banning ring to be equipped as a ring bauble.")
        public boolean liquidBanningRing = true;

        @Config.RequiresMcRestart
        @Config.Name("Potion Rings")
        @Config.Comment("Allows Potion Rings to be equipped as ring baubles. See SETTINGS for additional options.")
        public boolean potionRing = true;

        @Config.RequiresMcRestart
        @Config.Name("Ring of Growth")
        @Config.Comment("Allows the Ring of Growth to be equipped as a ring bauble.")
        public boolean growthRing = true;

        @Config.RequiresMcRestart
        @Config.Name("Ring of Magnetizing")
        @Config.Comment("Allows the Ring of Magnetizing to be equipped as a ring bauble.")
        public boolean magnetRing = true;

        @Config.RequiresMcRestart
        @Config.Name("Sacks")
        @Config.Comment("Allows the Traveller's Sack and Void Sack to be equipped as belt baubles.")
        public boolean sacks = true;

        @Config.RequiresMcRestart
        @Config.Name("Wings Of The Bat")
        @Config.Comment("Allows the Wings of the Bat to be equipped as a body bauble.")
        public boolean wingsOfTheBat = true;
    }

    public static class ConfigSettings {
        @Config.Name("Potion Ring - Advanced Only")
        @Config.Comment("Only the advanced variants of the potion rings will provide their effects when equipped.")
        public boolean potionRingAdvancedOnly = true;

        @Config.RequiresMcRestart
        @Config.Name("Open Sack Keybind")
        @Config.Comment("Adds a keybind that allows players to open Sacks equipped in baubles slots.")
        public boolean sackKeybind = true;
    }

    @Mod.EventBusSubscriber(modid = PossiblyBaubles.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(PossiblyBaubles.MOD_ID)) {
                ConfigManager.sync(PossiblyBaubles.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
