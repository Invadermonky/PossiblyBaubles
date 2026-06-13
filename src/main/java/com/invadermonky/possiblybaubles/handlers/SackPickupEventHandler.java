package com.invadermonky.possiblybaubles.handlers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerBag;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import de.ellpeck.actuallyadditions.mod.items.ItemDrill;
import de.ellpeck.actuallyadditions.mod.tile.FilterSettings;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA;
import de.ellpeck.actuallyadditions.mod.util.ItemUtil;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SackPickupEventHandler {
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        if(this.validateEvent(event)) {
            EntityPlayer player = event.getEntityPlayer();
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            ItemStack stack = event.getItem().getItem();
            ItemStack copy = stack.copy();
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack baubleStack = handler.getStackInSlot(i);
                if(this.isActiveSack(baubleStack)) {
                    boolean changed = false;
                    boolean isVoid = ((ItemBag)baubleStack.getItem()).isVoid;
                    ItemStackHandlerAA inv = new ItemStackHandlerAA(ContainerBag.getSlotAmount(isVoid));
                    ItemDrill.loadSlotsFromNBT(inv, baubleStack);
                    FilterSettings filter = new FilterSettings(4, false, false, false, false, 0, 0);
                    filter.readFromNBT(baubleStack.getTagCompound(), "Filter");
                    if (filter.check(stack)) {
                        if (isVoid) {
                            stack.setCount(0);
                            changed = true;
                        } else {
                            for(int j = 0; j < inv.getSlots(); ++j) {
                                ItemStack bagStack = inv.getStackInSlot(j);
                                if (StackUtil.isValid(bagStack)) {
                                    if (ItemUtil.canBeStacked(bagStack, stack)) {
                                        int maxTransfer = Math.min(stack.getCount(), stack.getMaxStackSize() - bagStack.getCount());
                                        if (maxTransfer > 0) {
                                            inv.setStackInSlot(j, StackUtil.grow(bagStack, maxTransfer));
                                            stack.shrink(maxTransfer);
                                            changed = true;
                                        }
                                    }
                                } else {
                                    inv.setStackInSlot(j, stack.copy());
                                    stack.setCount(0);
                                    changed = true;
                                }

                                if (!StackUtil.isValid(stack)) {
                                    break;
                                }
                            }
                        }
                    }

                    if (changed) {
                        if (!isVoid) {
                            ItemDrill.writeSlotsToNBT(inv, baubleStack);
                        }

                        event.setResult(Event.Result.ALLOW);
                    }
                }

                if(stack.isEmpty()) {
                    break;
                }
            }

            if(!ItemStack.areItemStacksEqual(stack, copy)) {
                event.getItem().setItem(stack);
            }
        }
    }

    private boolean validateEvent(EntityItemPickupEvent event) {
        EntityItem entityItem = event.getItem();
        ItemStack stack = entityItem.getItem();
        return !event.isCanceled()
                && event.getResult() != Event.Result.ALLOW
                && entityItem.isEntityAlive()
                && !stack.isEmpty();
    }

    private boolean isActiveSack(ItemStack stack) {
        return !stack.isEmpty()
                && stack.getItem() instanceof ItemBag
                && stack.getTagCompound() != null
                && stack.getTagCompound().getBoolean("AutoInsert");
    }
}
