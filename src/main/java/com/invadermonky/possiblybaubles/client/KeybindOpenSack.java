package com.invadermonky.possiblybaubles.client;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.possiblybaubles.PossiblyBaubles;
import com.invadermonky.possiblybaubles.network.PacketHandlerPB;
import com.invadermonky.possiblybaubles.network.messages.MessageOpenBagGui;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import de.ellpeck.actuallyadditions.mod.items.ItemBag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeybindOpenSack {
    private static final String KEY_CATEGORY = "key.category." + PossiblyBaubles.MOD_ID;
    public static KeyBinding openSackGui;

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player != null && openSackGui.isPressed()) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            for(int slot = 0; slot < handler.getSlots(); slot++) {
                ItemStack stack = handler.getStackInSlot(slot);
                if(!stack.isEmpty() && (stack.getItem() == InitItems.itemBag || stack.getItem() == InitItems.itemVoidBag)) {
                    PacketHandlerPB.INSTANCE.sendToServer(new MessageOpenBagGui(slot));
                    return;
                }
            }
        }
    }

    public static void initKeybind() {
        openSackGui = new KeyBinding("key." + PossiblyBaubles.MOD_ID + ".open_sack", new IKeyConflictContext() {
            @Override
            public boolean isActive() {
                EntityPlayer player = Minecraft.getMinecraft().player;
                if(player != null) {
                    IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                    for(int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if(!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean conflicts(IKeyConflictContext other) {
                return this.isActive() && other.isActive();
            }
        }, Keyboard.KEY_V, KEY_CATEGORY);

        ClientRegistry.registerKeyBinding(openSackGui);
        MinecraftForge.EVENT_BUS.register(KeybindOpenSack.class);
    }
}
