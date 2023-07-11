package com.minelittlepony.common.client.gui.element;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.minelittlepony.common.client.gui.ITextContext;
import com.minelittlepony.common.client.gui.ITooltipped;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.dimension.IBounded;
import com.minelittlepony.common.client.gui.style.IStyled;
import com.minelittlepony.common.client.gui.style.Style;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Util;
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
public class Button extends PressableWidget implements ITooltipped<Button>, IBounded, ITextContext, IStyled<Button> {

    private Style style = new Style();

    private final Bounds bounds;

    private static final Consumer<Button> NONE = v -> {};
    @NotNull
    private Consumer<Button> action = NONE;

    private boolean wasHovered;
    private long lastHoveredStateChanged;
    private long tooltipDelay;

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

    public void renderToolTip(MatrixStack matrices, Screen parent, int mouseX, int mouseY) {


        boolean hovered = this.isHovered();

        if (hovered != wasHovered) {
            wasHovered = hovered;
            lastHoveredStateChanged = Util.getMeasuringTimeMs();
        }

        if (hovered && visible && Util.getMeasuringTimeMs() - lastHoveredStateChanged > tooltipDelay) {
            getStyle().getTooltip().ifPresent(tooltip -> {
                parent.renderTooltip(matrices, tooltip.getLines(), mouseX + getStyle().toolTipX, mouseY + getStyle().toolTipY);
            });
        }
    }

    @Override
    public void setTooltipDelay(int delay) {
        super.setTooltipDelay(delay);
        this.tooltipDelay = delay;
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
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getInstance();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

        int state = getTextureY();

        renderButtonBlit(matrices, getX(), getY(), state, getWidth(), height);
        renderBackground(matrices, mc, mouseX, mouseY);

        int foreColor = getStyle().getColor();
        if (!active) {
            foreColor = 10526880;
        } else if (isHovered()) {
            foreColor = 16777120;
        }

        setMessage(getStyle().getText());
        drawIcon(matrices, mouseX, mouseY, partialTicks);
        renderForground(matrices, mc, mouseX, mouseY, foreColor | MathHelper.ceil(alpha * 255F) << 24);
    }

    protected void drawIcon(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        if (getStyle().hasIcon()) {
            getStyle().getIcon().render(matrices, getX(), getY(), mouseX, mouseY, partialTicks);
        }
    }

    protected void renderForground(MatrixStack matrices, MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        drawCenteredLabel(matrices, getMessage(), getX() + getWidth() / 2, getY() + (height - 8) / 2, foreColor, 0);
    }

    @Override
    protected void renderBackground(MatrixStack matrices, MinecraftClient mc, int mouseX, int mouseY) {

    }

    protected int getTextureY() {
        int i = 1;
        if (!active) {
            i = 0;
        } else if (isHovered()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    protected final void renderButtonBlit(MatrixStack matrices, int x, int y, int state, int blockWidth, int blockHeight) {
            int endV = 200 - blockWidth/2;
            int endU = state + 20 - blockHeight/2;

            drawTexture(matrices,
                    x,                y,
                    0, state,
                    blockWidth/2, blockHeight/2);
            drawTexture(matrices,
                    x + blockWidth/2, y,
                    endV, state,
                    blockWidth/2, blockHeight/2);

            drawTexture(matrices,
                    x,                y + blockHeight/2,
                    0, endU,
                    blockWidth/2, blockHeight/2);
            drawTexture(matrices,
                    x + blockWidth/2, y + blockHeight/2,
                    endV, endU,
                    blockWidth/2, blockHeight/2);
    }
}
