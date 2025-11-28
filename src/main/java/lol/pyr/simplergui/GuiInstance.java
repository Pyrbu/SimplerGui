package lol.pyr.simplergui;

import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GuiInstance<GuiData> implements Gui {
    private final GuiBlueprint<GuiData> blueprint;
    private final Inventory inventory;
    private final List<Player> viewers = new ArrayList<>();
    private final Map<GuiComponent<GuiData, ?>, Object> componentData = new HashMap<>();
    private GuiData guiData;

    private final Map<Integer, BiConsumer<GuiInstance<GuiData>, InventoryClickEvent>> clickHandlers = new HashMap<>();
    private boolean setup = false;

    public GuiInstance(GuiBlueprint<GuiData> blueprint, GuiData data) {
        this.blueprint = blueprint;
        this.inventory = blueprint.createInventory();
        this.guiData = data;
    }

    public void setup() {
        if (setup) return;
        setup = true;
        for (GuiComponent<GuiData, ?> component : blueprint.getComponents()) component.setup(this);
    }

    public void reset() {
        inventory.clear();
        clickHandlers.clear();
        setup = false;
    }

    public GuiData getData() {
        return guiData;
    }

    public GuiData getDataOrDefault(GuiData data) {
        return guiData == null ? data : guiData;
    }

    public GuiData getDataOrCompute(Supplier<GuiData> supplier) {
        if (guiData == null) guiData = supplier.get();
        return guiData;
    }

    public void setData(GuiData data) {
        guiData = data;
    }

    public List<Player> getViewers() {
        return viewers;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void playSound(Sound sound) {
        for (Player player : viewers) player.playSound(sound);
    }

    public void setClickHandler(int slot, BiConsumer<GuiInstance<GuiData>, InventoryClickEvent> handler) {
        clickHandlers.put(slot, handler);
    }

    public void clearClickHandler(int slot) {
        clickHandlers.remove(slot);
    }

    public void clearClickHandlers(Collection<Integer> slots) {
        for (Integer i : slots) if (i != null) clearClickHandler(i);
    }

    public <T> T getDataOrCompute(GuiComponent<GuiData, T> component, Supplier<T> supplier) {
        if (componentData.containsKey(component)) return getData(component);
        T value = supplier.get();
        setData(component, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(GuiComponent<GuiData, T> component) {
        return (T) componentData.get(component);
    }

    @SuppressWarnings("unchecked")
    public <T> T getDataOrDefault(GuiComponent<GuiData, T> component, T def) {
        return componentData.containsKey(component) ? (T) componentData.get(component) : def;
    }

    public <T> void setData(GuiComponent<GuiData, T> component, T data) {
        componentData.put(component, data);
    }

    public boolean hasData(GuiComponent<GuiData, ?> component) {
        return componentData.containsKey(component);
    }

    public void removeData(GuiComponent<GuiData, ?> component) {
        componentData.remove(component);
    }

    @Override
    public void open(Player player) {
        viewers.add(player);
        setup();
        for (GuiComponent<GuiData, ?> component : blueprint.getComponents()) component.handleOpen(this, player);
        player.openInventory(inventory);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory() instanceof PlayerInventory) {
            for (GuiComponent<GuiData, ?> component : blueprint.getComponents()) component.handlePlayerInventoryClick(this, event);
            return;
        }
        if (!inventory.equals(event.getClickedInventory())) return;

        var handler = clickHandlers.get(event.getSlot());
        if (handler != null) handler.accept(this, event);
    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        if (!inventory.equals(event.getInventory())) return;
        event.setCancelled(true);
        // TODO maybe add drag handlers
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) viewers.remove(player);
        if (!inventory.equals(event.getInventory())) return;
        for (GuiComponent<GuiData, ?> component : blueprint.getComponents()) component.handleClose(this, event);
    }

    public GuiBlueprint<GuiData> getBlueprint() {
        return blueprint;
    }
}
