package com.invadermonky.possiblybaubles.handlers;

import com.invadermonky.possiblybaubles.PossiblyBaubles;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DataFixerHandler implements IFixableData {
    public static final int DATA_FIXER_VERSION = 1;

    private final Map<ResourceLocation, ResourceLocation> ITEM_MAPPINGS = new HashMap<>();

    private DataFixerHandler() {
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "magnet_ring_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_suction_ring"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "battery_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_battery"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "battery_double_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_battery_double"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "battery_triple_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_battery_triple"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "battery_quadruple_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_battery_quadruple"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "battery_quintuple_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_battery_quintuple"));
        ITEM_MAPPINGS.put(new ResourceLocation(ActuallyAdditions.MODID, "potion_ring_advanced_bauble"), new ResourceLocation(ActuallyAdditions.MODID, "item_potion_ring_advanced"));
    }

    public static void init() {
        ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(PossiblyBaubles.MOD_ID, DATA_FIXER_VERSION);
        DataFixerHandler instance = new DataFixerHandler();
        MinecraftForge.EVENT_BUS.register(instance);
        modFixs.registerFix(FixTypes.ITEM_INSTANCE, instance);
    }

    @Override
    public int getFixVersion() {
        return DATA_FIXER_VERSION;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        return compound;
    }

    @SubscribeEvent
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for(RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = ITEM_MAPPINGS.get(oldName);
            if(newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if(newItem != null && newItem != Items.AIR) {
                    entry.remap(newItem);
                }
            }
        }
    }
}
