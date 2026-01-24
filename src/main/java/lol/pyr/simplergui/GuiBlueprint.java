package lol.pyr.simplergui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiBlueprint<GuiData> {
    private final int inventorySize;
    private final Component name;
    private final Map<String, ItemStack> items = new HashMap<>();
    private final Map<Integer, ItemStack> defaultLayout = new HashMap<>();
    protected final List<GuiComponent<GuiData, ?>> components = new ArrayList<>();

    protected GuiBlueprint(GuiBlueprintConfig config) {
        this.inventorySize = config.guiSize();
        this.name = MiniMessage.miniMessage().deserialize(config.guiName());
        for (Map.Entry<String, String> entry : config.items().entrySet()) items.put(entry.getKey(), ItemStack.deserializeBytes(Base64.getDecoder().decode(entry.getValue())));
        for (Map.Entry<Integer, String> entry : config.defaultLayout().entrySet()) defaultLayout.put(entry.getKey(), items.get(entry.getValue()));
    }

    public Inventory createInventory() {
        Inventory inventory = name == null ? Bukkit.createInventory(null, inventorySize) : Bukkit.createInventory(null, inventorySize, name);
        for (Map.Entry<Integer, ItemStack> entry : defaultLayout.entrySet()) inventory.setItem(entry.getKey(), entry.getValue());
        return inventory;
    }

    public ItemStack getDefaultItem(int slot) {
        return defaultLayout.get(slot);
    }

    public ItemStack getItem(String id) {
        return items.get(id);
    }

    protected <T extends GuiComponent<GuiData, ?>> T registerComponent(T component) {
        components.add(component);
        return component;
    }

    public List<GuiComponent<GuiData, ?>> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public boolean isInstance(GuiInstance<?> instance) {
        return Objects.equals(instance.getBlueprint(), this);
    }

    @SuppressWarnings("unchecked")
    public GuiInstance<GuiData> castInstance(GuiInstance<?> instance) {
        return (GuiInstance<GuiData>) instance;
    }
}
