package com.invadermonky.possiblybaubles.mixins.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import de.ellpeck.actuallyadditions.api.misc.IGoggles;
import de.ellpeck.actuallyadditions.mod.items.ItemEngineerGoggles;
import de.ellpeck.actuallyadditions.mod.items.base.ItemArmorAA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
@Mixin(value = ItemEngineerGoggles.class, remap = false)
public abstract class ItemEngineerGogglesBaubleMixin extends ItemArmorAA implements IBauble {
    @Shadow @Final private Set<Entity> cachedGlowingEntities;

    public ItemEngineerGogglesBaubleMixin(String name, ArmorMaterial material, int type, ItemStack repairItem) {
        super(name, material, type, repairItem);
    }

    @Inject(
            method = "onClientTick",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;isEmpty()Z",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void onClientBaubleTickMixin(TickEvent.ClientTickEvent event, CallbackInfo ci, @Local(ordinal = 0)EntityPlayer player) {
        if(player != null) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if(!stack.isEmpty() && stack.getItem() instanceof IGoggles && ((IGoggles) stack.getItem()).displaySpectralMobs()) {
                    double range = 8.0;
                    AxisAlignedBB aabb = new AxisAlignedBB(player.getPosition()).grow(range);
                    List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class, aabb);
                    if (!entities.isEmpty()) {
                        this.cachedGlowingEntities.addAll(entities);
                    }

                    if (!this.cachedGlowingEntities.isEmpty()) {
                        for(Entity entity : this.cachedGlowingEntities) {
                            if (!entity.isDead && !(entity.getDistanceSq(player.posX, player.posY, player.posZ) > range * range)) {
                                entity.setGlowing(true);
                            } else {
                                entity.setGlowing(false);
                                this.cachedGlowingEntities.remove(entity);
                            }
                        }
                    }
                    ci.cancel();
                }
            }
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.HEAD;
    }
}
