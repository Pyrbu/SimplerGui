package lol.pyr.simplergui.components.list;

import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;

public class GuiListComponent<GuiData, T> implements GuiComponent<GuiData, Long> {
    private final boolean renderOnOpen;
    private final Function<GuiInstance<GuiData>, Collection<T>> objectsSupplier;
    private final SelectHandler<GuiData, T> selectHandler;
    private final Function<T, ItemStack> itemRenderer;

    private final List<Integer> displaySlots = new ArrayList<>();
    private final int nextPageSlot;
    private final ItemStack nextPageItem;
    private final int previousPageSlot;
    private final ItemStack previousPageItem;
    private final Sound nextPageSound;
    private final Sound previousPageSound;
    private final Sound selectSound;

    public GuiListComponent(Map<String, ItemStack> items, GuiListConfig config, boolean renderOnOpen, Function<GuiInstance<GuiData>, Collection<T>> objectsSupplier, SelectHandler<GuiData, T> selectHandler, Function<T, ItemStack> itemRenderer) {
        this.renderOnOpen = renderOnOpen;
        this.objectsSupplier = objectsSupplier;
        this.selectHandler = selectHandler;
        this.itemRenderer = itemRenderer;
        this.displaySlots.addAll(config.displaySlots());
        this.nextPageSlot = config.nextPageSlot();
        this.nextPageItem = items.get(config.nextPageItem());
        this.previousPageSlot = config.previousPageSlot();
        this.previousPageItem = items.get(config.previousPageItem());
        this.nextPageSound = config.buildNextPageSound();
        this.previousPageSound = config.buildPreviousPageSound();
        this.selectSound = config.buildSelectSound();
    }

    public void render(GuiInstance<GuiData> instance) {
        long page = instance.getDataOrDefault(this, 0L);
        long objectsPerPage = displaySlots.size();
        Collection<T> list = objectsSupplier.apply(instance);
        long maxPage = list.size() / objectsPerPage;

        if (previousPageSlot != -1) {
            instance.clearClickHandler(previousPageSlot);
            instance.getInventory().setItem(previousPageSlot, instance.getBlueprint().getDefaultItem(previousPageSlot));
            if (page > 0) {
                instance.getInventory().setItem(previousPageSlot, previousPageItem);
                instance.setClickHandler(previousPageSlot, (i, event) -> {
                    if (previousPageSound != null) i.playSound(previousPageSound);
                    instance.setData(this, instance.getDataOrDefault(this, 0L) - 1);
                    render(i);
                });
            }
        }

        if (nextPageSlot != -1) {
            instance.clearClickHandler(nextPageSlot);
            instance.getInventory().setItem(nextPageSlot, instance.getBlueprint().getDefaultItem(nextPageSlot));
            if (page < maxPage) {
                instance.getInventory().setItem(nextPageSlot, nextPageItem);
                instance.setClickHandler(nextPageSlot, (i, event) -> {
                    if (nextPageSound != null) i.playSound(nextPageSound);
                    instance.setData(this, instance.getDataOrDefault(this, 0L) + 1);
                    render(i);
                });
            }
        }

        instance.clearClickHandlers(displaySlots);
        for (int slot : displaySlots) instance.getInventory().clear(slot);
        int i = 0;
        for (T obj : list.stream()
                .skip(objectsPerPage * page)
                .limit(objectsPerPage)
                .toList()) {
            int slot = displaySlots.get(i++);
            instance.getInventory().setItem(slot, itemRenderer.apply(obj));
            instance.setClickHandler(slot, (ins, event) -> {
                if (selectSound != null) ins.playSound(selectSound);
                selectHandler.handle(event, ins, obj);
            });
        }
    }

    @Override
    public void handleOpen(GuiInstance<GuiData> instance, Player player) {
        if (renderOnOpen) render(instance);
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        render(instance);
    }

    @FunctionalInterface
    public interface SelectHandler<GuiData, T> {
        void handle(InventoryClickEvent event, GuiInstance<GuiData> instance, T obj);
    }
}
