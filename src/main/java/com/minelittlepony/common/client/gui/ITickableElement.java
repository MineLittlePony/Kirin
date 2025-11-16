package com.minelittlepony.common.client.gui;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface ITickableElement extends GuiEventListener {
    void tick();
}
