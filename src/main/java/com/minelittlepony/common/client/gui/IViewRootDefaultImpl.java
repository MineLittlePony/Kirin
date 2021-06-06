package com.minelittlepony.common.client.gui;

import java.util.List;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.Padding;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

interface IViewRootDefaultImpl extends IViewRoot {
    @Override
    default Bounds getBounds() { throw new RuntimeException("stub"); }
    @Override
    default void setBounds(Bounds bounds) { throw new RuntimeException("stub"); }
    @Override
    default Padding getContentPadding() { throw new RuntimeException("stub"); }
    @Override
    default <T extends Element & Drawable & Selectable> List<Selectable> buttons() { throw new RuntimeException("stub"); }
    @Override
    default <T extends Element & Drawable & Selectable> T addButton(T button) { throw new RuntimeException("stub"); }
    @Override
    default List<Element> getChildElements() { throw new RuntimeException("stub"); }
}
