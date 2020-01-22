package com.minelittlepony.common.client.gui.element;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

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
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.util.math.MathHelper;

public class Button extends AbstractButtonWidget implements ITooltipped<Button>, IBounded, ITextContext, IStyled<Button> {

    private Style style = new Style();

    private final Bounds bounds;

    private static final Consumer<Button> NONE = v -> {};
    @Nonnull
    private Consumer<Button> action = NONE;

    public Button(int x, int y) {
        this(x, y, 200, 20);
    }

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height, "");

        bounds = new Bounds(y, x, width, height);
    }

    @SuppressWarnings("unchecked")
    public Button onClick(@Nonnull Consumer<? extends Button> callback) {
        action = (Consumer<Button>)callback;

        return this;
    }

    public Button setEnabled(boolean enable) {
        active = enable;
        return this;
    }

    @Override
    public Style getStyle() {
        return style;
    }

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

        x = bounds.left;
        y = bounds.top;
        width = bounds.width;
        height = bounds.height;
    }

    @Override
    public void renderToolTip(Screen parent, int mouseX, int mouseY) {
        if (visible) {
            getStyle().getTooltip().ifPresent(tooltip -> {
                parent.renderTooltip(tooltip, mouseX + getStyle().toolTipX, mouseY + getStyle().toolTipY);
            });
        }
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getInstance();

        mc.getTextureManager().bindTexture(WIDGETS_LOCATION);

        RenderSystem.color4f(1, 1, 1, alpha);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.blendFunc(
                GlStateManager.SrcFactor.SRC_ALPHA,
                GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

        int state = 46 + getYImage(isHovered()) * 20;

        renderButtonBlit(x, y, state, width, height);

        renderBg(mc, mouseX, mouseY);

        int foreColor = getStyle().getColor();
        if (!active) {
            foreColor = 10526880;
        } else if (isHovered()) {
            foreColor = 16777120;
        }

        if (getStyle().hasIcon()) {
            getStyle().getIcon().render(x, y, mouseX, mouseY, partialTicks);
            RenderSystem.disableDepthTest();
        }

        setMessage(getStyle().getText());
        renderForground(mc, mouseX, mouseY, foreColor | MathHelper.ceil(alpha * 255.0F) << 24);
    }

    protected void renderForground(MinecraftClient mc, int mouseX, int mouseY, int foreColor) {
        drawCenteredLabel(getMessage(), x + width / 2, y + (height - 8) / 2, foreColor, 0);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        action.accept(this);
    }

    protected final void renderButtonBlit(int x, int y, int state, int blockWidth, int blockHeight) {

        int endV = 200 - blockWidth/2;
        int endU = state + 20 - blockHeight/2;

        blit(x,                y,
                0, state,
                blockWidth/2, blockHeight/2);
        blit(x + blockWidth/2, y,
                endV, state,
                blockWidth/2, blockHeight/2);

        blit(x,                y + blockHeight/2,
                0, endU,
                blockWidth/2, blockHeight/2);
        blit(x + blockWidth/2, y + blockHeight/2,
                endV, endU,
                blockWidth/2, blockHeight/2);
    }

}
