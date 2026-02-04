package lol.pyr.simplergui.components.dynamicitem;

import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public abstract class GuiDynamicItemComponent<GuiData> implements GuiComponent<GuiData, Void> {
    private final int slot;

    protected GuiDynamicItemComponent(int slot) {
        this.slot = slot;
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        if (slot != -1) instance.getInventory().setItem(slot, getItem(instance));
    }

    public abstract ItemStack getItem(GuiInstance<GuiData> instance);
}
