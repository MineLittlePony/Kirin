package com.minelittlepony.common.client.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.dimension.Padding;
import com.minelittlepony.common.event.ScreenInitCallback;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

public interface IViewRoot extends IBounded, ScreenInitCallback.ButtonList {
    /**
     * Gets the total bounds of all the elements inside this container.
     */
    default Bounds getContentBounds() {
        return getAllBounds().stream().reduce(Bounds.empty(), Bounds::add).add(getContentPadding());
    }

    /**
     * Gets all the bounds of elements found inside this view. Includes sub-views and their contents.
     */
    default Set<Bounds> getAllBounds() {
        Set<Bounds> bounds = new HashSet<>();
        getChildElements().forEach(element -> {
            if (element instanceof IViewRoot) {
                bounds.addAll(((IViewRoot)element).getAllBounds());
            }
            if (element instanceof IBounded) {
                bounds.add(((IBounded)element).getBounds());
            }
        });
        return bounds;
    }

    /**
     * Any extra padding this view adds around its contents.
     */
    Padding getContentPadding();

    /**
     * The list of all child elements, buttons included, present on this screen.
     */
    List<Element> getChildElements();

    /**
     * The list of buttons (selectable elements) present on this screen.
     */
    <T extends Element & Drawable & Selectable> List<Selectable> buttons();

    /**
     * Gets the horizonal scroll offset in pixels.
     */
    default int getScrollY() {
        return 0;
    }

    /**
     * Gets the vertical scroll offset in pixels.
     */
    default int getScrollX() {
        return 0;
    }
}
