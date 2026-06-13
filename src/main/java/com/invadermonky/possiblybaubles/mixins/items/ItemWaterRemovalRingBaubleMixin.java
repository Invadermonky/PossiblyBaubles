package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import de.ellpeck.actuallyadditions.mod.items.ItemWaterRemovalRing;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemWaterRemovalRing.class, remap = false)
public abstract class ItemWaterRemovalRingBaubleMixin extends ItemEnergy implements IBauble {
    public ItemWaterRemovalRingBaubleMixin(int maxPower, int transfer, String name) {
        super(maxPower, transfer, name);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        World world = entity.world;
        if (entity instanceof EntityPlayer && !world.isRemote && !entity.isSneaking()) {
            EntityPlayer player = (EntityPlayer)entity;
            int energyUse = 150;
            if (this.getEnergyStored(stack) >= energyUse) {
                int range = 3;

                for(int x = -range; x < range + 1; ++x) {
                    for(int z = -range; z < range + 1; ++z) {
                        for(int y = -range; y < range + 1; ++y) {
                            int theX = MathHelper.floor(player.posX + x);
                            int theY = MathHelper.floor(player.posY + y);
                            int theZ = MathHelper.floor(player.posZ + z);
                            BlockPos pos = new BlockPos(theX, theY, theZ);
                            Block block = world.getBlockState(pos).getBlock();
                            if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && this.getEnergyStored(stack) >= energyUse) {
                                world.setBlockToAir(pos);
                                if (!player.capabilities.isCreativeMode) {
                                    this.extractEnergyInternal(stack, energyUse, false);
                                }
                            } else if ((block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) && this.getEnergyStored(stack) >= energyUse * 2) {
                                world.setBlockToAir(pos);
                                if (!player.capabilities.isCreativeMode) {
                                    this.extractEnergyInternal(stack, energyUse * 2, false);
                                }
                            }
                        }
                    }
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
