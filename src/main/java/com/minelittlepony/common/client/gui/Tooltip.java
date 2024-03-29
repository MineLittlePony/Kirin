package com.minelittlepony.common.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joml.Vector2i;
import com.google.common.base.Splitter;
import com.minelittlepony.common.client.gui.style.IStyled;
import com.minelittlepony.common.mixin.MixinTooltip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.gui.tooltip.WidgetTooltipPositioner;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public interface Tooltip extends Narratable {
    Splitter LINE_SPLITTER = Splitter.onPattern("\r?\n|\\\\n");

    List<Text> getLines();

    @Override
    default void appendNarrations(NarrationMessageBuilder narrationMsg) {
        getLines().forEach(line -> narrationMsg.put(NarrationPart.HINT, line));
    }

    default CharSequence getString() {
        StringBuilder builder = new StringBuilder();
        getLines().forEach(line -> {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(line.getString());
        });
        return builder;
    }

    default Stream<Text> stream() {
        return getLines().stream();
    }

    default net.minecraft.client.gui.tooltip.Tooltip toTooltip(IStyled<?> element) {
        var tooltip = new net.minecraft.client.gui.tooltip.Tooltip(Text.empty(), Text.literal(getString().toString())) {
            @Override
            protected TooltipPositioner createPositioner(boolean hovered, boolean focused, ScreenRect focus) {
                TooltipPositioner original = super.createPositioner(hovered, focused, focus);
                if (!(original instanceof WidgetTooltipPositioner)) {
                    return original;
                }
                return (int screenWidth, int screenHeight, int x, int y, int width, int height) -> {
                    return original.getPosition(screenWidth, screenHeight, x, y, width, height)
                        .add(element.getStyle().toolTipX, element.getStyle().toolTipY, new Vector2i());
                };
            }
        };
        ((MixinTooltip)tooltip).setLines(stream().map(Text::asOrderedText).toList());
        return tooltip;
    }

    static Tooltip of(List<Text> lines) {
        List<Text> flines = lines.stream()
                .map(Tooltip::of)
                .flatMap(Tooltip::stream)
                .collect(Collectors.toList());
        return () -> flines;
    }

    static Tooltip of(List<StringVisitable> lines, Style style) {
        List<Text> flines = lines.stream()
                .map(line -> of(line, style))
                .flatMap(Tooltip::stream)
                .collect(Collectors.toList());
        return () -> flines;
    }

    static Tooltip of(String text) {
        return of(Text.translatable(text));
    }

    static Tooltip of(Text text) {
        return of(text, text.getStyle());
    }

    static Tooltip of(StringVisitable text, Style styl) {
        List<Text> lines = new ArrayList<>();
        lines.add(Text.empty());

        text.visit((style, part) -> {
            List<Text> parts = LINE_SPLITTER.splitToList(part)
                    .stream()
                    .map(i -> Text.literal(i).fillStyle(style))
                    .collect(Collectors.toList());

            lines.add(((MutableText)lines.remove(lines.size() - 1)).append(parts.remove(0)));
            lines.addAll(parts);

            return Optional.empty();
        }, styl);

        return () -> lines;
    }

    static Tooltip of(String text, int maxWidth) {
        return of(Text.translatable(text), maxWidth);
    }

    static Tooltip of(Text text, int maxWidth) {
        return of(text, text.getStyle(), maxWidth);
    }

    static Tooltip of(StringVisitable text, Style style, int maxWidth) {
        return of(MinecraftClient.getInstance().textRenderer.getTextHandler().wrapLines(text, maxWidth, style), style);
    }
}
