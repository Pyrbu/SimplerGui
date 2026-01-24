package lol.pyr.simplergui.components.button;

import lol.pyr.simplergui.GuiBlueprint;
import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import lol.pyr.simplergui.GuiManager;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuiBackButtonComponent<GuiData> implements GuiComponent<GuiData, GuiInstance<?>> {
    private final GuiManager guiManager;
    private final int slot;
    private final ItemStack item;
    private final Sound sound;

    public GuiBackButtonComponent(GuiBlueprint<GuiData> blueprint, GuiManager guiManager, GuiButtonConfig config) {
        this.guiManager = guiManager;
        this.slot = config.slot();
        this.item = blueprint.getItem(config.item());
        this.sound = config.buildSound();
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        if (slot == -1) return;
        if (instance.hasData(this)) {
            instance.getInventory().setItem(slot, item);
            instance.setClickHandler(slot, (i, event) -> {
                if (!(event.getWhoClicked() instanceof Player player)) return;
                GuiInstance<?> previous = i.getData(this);
                if (previous == null) return;
                if (sound != null) player.playSound(sound);
                guiManager.openGui(player, previous);
            });
        }
    }
}
