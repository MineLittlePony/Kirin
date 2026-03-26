package com.minelittlepony.kirin.impl;

import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.ScrollContainer;
import com.minelittlepony.common.client.gui.VisibilityMode;
import com.minelittlepony.common.client.gui.element.Button;
import com.minelittlepony.common.client.gui.element.Cycler;
import com.minelittlepony.common.client.gui.element.EnumSlider;
import com.minelittlepony.common.client.gui.element.Label;
import com.minelittlepony.common.client.gui.element.Slider;
import com.minelittlepony.common.client.gui.element.Toggle;
import com.minelittlepony.common.client.gui.packing.GridPacker;
import com.minelittlepony.common.client.gui.packing.IPacker;
import com.minelittlepony.common.client.gui.packing.ListPacker;
import com.minelittlepony.common.client.gui.sprite.ItemStackSprite;
import com.minelittlepony.common.client.gui.style.Style;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;

class KirinTestScreen extends GameGui {

    private final ScrollContainer content = new ScrollContainer();

    private int ticker;

    KirinTestScreen(@Nullable Screen parent) {
        super(Component.literal("Test!! :O"), parent);
        content.margin.top = 30;
        content.margin.bottom = 30;
        content.getContentPadding().top = 10;
        content.getContentPadding().right = 10;
        content.getContentPadding().bottom = 20;
        content.getContentPadding().left = 10;
    }

    @Override
    protected void init() {
        content.init(this::rebuildContent);
    }

    private void rebuildContent() {
        addRenderableOnly(content);
        getChildElements().add(content);

        addButton(new Label(width / 2, 5).setCentered()).getStyle().setText(getTitle().getString());
        addButton(new Button(width / 2 - 100, height - 25))
            .onClick(_ -> finish())
            .getStyle()
                .setText("gui.done");

        int LEFT = content.width / 2 - 210;
        int RIGHT = content.width / 2 + 10;

        if (LEFT < 0) {
            LEFT = content.width / 2 - 100;
            RIGHT = LEFT;
        }

        int row = 0;

        content.addButton(new Toggle(0, 0, false))
            .onChange(on -> {
                GameGui.playSound(SoundEvents.VILLAGER_YES);
                return on;
            })
            .getStyle()
                .setText("Toggle")
                .setTooltip("This is a toggle");
        content.addButton(new Slider(LEFT, row += 20, 0, 100, 50))
            .getStyle()
                .setText("Slider")
                .setTooltip("This is a slider");
        content.addButton(new Slider(LEFT, row += 20, 0, 100, 50))
            .setEnabled(false)
            .getStyle()
                .setText("Slider")
                .setTooltip("This is a DISABLED slider");
        for (int i = 0; i < 2; i++) {
            content.addButton(new EnumSlider<>(LEFT, row += 20, VisibilityMode.AUTO))
                .setTextFormat(slider -> Component.literal("Visibility Mode: " + slider.getValue().name()))
                .setEnabled(i == 0)
                .getStyle()
                    .setTooltip("This is a enum slider! It has a set number of possible values");
        }
        row += 10;
        for (int i = 0; i < 3; i++) {
            content.addButton(new Toggle(LEFT, row += 20, true))
                .getStyle().setText("This is a toggle AAAAH WA LA LALALALALALA");
        }
        content.addButton(new Cycler(LEFT, row += 20, 100, 20))
            .setStyles(
                    new Style().setText("One").setTooltip("Keep clicking and I will cycle back to the beginning!"),
                    new Style().setText("Deus").setTooltip("I have different states"),
                    new Style().setText("Tre").setTooltip("My appearance depends on my state"),
                    new Style().setText("Quadro").setTooltip("Keep clicking and I will cycle back to the beginning!")
            )
            .getStyle()
                .setText("Cycler")
                .setTooltip("This is a cycler. Click me!");

        row += 20;

        var colors = DyeColor.values();

        content.addButton(new Label(width / 2, row += 20))
            .setCentered()
            .onUpdate(button -> {
                ticker++;
                button.getStyle().setColor(colors[(ticker / 10) % colors.length].getTextColor());
            })
            .getStyle()
                .setText("This is a label")
                .setTooltip("Labels can have tooltips. Isn't that cool?");

        int top = row;

        for (int i = 0; i < 10; i++) {
            content.addButton(new Button(LEFT, row += 20, 180, 20))
                .getStyle()
                    .setText("Left Button " + i)
                    .setIcon(Items.SKELETON_SKULL)
                    .setTooltip("Left");
        }
        if (LEFT != RIGHT) {
            row = top;
        }
        for (int i = 0; i < 10; i++) {
            content.addButton(new Button(RIGHT, row += 20, 180, 20))
                .setEnabled(i % 5 == 0)
                .getStyle()
                    .setText("Right Button " + i)
                    .setTooltip("Right");
        }

        row += 20;

        content.addButton(new Label(width / 2, row += 20))
            .setCentered()
            .getStyle()
                .setText("Packing time!");
        content.addButton(new Label(0, row += 20)).getStyle().setText("List packer (single column):");

        IPacker packer = new ListPacker()
                .setItemHeight(20)
                .setItemSpacing(5)
                .setListWidth(content.width - 25)
                .setOffset(0, row + 20);
        packer.start();
        for (int i = 0; i < 10; i++) {
            int l = i;
            content.addButton(new Button(0, 0)).styled(s -> s.setText("" + l)).setBounds(packer.next());
        }
        row = packer.next().bottom();

        content.addButton(new Label(0, row += 20)).getStyle().setText("Grid packer (multi column):");

        packer = new GridPacker()
                .setItemHeight(20)
                .setItemSpacing(5)
                .setListWidth(content.width - 10)
                .setItemWidth(100)
                .setOffset(0, row + 20);
        packer.start();
        for (int i = 0; i < 10; i++) {
            int l = i;
            content.addButton(new Button(0, 0)).styled(s -> s.setText("" + l)).setBounds(packer.next());
        }
        for (var item : BuiltInRegistries.ITEM) {
            if (item != Items.AIR) {
                var b = new Button(0, 0);
                b.getStyle().setIcon(new ItemStackSprite().setStack(item).setTint(CommonColors.DARK_PURPLE));
                content.addButton(b).setBounds(packer.next());
            }
        }
        row = packer.next().bottom();

    }
}
