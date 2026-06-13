package com.invadermonky.possiblybaubles.inventory.containers;

import de.ellpeck.actuallyadditions.mod.inventory.ContainerBag;
import de.ellpeck.actuallyadditions.mod.inventory.slot.SlotDeletion;
import de.ellpeck.actuallyadditions.mod.inventory.slot.SlotFilter;
import de.ellpeck.actuallyadditions.mod.inventory.slot.SlotItemHandlerUnconditioned;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import de.ellpeck.actuallyadditions.mod.items.ItemDrill;
import de.ellpeck.actuallyadditions.mod.network.gui.IButtonReactor;
import de.ellpeck.actuallyadditions.mod.tile.FilterSettings;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class ContainerBagPB extends Container implements IButtonReactor {
    public final FilterSettings filter = new FilterSettings(4, false, true, false, false, 0, -1000);
    private final ItemStackHandlerAA bagInventory;
    private final InventoryPlayer inventory;
    private final boolean isVoid;
    public boolean autoInsert;
    private boolean oldAutoInsert;
    private final ItemStack sack;

    public ContainerBagPB(ItemStack sack, InventoryPlayer inventory, boolean isVoid) {
        this.inventory = inventory;
        this.bagInventory = new ItemStackHandlerAA(ContainerBag.getSlotAmount(isVoid), (slot, stackx, automation) -> !ContainerBag.isBlacklisted(stackx), ItemStackHandlerAA.REMOVE_TRUE);
        this.isVoid = isVoid;
        this.sack = sack;

        this.bindBagInventory();
        this.bindPlayerInventory();
    }

    public void bindBagInventory() {
        for(int i = 0; i < 4; ++i) {
            this.addSlotToContainer(new SlotFilter(this.filter, i, 155, 10 + i * 18));
        }

        if (this.isVoid) {
            this.addSlotToContainer(new SlotDeletion(this.bagInventory, 0, 64, 65) {
                public boolean isItemValid(ItemStack stack) {
                    return filter.check(stack);
                }
            });
        } else {
            for(int i = 0; i < 4; ++i) {
                for(int j = 0; j < 7; ++j) {
                    this.addSlotToContainer(new SlotItemHandlerUnconditioned(this.bagInventory, j + i * 7, 10 + j * 18, 10 + i * 18) {
                        public boolean isItemValid(ItemStack stack) {
                            return !ContainerBag.isBlacklisted(stack) && filter.check(stack);
                        }
                    });
                }
            }
        }

        if (StackUtil.isValid(this.sack) && this.sack.getItem() instanceof ItemBag) {
            ItemDrill.loadSlotsFromNBT(this.bagInventory, this.sack);
            NBTTagCompound compound = this.sack.getTagCompound();
            if (compound != null) {
                this.filter.readFromNBT(compound, "Filter");
                this.autoInsert = compound.getBoolean("AutoInsert");
            }
        }
    }

    public void bindPlayerInventory() {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.inventory, j + i * 9 + 9, 8 + j * 18, 94 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.inventory, i, 8 + i * 18, 152));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.filter.needsUpdateSend() || this.autoInsert != this.oldAutoInsert) {
            for(IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(this, 0, this.filter.isWhitelist ? 1 : 0);
                listener.sendWindowProperty(this, 1, this.filter.respectMeta ? 1 : 0);
                listener.sendWindowProperty(this, 2, this.filter.respectNBT ? 1 : 0);
                listener.sendWindowProperty(this, 3, this.filter.respectOredict);
                listener.sendWindowProperty(this, 4, this.autoInsert ? 1 : 0);
                listener.sendWindowProperty(this, 5, this.filter.respectMod ? 1 : 0);
            }

            this.filter.updateLasts();
            this.oldAutoInsert = this.autoInsert;
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.filter.isWhitelist = data == 1;
        } else if (id == 1) {
            this.filter.respectMeta = data == 1;
        } else if (id == 2) {
            this.filter.respectNBT = data == 1;
        } else if (id == 3) {
            this.filter.respectOredict = data;
        } else if (id == 4) {
            this.autoInsert = data == 1;
        } else if (id == 5) {
            this.filter.respectMod = data == 1;
        }

    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int slot) {
        int inventoryStart = this.bagInventory.getSlots() + 4;
        int inventoryEnd = inventoryStart + 26;
        int hotbarStart = inventoryEnd + 1;
        int hotbarEnd = hotbarStart + 8;
        Slot theSlot = this.inventorySlots.get(slot);
        if (theSlot != null && theSlot.getHasStack()) {
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();
            if (slot >= inventoryStart) {
                if (this.isVoid || !this.filter.check(newStack) || !this.mergeItemStack(newStack, 4, 32, false)) {
                    if (slot >= inventoryStart && slot <= inventoryEnd) {
                        if (!this.mergeItemStack(newStack, hotbarStart, hotbarEnd + 1, false)) {
                            return StackUtil.getEmpty();
                        }
                    } else if (slot >= inventoryEnd + 1 && slot < hotbarEnd + 1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd + 1, false)) {
                        return StackUtil.getEmpty();
                    }
                }
            } else if (!this.mergeItemStack(newStack, inventoryStart, hotbarEnd + 1, false)) {
                return StackUtil.getEmpty();
            }

            if (!StackUtil.isValid(newStack)) {
                theSlot.putStack(StackUtil.getEmpty());
            } else {
                theSlot.onSlotChanged();
            }

            if (newStack.getCount() == currentStack.getCount()) {
                return StackUtil.getEmpty();
            } else {
                theSlot.onTake(player, newStack);
                return currentStack;
            }
        } else {
            return StackUtil.getEmpty();
        }
    }

    @Override
    public @NotNull ItemStack slotClick(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull EntityPlayer player) {
        if (SlotFilter.checkFilter(this, slotId, player)) {
            return StackUtil.getEmpty();
        } else {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    public void onContainerClosed(@NotNull EntityPlayer player) {
        if (StackUtil.isValid(this.sack) && this.sack.getItem() instanceof ItemBag) {
            ItemDrill.writeSlotsToNBT(this.bagInventory, this.sack);
            NBTTagCompound compound = this.sack.getTagCompound();
            if(compound != null) {
                this.filter.writeToNBT(compound, "Filter");
                compound.setBoolean("AutoInsert", this.autoInsert);
            }
        }
        super.onContainerClosed(player);
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return !this.sack.isEmpty() && this.sack.getItem() instanceof ItemBag;
    }

    @Override
    public void onButtonPressed(int buttonID, EntityPlayer player) {
        if (buttonID == 0) {
            this.autoInsert = !this.autoInsert;
        } else {
            this.filter.onButtonPressed(buttonID);
        }
    }
}
