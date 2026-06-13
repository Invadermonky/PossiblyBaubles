package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import de.ellpeck.actuallyadditions.mod.items.ItemGrowthRing;
import de.ellpeck.actuallyadditions.mod.items.base.ItemEnergy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemGrowthRing.class, remap = false)
public abstract class ItemGrowthRingBaubleMixin extends ItemEnergy implements IBauble {
    public ItemGrowthRingBaubleMixin(int maxPower, int transfer, String name) {
        super(maxPower, transfer, name);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        World world = entity.world;
        if (entity instanceof EntityPlayer && !world.isRemote && !entity.isSneaking()) {
            EntityPlayer player = (EntityPlayer)entity;
            int energyUse = 300;
            if (this.getEnergyStored(stack) >= energyUse) {
                List<BlockPos> blocks = new ArrayList<>();
                if (player.world.getTotalWorldTime() % 30L == 0L) {
                    int range = 3;

                    for(int x = -range; x < range + 1; ++x) {
                        for(int z = -range; z < range + 1; ++z) {
                            for(int y = -range; y < range + 1; ++y) {
                                int theX = MathHelper.floor(player.posX + x);
                                int theY = MathHelper.floor(player.posY + y);
                                int theZ = MathHelper.floor(player.posZ + z);
                                BlockPos posInQuestion = new BlockPos(theX, theY, theZ);
                                Block theBlock = world.getBlockState(posInQuestion).getBlock();
                                if ((theBlock instanceof IGrowable || theBlock instanceof IPlantable) && !(theBlock instanceof BlockGrass)) {
                                    blocks.add(posInQuestion);
                                }
                            }
                        }
                    }

                    if (!blocks.isEmpty()) {
                        for(int i = 0; i < 45 && this.getEnergyStored(stack) >= energyUse; ++i) {
                            BlockPos pos = blocks.get(world.rand.nextInt(blocks.size()));
                            IBlockState state = world.getBlockState(pos);
                            Block block = state.getBlock();
                            int metaBefore = block.getMetaFromState(state);
                            block.updateTick(world, pos, world.getBlockState(pos), world.rand);
                            IBlockState newState = world.getBlockState(pos);
                            if (newState.getBlock().getMetaFromState(newState) != metaBefore) {
                                world.playEvent(2005, pos, 0);
                            }

                            if (!player.capabilities.isCreativeMode) {
                                this.extractEnergyInternal(stack, energyUse, false);
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
