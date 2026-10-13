package com.client.nocturnal;

import com.client.nocturnal.ui.NocturnalScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputConstants;
import org.lwjgl.glfw.GLFW;

public final class NocturnalClient implements ClientModInitializer {
    private KeyMapping openGui;

    @Override
    public void onInitializeClient() {
        openGui = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.nocturnal.open_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.consumeClick()) {
                if (client.screen instanceof NocturnalScreen) {
                    client.setScreen(null);
                } else if (client.screen == null) {
                    client.setScreen(new NocturnalScreen());
                }
            }
        });
    }
}
