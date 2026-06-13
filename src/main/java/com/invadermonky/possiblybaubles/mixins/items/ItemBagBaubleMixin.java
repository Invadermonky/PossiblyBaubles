package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemBag.class, remap = false)
public class ItemBagBaubleMixin implements IBauble {
    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BELT;
    }
}
