package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import de.ellpeck.actuallyadditions.mod.items.ItemWingsOfTheBats;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemWingsOfTheBats.class, remap = false)
public class ItemWingsOfTheBatsBaubleMixin implements IBauble {
    @Inject(method = "getWingItem", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void getBaubleWingItemMixin(EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(StackUtil.isValid(stack) && stack.getItem() instanceof ItemWingsOfTheBats) {
                cir.setReturnValue(stack);
                return;
            }
        }
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BODY;
    }
}
