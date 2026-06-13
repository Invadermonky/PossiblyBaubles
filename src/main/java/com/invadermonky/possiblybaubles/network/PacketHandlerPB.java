package com.invadermonky.possiblybaubles.network;

import com.invadermonky.possiblybaubles.PossiblyBaubles;
import com.invadermonky.possiblybaubles.network.messages.MessageOpenBagGui;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandlerPB {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(PossiblyBaubles.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(MessageOpenBagGui.Handler.class, MessageOpenBagGui.class, 0, Side.SERVER);
    }
}
