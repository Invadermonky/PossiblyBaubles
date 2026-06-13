package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import de.ellpeck.actuallyadditions.mod.items.ItemBattery;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import de.ellpeck.actuallyadditions.mod.util.ItemUtil;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ItemBattery.class, remap = false)
public abstract class ItemBatteryBaubleMixin extends ItemEnergy implements IBauble {
    public ItemBatteryBaubleMixin(int maxPower, int transfer, String name) {
        super(maxPower, transfer, name);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        World world = entity.world;
        if (!world.isRemote && entity instanceof EntityPlayer && ItemUtil.isEnabled(stack)) {
            EntityPlayer player = (EntityPlayer)entity;

            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack slot = handler.getStackInSlot(i);
                this.franklyBaubles$rechargeItem(stack, slot);
            }

            for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack slot = player.inventory.getStackInSlot(i);
                this.franklyBaubles$rechargeItem(stack, slot);
            }
        }
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.CHARM;
    }

    @Unique
    private void franklyBaubles$rechargeItem(ItemStack battery, ItemStack toCharge) {
        if (StackUtil.isValid(toCharge) && toCharge.getCount() == 1) {
            int extractable = this.extractEnergy(battery, Integer.MAX_VALUE, true);
            int received = 0;
            if (toCharge.hasCapability(CapabilityEnergy.ENERGY, null)) {
                IEnergyStorage cap = toCharge.getCapability(CapabilityEnergy.ENERGY, null);
                if (cap != null) {
                    received = cap.receiveEnergy(extractable, false);
                }
            }

            if (received > 0) {
                this.extractEnergy(battery, received, false);
            }
        }
    }
}
