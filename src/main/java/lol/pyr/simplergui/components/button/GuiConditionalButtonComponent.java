package lol.pyr.simplergui.components.button;

import lol.pyr.simplergui.GuiBlueprint;
import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class GuiConditionalButtonComponent<GuiData> implements GuiComponent<GuiData, Void> {
    private final int slot;
    private final ItemStack item;
    private final BiConsumer<InventoryClickEvent, GuiInstance<GuiData>> handler;
    private final BiPredicate<GuiInstance<GuiData>, Player> shouldRender;
    private final Sound sound;

    public GuiConditionalButtonComponent(GuiBlueprint<GuiData> blueprint, GuiButtonConfig config, BiPredicate<GuiInstance<GuiData>, Player> shouldRender, BiConsumer<InventoryClickEvent, GuiInstance<GuiData>> handler) {
        this.slot = config.slot();
        this.item = blueprint.getItem(config.item());
        this.shouldRender = shouldRender;
        this.handler = handler;
        this.sound = config.buildSound();
    }

    @Override
    public void handleOpen(GuiInstance<GuiData> instance, Player player) {
        if (slot == -1) return;
        if (shouldRender.test(instance, player)) {
            instance.getInventory().setItem(slot, item);
            instance.setClickHandler(slot, (i, event) -> {
                if (sound != null) i.playSound(sound);
                handler.accept(event, instance);
            });
        }
    }
}
