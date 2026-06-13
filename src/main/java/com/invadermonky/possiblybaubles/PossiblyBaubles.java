package com.invadermonky.possiblybaubles;

import com.invadermonky.possiblybaubles.client.KeybindOpenSack;
import com.invadermonky.possiblybaubles.handlers.ConfigHandlerPB;
import com.invadermonky.possiblybaubles.handlers.SackPickupEventHandler;
import com.invadermonky.possiblybaubles.inventory.GuiHandlerPB;
import com.invadermonky.possiblybaubles.network.PacketHandlerPB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = PossiblyBaubles.MOD_ID,
        name = PossiblyBaubles.MOD_NAME,
        version = PossiblyBaubles.MOD_VERSION,
        dependencies = PossiblyBaubles.DEPENDENCIES
)
public class PossiblyBaubles {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:actuallyadditions" +
            ";required-after:mixinbooter" +
            ";required-after:baubles";

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static PossiblyBaubles INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandlerPB.init();
        if(ConfigHandlerPB.TOGGLES.sacks) {
            MinecraftForge.EVENT_BUS.register(new SackPickupEventHandler());
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(ConfigHandlerPB.SETTINGS.sackKeybind) {
            NetworkRegistry.INSTANCE.registerGuiHandler(PossiblyBaubles.INSTANCE, new GuiHandlerPB());
        }
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void initClient(FMLInitializationEvent event) {
        if(ConfigHandlerPB.SETTINGS.sackKeybind) {
            KeybindOpenSack.initKeybind();
        }
    }
}
