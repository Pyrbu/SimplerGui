package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Collection;

public interface Gui {
    void open(Player player);
    Collection<Player> getViewers();

    void handleClick(InventoryClickEvent event);
    void handleDrag(InventoryDragEvent event);
    void handleClose(InventoryCloseEvent event);
}
