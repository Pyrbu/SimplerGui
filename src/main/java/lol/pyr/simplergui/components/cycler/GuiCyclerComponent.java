package lol.pyr.simplergui.components.cycler;

import lol.pyr.simplergui.GuiBlueprint;
import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import lol.pyr.simplergui.util.ItemBuilder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings("unused")
public abstract class GuiCyclerComponent<GuiData, T> implements GuiComponent<GuiData, Integer> {
    private final List<T> modes = new ArrayList<>();

    private final int slot;
    private final ItemStack baseItem;
    private final String modeFormat;
    private final String modeSelectedFormat;
    private final List<Component> extraLore;
    private final Sound sound;

    public GuiCyclerComponent(GuiBlueprint<GuiData> blueprint, GuiCyclerConfig config, List<T> modes) {
        this.modes.addAll(modes);
        this.slot = config.slot();
        this.baseItem = blueprint.getItem(config.baseItem());
        this.modeFormat = config.modeFormat();
        this.modeSelectedFormat = config.modeSelectedFormat();
        this.extraLore = config.extraLore().stream()
                .map(str -> MiniMessage.miniMessage().deserialize(str))
                .toList();
        this.sound = config.buildSound();
    }

    public T getCurrentMode(GuiInstance<GuiData> instance) {
        int mode = instance.getDataOrDefault(this, 0);
        return mode < 0 || mode >= modes.size() ? null : modes.get(mode);
    }

    public void setCurrentMode(GuiInstance<GuiData> instance, T mode) {
        int index = modes.indexOf(mode);
        instance.setData(this, index);
        update(instance, index);
    }

    public void update(GuiInstance<GuiData> instance, int mode) {
        instance.getInventory().setItem(slot, generateItem(instance, mode));
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        instance.getInventory().setItem(slot, generateItem(instance, 0));
        instance.setClickHandler(slot, (i, event) -> {
            int modifier = 0;
            if (event.isLeftClick()) modifier++;
            if (event.isRightClick()) modifier--;
            int mode = i.getDataOrDefault(this, 0) + modifier;
            if (mode < 0) mode = modes.size() - 1;
            if (mode >= modes.size()) mode = 0;
            i.setData(this, mode);
            if (sound != null) instance.playSound(sound);
            update(instance, mode);
            handleSwitch(i, modes.get(mode));
        });
    }

    private ItemStack generateItem(GuiInstance<GuiData> instance, int currentMode) {
        T mode = currentMode < 0 || currentMode >= modes.size() ? null : modes.get(currentMode);
        ItemBuilder builder = new ItemBuilder(baseItem);
        for (T m : modes) builder.addLore(MiniMessage.miniMessage().deserialize(
                (Objects.equals(m, mode) ? modeSelectedFormat : modeFormat).replace("%mode%", computeModeName(instance, m))));
        if (extraLore != null) builder.addLore(extraLore);
        return builder.build();
    }

    public abstract void handleSwitch(GuiInstance<GuiData> instance, T mode);
    public abstract String computeModeName(GuiInstance<GuiData> instance, T mode);
}
