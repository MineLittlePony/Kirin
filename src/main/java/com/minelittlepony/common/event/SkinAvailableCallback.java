package com.minelittlepony.common.event;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

public interface SkinAvailableCallback {

    Event<SkinAvailableCallback> EVENT = EventFactory.createArrayBacked(SkinAvailableCallback.class, listeners -> (type, id, texture) -> {
        for (SkinAvailableCallback event : listeners) {
            event.onSkinAvailable(type, id, texture);
        }
    });

    void onSkinAvailable(Type type, Identifier id, MinecraftProfileTexture texture);

}
