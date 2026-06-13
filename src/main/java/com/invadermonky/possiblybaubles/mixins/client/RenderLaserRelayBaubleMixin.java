package com.invadermonky.possiblybaubles.mixins.client;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.ellpeck.actuallyadditions.api.misc.IGoggles;
import de.ellpeck.actuallyadditions.mod.blocks.render.RenderLaserRelay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SideOnly(Side.CLIENT)
@Mixin(value = RenderLaserRelay.class, remap = false)
public class RenderLaserRelayBaubleMixin {
    @ModifyExpressionValue(
            method = "render(Lde/ellpeck/actuallyadditions/mod/tile/TileEntityLaserRelay;DDDFIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/ellpeck/actuallyadditions/mod/items/ItemEngineerGoggles;isWearing(Lnet/minecraft/entity/player/EntityPlayer;)Z"
            )
    )
    private boolean isWearingBaubleMixin(boolean original, @Local(ordinal = 0) EntityPlayer player) {
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
