package com.minelittlepony.common.client.gui.element;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.minelittlepony.common.client.gui.ITextContext;
import com.minelittlepony.common.client.gui.Tooltip;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.style.IStyled;
import com.minelittlepony.common.client.gui.style.Style;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.math.MathHelper;

/**
 * A stylable button element.
 * <p>
 * All appearance other than dimensions and position are controlled by this element's {Style}
 * to make switching and changing styles easier.
 *
 * @author     Sollace
 *
 */
public class Button extends PressableWidget implements IBounded, ITextContext, IStyled<Button> {

    private Style style = new Style();

    private final Bounds bounds;

    private static final Consumer<Button> NONE = v -> {};
    @NotNull
    private Consumer<Button> action = NONE;
    @Nullable
    private Tooltip prevTooltip;

    public Button(int x, int y) {
        this(x, y, 200, 20);
    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height, ScreenTexts.EMPTY);

        bounds = new Bounds(y, x, width, height);
    }

    /**
     * Adds a listener to call when this button is clicked.
     *
     * @param callback The callback function.
     * @return {@code this} for chaining purposes.
     */
    @SuppressWarnings("unchecked")
    public Button onClick(@NotNull Consumer<? extends Button> callback) {
        action = (Consumer<Button>)callback;

        return this;
    }

    /**
     * Enables or disables this button.
     */
    public Button setEnabled(boolean enable) {
        active = enable;
        return this;
    }

    /**
     * Hides or shows this button.
     */
    public Button setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Gets this button's current styling.
     */
    @Override
    public Style getStyle() {
        return style;
    }

    /**
     * Sets this button's current styling.
     */
    @Override
    public Button setStyle(Style style) {
        this.style = style;

        return this;
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {
        this.bounds.copy(bounds);

        setX(bounds.left);
        setY(bounds.top);
        setWidth(bounds.width);
        setHeight(bounds.height);
    }

    @Override
    public void setX(int x) {
        bounds.left = x;
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        bounds.top = y;
        super.setY(y);
    }

    @Override
    public void setWidth(int width) {
        bounds.width = width;
        super.setWidth(width);
    }

    public void setHeight(int height) {
        bounds.height = height;
        this.height = height;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder narrationMsg) {
        getStyle().getTooltip().ifPresent(tooltip -> tooltip.appendNarrations(narrationMsg));
    }

    @Override
    public void onPress() {
        action.accept(this);
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return active && visible && getBounds().contains(mouseX, mouseY);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getInstance();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

        int state = getTextureY();

        renderButtonBlit(context, getX(), getY(), state, getWidth(), height);
        renderBackground(context, mc, mouseX, mouseY);

        int foreColor = getStyle().getColor();
        if (!active) {
            foreColor = 10526880;
        } else if (isHovered()) {
            foreColor = 16777120;
        }

        setMessage(getStyle().getText());
        drawIcon(context, mouseX, mouseY, partialTicks);
        renderForground(context, mc, mouseX, mouseY, foreColor | MathHelper.ceil(alpha * 255F) << 24);

        getStyle().getTooltip().ifPresentOrElse(tooltip -> {
            if (tooltip != prevTooltip) {
                prevTooltip = tooltip;
                setTooltip(tooltip.toTooltip());
                this.getTooltipPositioner();
            }
        }, () -> setTooltip(null));
    }

    @Override
    protected TooltipPositioner getTooltipPositioner() {
        TooltipPositioner original = super.getTooltipPositioner();
        return (int screenWidth, int screenHeight, int x, int y, int width, int height) -> {
            Vector2ic pos = original.getPosition(screenWidth, screenHeight, x, y, width, height);
            return new Vector2i(pos.x() + getStyle().toolTipX, pos.y() + getStyle().toolTipY);
        };
    }

    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (getStyle().hasIcon()) {
            getStyle().getIcon().render(context, getX(), getY(), mouseX, mouseY, partialTicks);
        }
    }

    protected void renderForground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        drawMessage(context, mc.textRenderer, foreColor);
    }

    protected void renderBackground(DrawContext context, MinecraftClient mc, int mouseX, int mouseY) {

    }

    protected int getTextureY() {
        int i = 1;
        if (!active) {
            i = 0;
        } else if (isSelected()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    protected final void renderButtonBlit(DrawContext context, int x, int y, int state, int blockWidth, int blockHeight) {
        context.drawNineSlicedTexture(WIDGETS_TEXTURE, x, y, blockWidth, blockHeight, 20, 4, 200, 20, 0, state);
    }
}
