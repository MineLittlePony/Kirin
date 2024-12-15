package com.minelittlepony.common.client.gui;

import java.util.List;

import org.apache.logging.log4j.util.InternalApi;
import org.spongepowered.asm.mixin.throwables.MixinException;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;

@InternalApi
public interface IViewRootDefaultImpl extends IViewRoot, ITextContext {
    @Override
    default Bounds getBounds() { throw applicationFailed(); }
    @Override
    default void setBounds(Bounds bounds) { }
    @Override
    default Padding getContentPadding() { throw applicationFailed(); }
    @Override
    default <T extends Element & Drawable & Selectable> List<Selectable> buttons() { throw applicationFailed(); }
    @Override
    default <T extends Element & Drawable & Selectable> T addButton(T button) { throw applicationFailed(); }

    @SuppressWarnings("unchecked")
    @Override
    default List<Element> getChildElements() {
        return (List<Element>) ((Screen) this).children();
    }

    private static MixinException applicationFailed() {
        return new MixinException("Mixin com.minelittlepony.common.mixin.MixinScreen was not applied");
    }
}
