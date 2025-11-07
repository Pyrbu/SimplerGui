package lol.pyr.simplergui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.List;

public abstract class SimpleGui extends HandledGui {
    protected final Player player;

    protected SimpleGui(Player player, Inventory inventory) {
        super(inventory);
        this.player = player;
    }

    @Override
    public Collection<Player> getViewers() {
        return List.of(player);
    }

    @Override
    public void open(Player player) {
        render();
        super.open(player);
    }

    protected abstract void render();
}
