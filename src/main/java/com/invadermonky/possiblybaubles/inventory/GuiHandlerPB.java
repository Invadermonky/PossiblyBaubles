package com.invadermonky.possiblybaubles.inventory;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.possiblybaubles.client.GuiBagPB;
import com.invadermonky.possiblybaubles.inventory.containers.ContainerBagPB;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandlerPB implements IGuiHandler {
    public static final int GUI_SACK = 0;

    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SACK) {
            ItemStack stack = this.getSackBaubleStack(player);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
                return new ContainerBagPB(stack, player.inventory, ((ItemBag) stack.getItem()).isVoid);
            }
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SACK) {
            ItemStack stack = this.getSackBaubleStack(player);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
                return new GuiBagPB(stack, player.inventory, ((ItemBag) stack.getItem()).isVoid);
            }
        }
        return null;
    }

    public ItemStack getSackBaubleStack(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
