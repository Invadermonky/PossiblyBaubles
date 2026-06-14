package com.invadermonky.possiblybaubles.client;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.invadermonky.possiblybaubles.PossiblyBaubles;
import com.invadermonky.possiblybaubles.handlers.ConfigHandlerPB;
import com.invadermonky.possiblybaubles.network.PacketHandlerPB;
import com.invadermonky.possiblybaubles.network.messages.MessageOpenBagGui;
import com.invadermonky.possiblybaubles.network.messages.MessageToggleMagnets;
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
public class KeybindsPB {
    private static final String KEY_CATEGORY = "key.category." + PossiblyBaubles.MOD_ID;
    public static KeyBinding openSackGui;
    public static KeyBinding toggleMagnet;

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player == null) return;

        if(openSackGui != null && openSackGui.isPressed()) {
            attemptOpenBagGui(player);
        }

        if(toggleMagnet != null && toggleMagnet.isPressed()) {
            attemptToggleMagnet();
        }
    }

    private static void attemptOpenBagGui(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if(!stack.isEmpty() && (stack.getItem() == InitItems.itemBag || stack.getItem() == InitItems.itemVoidBag)) {
                PacketHandlerPB.INSTANCE.sendToServer(new MessageOpenBagGui(slot));
                return;
            }
        }
    }

    private static void attemptToggleMagnet() {
        PacketHandlerPB.INSTANCE.sendToServer(new MessageToggleMagnets());
    }

    public static void initKeybinds() {
        boolean register = false;
        if(ConfigHandlerPB.SETTINGS.openSackKeybind && ConfigHandlerPB.TOGGLES.sacks) {
            register = true;
            openSackGui = new KeyBinding("key." + PossiblyBaubles.MOD_ID + ".open_sack", new IKeyConflictContext() {
                @Override
                public boolean isActive() {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    if (player != null) {
                        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack stack = handler.getStackInSlot(i);
                            if (!stack.isEmpty() && stack.getItem() instanceof ItemBag) {
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
        }

        if(ConfigHandlerPB.SETTINGS.toggleMagnetKeybind) {
            register = true;
            toggleMagnet = new KeyBinding("key." + PossiblyBaubles.MOD_ID + ".toggle_magnet", new IKeyConflictContext() {
                @Override
                public boolean isActive() {
                    return Minecraft.getMinecraft().player != null;
                }

                @Override
                public boolean conflicts(IKeyConflictContext other) {
                    return this.isActive() && other.isActive();
                }
            }, Keyboard.KEY_N, KEY_CATEGORY);
            ClientRegistry.registerKeyBinding(toggleMagnet);
        }

        if(register) {
            MinecraftForge.EVENT_BUS.register(KeybindsPB.class);
        }
    }
}
