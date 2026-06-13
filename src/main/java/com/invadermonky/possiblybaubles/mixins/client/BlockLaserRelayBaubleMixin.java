package com.invadermonky.possiblybaubles.mixins.client;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.ellpeck.actuallyadditions.api.misc.IGoggles;
import de.ellpeck.actuallyadditions.mod.blocks.BlockLaserRelay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SideOnly(Side.CLIENT)
@Mixin(value = BlockLaserRelay.class, remap = false)
public class BlockLaserRelayBaubleMixin {
    @ModifyExpressionValue(
            method = "displayHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/ellpeck/actuallyadditions/mod/items/ItemEngineerGoggles;isWearing(Lnet/minecraft/entity/player/EntityPlayer;)Z"
            )
    )
    private boolean isWearingBaubleMixin(boolean original, @Local(argsOnly = true)EntityPlayer player) {
        if(!original) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if(!stack.isEmpty() && stack.getItem() instanceof IGoggles) {
                    return true;
                }
            }
        }
        return original;
    }
}
