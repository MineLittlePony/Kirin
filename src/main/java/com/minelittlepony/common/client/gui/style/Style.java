package com.minelittlepony.common.client.gui.style;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Splitter;
import com.minelittlepony.common.client.gui.sprite.ISprite;
import com.minelittlepony.common.client.gui.sprite.ItemStackSprite;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class Style {

    private ISprite icon = ISprite.EMPTY;

    public int toolTipX = 0;
    public int toolTipY = 0;

    private Optional<List<String>> tooltip = Optional.empty();

    private String text = "";
    private int color = 0xFFFFFFFF;

    public ISprite getIcon() {
        return icon;
    }

    public boolean hasIcon() {
        return getIcon() != ISprite.EMPTY;
    }

    public Style setColor(int color) {
        this.color = color;

        return this;
    }

    public int getColor() {
        return color;
    }

    public Style setText(String text) {
        this.text = text;

        return this;
    }

    public String getText() {
        return I18n.translate(text);
    }

    public Style setIcon(ItemConvertible iitem) {
        return setIcon(new ItemStackSprite().setStack(iitem));
    }

    public Style setIcon(ItemStack stack) {
        return setIcon(new ItemStackSprite().setStack(stack));
    }

    public Style setIcon(ISprite sprite) {
        icon = sprite;

        return this;
    }

    public Style setIcon(ItemStack stack, int colour) {
        return setIcon(new ItemStackSprite().setStack(stack).setTint(colour));
    }

    /**
     * Sets the tooltip. The passed in value will be automatically translated and split into separate
     * lines.
     *
     * @param tooltip A tooltip translation string.
     */
    public Style setTooltip(String tooltip) {
        return setTooltip(Splitter.onPattern("\r?\n|\\\\n").splitToList(I18n.translate(tooltip)));
    }

    public Style setTooltip(String tooltip, int x, int y) {
        return setTooltip(tooltip).setTooltipOffset(x, y);
    }

    /**
     * Sets the tooltip text with a multi-line value.
     */
    public Style setTooltip(List<String> tooltip) {
        this.tooltip = Optional.ofNullable(tooltip);
        return this;
    }

    public Optional<List<String>> getTooltip() {
        return tooltip;
    }

    /**
     * Sets the tooltip offset from the original mouse position.
     */
    public Style setTooltipOffset(int x, int y) {
        toolTipX = x;
        toolTipY = y;
        return this;
    }
}
