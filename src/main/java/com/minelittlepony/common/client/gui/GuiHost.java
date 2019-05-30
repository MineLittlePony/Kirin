package com.minelittlepony.common.client.gui;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Host GUI for rendering content independent of itself.
 *
 * Useful for supporting stuff like LiteLoader's custom panel,
 * though ultimately pointless if LiteLoader does end up biting the dust.
 */
public class GuiHost extends GameGui {

    private final IGuiGuest guest;

    public GuiHost(IGuiGuest guest) {
    	super(new TranslatableComponent(guest.getTitle()));
        this.guest = guest;
    }

    @Override
    public void init() {
        guest.initGui(this);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (guest.render(this, mouseX, mouseY, partialTicks)) {
            super.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        guest.onGuiClosed(this);
    }

    @Override
    public <T extends AbstractButtonWidget> T addButton(T button) {
        return super.addButton(button);
    }

    public boolean mustScroll() {
        return false;
    }
}
