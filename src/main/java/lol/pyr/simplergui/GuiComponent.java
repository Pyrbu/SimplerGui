package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

@SuppressWarnings("unused")
public interface GuiComponent<GuiData, ComponentData> {
    default void setup(GuiInstance<GuiData> instance) {}
    default void handleClose(GuiInstance<GuiData> instance, InventoryCloseEvent event) {}
    default void handleOpen(GuiInstance<GuiData> instance, Player player) {}
    default void handlePlayerInventoryClick(GuiInstance<GuiData> instance, InventoryClickEvent event) {}
}
