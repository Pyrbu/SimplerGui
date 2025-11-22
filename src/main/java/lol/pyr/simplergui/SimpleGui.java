package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.function.Consumer;

public abstract class SimpleGui implements Gui {
    private final Map<Integer, Consumer<InventoryClickEvent>> handlerMap = new HashMap<>();
    protected final Inventory inventory;
    protected final List<Player> viewers = new ArrayList<>();

    public SimpleGui(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Collection<Player> getViewers() {
        return viewers;
    }

    @Override
    public void open(Player player) {
        viewers.add(player);
        player.openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!inventory.equals(event.getClickedInventory())) return;
        if (!handlerMap.containsKey(event.getSlot())) return;
        handlerMap.get(event.getSlot()).accept(event);
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {
    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    protected void registerHandler(int slot, Consumer<InventoryClickEvent> handler) {
        handlerMap.put(slot, handler);
    }

    protected void clearHandlers() {
        handlerMap.clear();
    }
}
