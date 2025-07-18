package com.minelittlepony.common.client.gui.element;

import com.minelittlepony.common.client.gui.IField;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a toggle (switch) element with two states (ON/OFF).
 *
 * @author Sollace
 */
public class Toggle extends Button implements IField<Boolean, Toggle> {

    private boolean on;

    @NotNull
    private IChangeCallback<Boolean> action = IChangeCallback::none;

    public Toggle(int x, int y, Supplier<Boolean> value) {
        this(x, y, Objects.requireNonNull(value.get(), "value was null"));
    }

    public Toggle(int x, int y, boolean value) {
        super(x, y, 30, 15);

        on = value;
    }

    @Override
    public Toggle onChange(@NotNull IChangeCallback<Boolean> action) {
        this.action = action;
        return this;
    }

    @Override
    public Boolean getValue() {
        return on;
    }

    @Override
    public Toggle setValue(Boolean value) {
        if (value != on) {
            on = action.perform(value);
        }

        return this;
    }

    @Override
    public Bounds getBounds() {
        Bounds bounds = super.getBounds();

        // The text label sits outside the bounds of the main toggle widget,
        // so we have to include that in our calculations.
        Text label = getStyle().getText();
        int labelWidth = MinecraftClient.getInstance().textRenderer.getWidth(label);

        bounds.width = labelWidth > 0 ? Math.max(bounds.width, width + 10 + labelWidth) : width;

        return bounds;
    }

    @Override
    public void onPress() {
        super.onPress();
        setValue(!on);
    }

    @Override
    protected void renderBackground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY) {
        Identifier backgroundTexture = TEXTURES.get(false, isSelected());
        context.drawTexture(
                null,
                backgroundTexture,
                getX(), getY(),
                0.0F, 0.0F,
                getWidth(), getHeight(),
                getWidth(), getHeight()
        );
        int sliderX = getX() + (on ? getWidth() - 8 : 0);
        Identifier sliderTexture = TEXTURES.get(active, isSelected());
        context.drawTexture(
                null,
                sliderTexture,
                sliderX, getY(),
                0.0F, 0.0F,
                8, getHeight(),
                8, getHeight()
        );
    }

    @Override
    protected void renderForeground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        int textY = getY() + mc.textRenderer.fontHeight / 2;
        int textX = getX() + width + 10;

        drawLabel(context, getStyle().getText(), textX, textY, foreColor, 0);
    }
}
