package lol.pyr.simplergui.components.button;

import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.BiConsumer;

public class GuiButtonComponent<GuiData> implements GuiComponent<GuiData, Void> {
    private final int slot;
    private final ItemStack item;
    private final BiConsumer<InventoryClickEvent, GuiInstance<GuiData>> handler;
    private final Sound sound;

    public GuiButtonComponent(Map<String, ItemStack> items, GuiButtonConfig config, BiConsumer<InventoryClickEvent, GuiInstance<GuiData>> handler) {
        this.slot = config.slot();
        this.item = items.get(config.item());
        this.handler = handler;
        this.sound = config.buildSound();
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        if (slot == -1) return;
        instance.getInventory().setItem(slot, item);
        instance.setClickHandler(slot, (i, event) -> {
            if (sound != null) i.playSound(sound);
            handler.accept(event, i);
        });
    }
}
