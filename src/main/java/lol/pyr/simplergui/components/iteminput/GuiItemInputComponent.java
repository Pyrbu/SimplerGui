package lol.pyr.simplergui.components.iteminput;

import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class GuiItemInputComponent<GuiData> implements GuiComponent<GuiData, List<ItemStack>> {
    private final List<Integer> slots = new ArrayList<>();

    public GuiItemInputComponent(GuiItemInputConfig config) {
        this.slots.addAll(config.slots());
    }

    public List<ItemStack> getItems(GuiInstance<GuiData> instance) {
        return instance.getDataOrCompute(this, ArrayList::new);
    }

    public List<ItemStack> removeItems(GuiInstance<GuiData> instance) {
        List<ItemStack> list = instance.getData(this);
        instance.removeData(this);
        render(instance);
        return list;
    }

    public void render(GuiInstance<GuiData> instance) {
        for (Integer slot : slots) instance.getInventory().clear(slot);
        instance.clearClickHandlers(slots);

        List<ItemStack> items = getItems(instance);
        int len = Math.min(items.size(), slots.size());
        for (int i = 0; i < len; i++) {
            int index = i;
            int slot = slots.get(i);
            instance.getInventory().setItem(slot, items.get(i));
            instance.setClickHandler(slot, (ins, event) -> {
                if (!(event.getWhoClicked() instanceof Player player)) return;
                ItemStack item = items.remove(index);
                if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItem(player.getLocation(), item);
                else player.getInventory().addItem(item);
                render(ins);
            });
        }
    }

    @Override
    public void handleClose(GuiInstance<GuiData> instance, InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        List<ItemStack> items = getItems(instance);
        for (ItemStack item : items) {
            if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItem(player.getLocation(), item);
            else player.getInventory().addItem(item);
        }
        items.clear();
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        render(instance);
    }

    @Override
    public void handlePlayerInventoryClick(GuiInstance<GuiData> instance, InventoryClickEvent event) {
        List<ItemStack> items = getItems(instance);
        if (items.size() >= slots.size()) return;
        if (!(event.getClickedInventory() instanceof PlayerInventory)) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        items.add(item.clone());
        item.setAmount(0);
        render(instance);
    }
}
