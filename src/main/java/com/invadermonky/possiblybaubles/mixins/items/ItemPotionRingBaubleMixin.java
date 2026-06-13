package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.invadermonky.possiblybaubles.handlers.ConfigHandlerPB;
import de.ellpeck.actuallyadditions.mod.items.ItemPotionRing;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemPotionRing.class, remap = false)
public abstract class ItemPotionRingBaubleMixin implements IBauble {
    @Shadow @Final private boolean isAdvanced;

    @Shadow
    protected abstract boolean effectEntity(EntityLivingBase thePlayer, ItemStack stack, boolean canUseBasic);

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        World world = player.world;
        if (!world.isRemote && stack.getItemDamage() < ItemPotionRing.ALL_RINGS.length && player instanceof EntityPlayer) {
            EntityPlayer thePlayer = (EntityPlayer) player;
            int storedBlaze = ItemPotionRing.getStoredBlaze(stack);
            if (storedBlaze > 0) {
                boolean shouldActivate = StackUtil.isValid(stack) && (!ConfigHandlerPB.SETTINGS.potionRingAdvancedOnly || this.isAdvanced) && world.getTotalWorldTime() % 10L == 0;
                if (this.effectEntity(thePlayer, stack, shouldActivate)) {
                    ItemPotionRing.setStoredBlaze(stack, storedBlaze - 1);
                }
            }
        }
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
