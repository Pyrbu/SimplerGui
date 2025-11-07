package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public abstract class MultiPlayerGui extends HandledGui {
    protected final List<Player> viewers = new ArrayList<>();

    protected MultiPlayerGui(Inventory inventory) {
        super(inventory);
    }

    public void handleClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        viewers.remove(player);
    }

    public void open(Player player) {
        viewers.add(player);
        super.open(player);
    }

    public List<Player> getViewers() {
        return Collections.unmodifiableList(viewers);
    }
}
