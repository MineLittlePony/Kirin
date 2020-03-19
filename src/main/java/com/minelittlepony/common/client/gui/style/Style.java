package com.minelittlepony.common.client.gui.style;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Splitter;
import com.minelittlepony.common.client.gui.sprite.ISprite;
import com.minelittlepony.common.client.gui.sprite.ItemStackSprite;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

/**
 * Controls the visual appearance of any elements in Kirin
 * (label, font colour, the icon, and tooltip)
 *
 * @author     Sollace
 *
 */
public class Style {

    private ISprite icon = ISprite.EMPTY;

    public int toolTipX = 0;
    public int toolTipY = 0;

    private Optional<List<String>> tooltip = Optional.empty();

    private String text = "";
    private int color = 0xFFFFFFFF;

    /**
     * Gets the icon to be used on buttons with this style.
     */
    public ISprite getIcon() {
        return icon;
    }

    public boolean hasIcon() {
        return getIcon() != ISprite.EMPTY;
    }

    /**
     * Sets the font colour to be used for labes and button text.
     */
    public Style setColor(int color) {
        this.color = color;

        return this;
    }

    public int getColor() {
        return color;
    }

    /**
     * Sets the text label to display. Accepts raw text, or a translation string.
     * Translations are done internally.
     */
    public Style setText(String text) {
        this.text = text;

        return this;
    }

    public String getText() {
        return I18n.translate(text);
    }

    /**
     * Sets the icon to use for elements with this style.
     *
     * @param iitem An Item or Item supplier to render on this button
     */
    public Style setIcon(ItemConvertible iitem) {
        return setIcon(new ItemStackSprite().setStack(iitem));
    }

    /**
     * Sets the icon to use for elements with this style.
     *
     * @param stack An ItemStack to render on this button
     */
    public Style setIcon(ItemStack stack) {
        return setIcon(new ItemStackSprite().setStack(stack));
    }

    /**
     * Sets the icon to use for elements with this style.
     */
    public Style setIcon(ISprite sprite) {
        icon = sprite;

        return this;
    }

    /**
     * Sets the icon to use for elements with this style.
     *
     * @param stack An ItemStack to render on this button
     * @param colour If the ItemStack is dyeable, will use the defined colour.
     */
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
