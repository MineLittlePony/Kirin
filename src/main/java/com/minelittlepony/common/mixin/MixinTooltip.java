package com.minelittlepony.common.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.OrderedText;

@Mixin(Tooltip.class)
public interface MixinTooltip {
    @Accessor("lines")
    void setLines(List<OrderedText> lines);
}
