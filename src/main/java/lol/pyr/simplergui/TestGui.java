package lol.pyr.simplergui;

import lol.pyr.simplergui.util.SlotUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestGui extends PaginatedGui{
    protected TestGui(Player player) {
        super(player, Bukkit.createInventory(player, 9 * 6));
        setMaxPage(10);
    }

    @Override
    protected void render() {
        clearHandlers();
        registerPreviousPage(SlotUtil.getSlot(6, 1));
        registerNextPage(SlotUtil.getSlot(6, 9));
    }
}
