package com.minelittlepony.common.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Splitter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public interface Tooltip {
    Splitter LINE_SPLITTER = Splitter.onPattern("\r?\n|\\\\n");

    List<Text> getLines();

    default void appendNarrations(NarrationMessageBuilder narrationMsg) {
        getLines().forEach(line -> narrationMsg.put(NarrationPart.HINT, line));
    }

    default CharSequence getString() {
        StringBuilder builder = new StringBuilder();
        getLines().forEach(line -> {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(line.asString());
        });
        return builder;
    }

    default Stream<Text> stream() {
        return getLines().stream();
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
        return of(new TranslatableText(text));
    }

    static Tooltip of(Text text) {
        return of(text, text.getStyle());
    }

    static Tooltip of(StringVisitable text, Style styl) {
        List<Text> lines = new ArrayList<>();
        lines.add(new LiteralText(""));

        text.visit((style, part) -> {
            List<Text> parts = LINE_SPLITTER.splitToList(part)
                    .stream()
                    .map(i -> new LiteralText(i).fillStyle(style))
                    .collect(Collectors.toList());

            lines.add(((MutableText)lines.remove(lines.size() - 1)).append(parts.remove(0)));
            lines.addAll(parts);

            return Optional.empty();
        }, styl);

        return () -> lines;
    }

    static Tooltip of(String text, int maxWidth) {
        return of(new TranslatableText(text), maxWidth);
    }

    static Tooltip of(Text text, int maxWidth) {
        return of(text, text.getStyle(), maxWidth);
    }

    static Tooltip of(StringVisitable text, Style style, int maxWidth) {
        return of(MinecraftClient.getInstance().textRenderer.getTextHandler().wrapLines(text, maxWidth, style), style);
    }
}
