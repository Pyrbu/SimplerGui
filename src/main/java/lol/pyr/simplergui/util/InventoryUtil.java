package lol.pyr.simplergui.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
    public static void fillBorder(Inventory inventory, ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) {
            if (SlotUtil.isBorderSlot(i, size)) {
                inventory.setItem(i, item.clone());
            }
        }
    }

    public static void fillInventory(Inventory inventory, ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) inventory.setItem(i, item);
    }

    public static void fillRow(Inventory inventory, int row, ItemStack item) {
        int slot = row * 9;
        for (int i = slot; i < slot + 9; i++) inventory.setItem(i, item);
    }
}
