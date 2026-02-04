package lol.pyr.simplergui.components.button;

import lol.pyr.simplergui.GuiBlueprint;
import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public abstract class GuiButtonComponent<GuiData> implements GuiComponent<GuiData, Void> {
    private final int slot;
    private final ItemStack item;
    private final Sound sound;

    public GuiButtonComponent(GuiBlueprint<GuiData> blueprint, GuiButtonConfig config) {
        this.slot = config.slot();
        this.item = blueprint.getItem(config.item());
        this.sound = config.buildSound();
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        if (slot == -1) return;
        instance.getInventory().setItem(slot, item);
        instance.setClickHandler(slot, (i, event) -> {
            if (sound != null) i.playSound(sound);
            handleClick(i, event);
        });
    }

    public abstract void handleClick(GuiInstance<GuiData> instance, InventoryClickEvent event);
}
