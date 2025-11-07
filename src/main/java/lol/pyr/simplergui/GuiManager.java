package lol.pyr.simplergui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager implements Listener {
    private final Plugin plugin;
    private final Map<UUID, Gui> guiMap = new HashMap<>();

    public GuiManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void shutdown() {
        for (Gui gui : guiMap.values()) for (Player player : gui.getViewers()) player.closeInventory();
        guiMap.clear();
        HandlerList.unregisterAll(this);
    }

    public void openGui(Player player, Gui gui) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> openGui(player, gui));
            return;
        }
        player.closeInventory();
        guiMap.put(player.getUniqueId(), gui);
        gui.open(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Gui gui = guiMap.get(event.getWhoClicked().getUniqueId());
        if (gui == null) return;
        gui.handleClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Gui gui = guiMap.get(event.getPlayer().getUniqueId());
        if (gui == null) return;
        gui.handleClose(event);
        guiMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Gui gui = guiMap.get(event.getWhoClicked().getUniqueId());
        if (gui == null) return;
        gui.handleDrag(event);
    }
}
