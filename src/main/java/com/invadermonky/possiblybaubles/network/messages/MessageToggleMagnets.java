package com.invadermonky.possiblybaubles.network.messages;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import de.ellpeck.actuallyadditions.mod.items.ItemMagnetRing;
import de.ellpeck.actuallyadditions.mod.util.ItemUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageToggleMagnets implements IMessage {

    public MessageToggleMagnets() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<MessageToggleMagnets, IMessage> {

        @Override
        public IMessage onMessage(MessageToggleMagnets message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                EntityPlayer player = ctx.getServerHandler().player;
                //Toggle Baubles
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                for(int slot = 0; slot < handler.getSlots(); slot++) {
                    ItemStack stack = handler.getStackInSlot(slot);
                    if(!stack.isEmpty() && stack.getItem() instanceof ItemMagnetRing) {
                        ItemUtil.changeEnabled(stack);
                    }
                }
                //Toggle Inventory
                for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
                    ItemStack stack = player.inventory.getStackInSlot(slot);
                    if(!stack.isEmpty() && stack.getItem() instanceof ItemMagnetRing) {
                        ItemUtil.changeEnabled(stack);
                    }
                }
            });
            return null;
        }
    }
}
