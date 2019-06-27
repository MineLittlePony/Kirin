package com.minelittlepony.common.client.gui.packing;

import java.util.List;

import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;

/**
 * A packer is a tool used for arranging elements on a UI.
 *
 * @author Sollace
 */
public interface IPacker {

    /**
     * Starts packing. Called to reset the packer's state back at the beginning.
     */
    void start();

    /**
     * Increments the packer's state and gets the next element position.
     */
    Bounds next();

    /**
     * Repacks the elements of the supplied screen according to this packer's specifications.
     */
    default void pack(Screen screen) {
        pack(screen.children());
    }

    /**
     * Repacks all the supplied elements according to this packer's specifications.
     */
    default void pack(List<? extends Element> elements) {
        start();
        elements.forEach(element -> {
            if (element instanceof IBounded) {
                ((IBounded)element).setBounds(next());
            }
        });
    }
}
