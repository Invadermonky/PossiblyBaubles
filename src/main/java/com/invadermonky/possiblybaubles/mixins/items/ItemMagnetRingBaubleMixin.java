package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import de.ellpeck.actuallyadditions.mod.items.ItemMagnetRing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemMagnetRing.class, remap = false)
public abstract class ItemMagnetRingBaubleMixin implements IBauble {
    @Shadow(remap = true)
    public abstract void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5);

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        this.onUpdate(stack, player.world, player, 0, false);
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }
}
