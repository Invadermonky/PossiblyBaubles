package com.invadermonky.possiblybaubles.network.messages;

import baubles.api.BaublesApi;
import com.invadermonky.possiblybaubles.PossiblyBaubles;
import com.invadermonky.possiblybaubles.inventory.GuiHandlerPB;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOpenBagGui implements IMessage {
    public static final int TRAVELLERS = 0;
    public static final int VOID = 1;

    private int baubleSlot;

    public MessageOpenBagGui(int baubleSlot) {
        this.baubleSlot = baubleSlot;
    }

    public MessageOpenBagGui() {
        this(-1);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.baubleSlot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.baubleSlot);
    }

    public static class Handler implements IMessageHandler<MessageOpenBagGui, IMessage> {
        @Override
        public IMessage onMessage(MessageOpenBagGui message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                EntityPlayer player = ctx.getServerHandler().player;
                if(player != null && message.baubleSlot > -1) {
                    ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(message.baubleSlot);
                    if(!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
                        player.openGui(
                                PossiblyBaubles.INSTANCE,
                                GuiHandlerPB.GUI_SACK,
                                player.world,
                                player.getPosition().getX(),
                                player.getPosition().getY(),
                                player.getPosition().getZ()
                        );
                    }
                }
            });
            return null;
        }
    }
}
