package com.minelittlepony.common.client.gui;

import java.util.List;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;

public interface IViewRootDefaultImpl extends IViewRoot, ITextContext {

    @Override
    default Bounds getBounds() { throw new RuntimeException("stub"); }

    @Override
    default void setBounds(Bounds bounds) { }

    @Override
    default Padding getContentPadding() { throw new RuntimeException("stub"); }

    @Override
    default <T extends Element & Drawable & Selectable> List<Selectable> buttons() { throw new RuntimeException("stub"); }

    @Override
    default <T extends Element & Drawable & Selectable> T addButton(T button) { return button; }

    @Override
    @SuppressWarnings("unchecked")
    default List<Element> getChildElements() {
        return (List<Element>) ((Screen)this).children();
    }
}
