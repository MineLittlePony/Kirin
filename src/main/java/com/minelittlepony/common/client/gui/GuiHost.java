package com.minelittlepony.common.client.gui;

import net.minecraft.client.gui.GuiButton;

/**
 * Host GUI for rendering content independent of itself.
 *
 * Useful for supporting stuff like LiteLoader's custom panel,
 * though ultimately pointless if LiteLoader does end up biting the dust.
 */
public class GuiHost extends GameGui {

    private final IGuiGuest guest;

    public GuiHost(IGuiGuest guest) {
        this.guest = guest;
    }

    @Override
    public void initGui() {
        guest.initGui(this);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (guest.render(this, mouseX, mouseY, partialTicks)) {
            super.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onGuiClosed() {
        guest.onGuiClosed(this);
    }

    public String getTitle() {
        return guest.getTitle();
    }

    @Override
    public <T extends GuiButton> T addButton(T button) {
        return super.addButton(button);
    }

    public boolean mustScroll() {
        return false;
    }
}
