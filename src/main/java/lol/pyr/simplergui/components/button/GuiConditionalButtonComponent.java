package lol.pyr.simplergui.components.button;

import lol.pyr.simplergui.GuiBlueprint;
import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public abstract class GuiConditionalButtonComponent<GuiData> implements GuiComponent<GuiData, Void> {
    private final int slot;
    private final ItemStack item;
    private final Sound sound;

    public GuiConditionalButtonComponent(GuiBlueprint<GuiData> blueprint, GuiButtonConfig config) {
        this.slot = config.slot();
        this.item = blueprint.getItem(config.item());
        this.sound = config.buildSound();
    }

    @Override
    public void handleOpen(GuiInstance<GuiData> instance, Player player) {
        if (slot == -1) return;
        if (shouldRender(instance, player)) {
            instance.getInventory().setItem(slot, item);
            instance.setClickHandler(slot, (i, event) -> {
                if (sound != null) i.playSound(sound);
                handleClick(instance, event);
            });
        }
    }

    public abstract void handleClick(GuiInstance<GuiData> instance, InventoryClickEvent event);
    public abstract boolean shouldRender(GuiInstance<GuiData> instance, Player player);
}
