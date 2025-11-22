package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Collection;

public interface Gui {
    Collection<Player> getViewers();
    void open(Player player);
    void handleClick(InventoryClickEvent event);
    void handleClose(InventoryCloseEvent event);
    void handleDrag(InventoryDragEvent event);
}
