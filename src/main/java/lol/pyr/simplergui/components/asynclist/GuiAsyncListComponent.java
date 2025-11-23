package lol.pyr.simplergui.components.asynclist;

import lol.pyr.simplergui.GuiComponent;
import lol.pyr.simplergui.GuiInstance;
import lol.pyr.simplergui.components.list.GuiListComponent;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class GuiAsyncListComponent<GuiData, T> implements GuiComponent<GuiData, GuiAsyncListState> {
    private final Plugin plugin;

    private final Function<GuiInstance<GuiData>, CompletableFuture<Collection<T>>> objectsSupplier;
    private final GuiListComponent.SelectHandler<GuiData, T> selectHandler;
    private final Function<T, CompletableFuture<ItemStack>> itemRenderer;
    private final boolean renderOnOpen;

    private final List<Integer> displaySlots = new ArrayList<>();
    private final int nextPageSlot;
    private final ItemStack nextPageItem;
    private final int previousPageSlot;
    private final ItemStack previousPageItem;

    private final int loadingSlot;
    private final ItemStack loadingItem;

    private final Sound nextPageSound;
    private final Sound previousPageSound;
    private final Sound selectSound;

    public GuiAsyncListComponent(Plugin plugin, Map<String, ItemStack> items, GuiAsyncListConfig config, boolean renderOnOpen, Function<GuiInstance<GuiData>, CompletableFuture<Collection<T>>> objectsSupplier, GuiListComponent.SelectHandler<GuiData, T> selectHandler, Function<T, CompletableFuture<ItemStack>> itemRenderer) {
        this.plugin = plugin;
        this.objectsSupplier = objectsSupplier;
        this.selectHandler = selectHandler;
        this.itemRenderer = itemRenderer;
        this.renderOnOpen = renderOnOpen;
        this.displaySlots.addAll(config.displaySlots());
        this.nextPageSlot = config.nextPageSlot();
        this.nextPageItem = items.get(config.nextPageItem());
        this.previousPageSlot = config.previousPageSlot();
        this.previousPageItem = items.get(config.previousPageItem());
        this.loadingSlot = config.loadingIconSlot();
        this.loadingItem = items.get(config.loadingIconItem());
        this.nextPageSound = config.buildNextPageSound();
        this.previousPageSound = config.buildPreviousPageSound();
        this.selectSound = config.buildSelectSound();
    }

    public void render(GuiInstance<GuiData> instance) {
        GuiAsyncListState state = instance.getDataOrCompute(this, GuiAsyncListState::new);
        instance.clearClickHandlers(displaySlots);
        for (int slot : displaySlots) instance.getInventory().clear(slot);

        if (loadingSlot != -1) instance.getInventory().setItem(loadingSlot, loadingItem);
        int renderCount = state.incrementAndGetRenderCount();

        long page = state.getPage();
        long objectsPerPage = displaySlots.size();

        objectsSupplier.apply(instance).thenAccept(list -> {
            long maxPage = list.size() / objectsPerPage;

            Map<Integer, Map.Entry<T, CompletableFuture<ItemStack>>> stateMap = new HashMap<>();
            int i = 0;
            for (T obj : list.stream()
                    .skip(objectsPerPage * page)
                    .limit(objectsPerPage)
                    .toList()) {
                int slot = displaySlots.get(i++);
                stateMap.put(slot, Map.entry(obj, itemRenderer.apply(obj)));
            }

            Runnable whenDone = () -> {
                if (state.getRenderCount() > renderCount) return;
                if (loadingSlot != -1) instance.getInventory().setItem(loadingSlot, instance.getBlueprint().getDefaultItem(loadingSlot));

                if (previousPageSlot != -1) {
                    instance.clearClickHandler(previousPageSlot);
                    instance.getInventory().setItem(previousPageSlot, instance.getBlueprint().getDefaultItem(previousPageSlot));
                    if (page > 0) {
                        instance.getInventory().setItem(previousPageSlot, previousPageItem);
                        instance.setClickHandler(previousPageSlot, (ins, event) -> {
                            if (previousPageSound != null) ins.playSound(previousPageSound);
                            GuiAsyncListState s = ins.getDataOrCompute(this, GuiAsyncListState::new);
                            s.setPage(s.getPage() - 1);
                            render(ins);
                        });
                    }
                }

                if (nextPageSlot != -1) {
                    instance.clearClickHandler(nextPageSlot);
                    instance.getInventory().setItem(nextPageSlot, instance.getBlueprint().getDefaultItem(nextPageSlot));
                    if (page < maxPage) {
                        instance.getInventory().setItem(nextPageSlot, nextPageItem);
                        instance.setClickHandler(nextPageSlot, (ins, event) -> {
                            if (nextPageSound != null) ins.playSound(nextPageSound);
                            GuiAsyncListState s = ins.getDataOrCompute(this, GuiAsyncListState::new);
                            s.setPage(s.getPage() + 1);
                            render(ins);
                        });
                    }
                }

                for (Map.Entry<Integer, Map.Entry<T, CompletableFuture<ItemStack>>> entry : stateMap.entrySet()) {
                    instance.getInventory().setItem(entry.getKey(), entry.getValue().getValue().join());
                    instance.setClickHandler(entry.getKey(), (ins, event) -> {
                        if (selectSound != null) ins.playSound(selectSound);
                        selectHandler.handle(event, instance, entry.getValue().getKey());
                    });
                }
            };

            if (Bukkit.isPrimaryThread() && stateMap.values().stream()
                    .map(Map.Entry::getValue)
                    .map(CompletableFuture::isDone)
                    .reduce(true, (a, b) -> a && b)) whenDone.run();
            else CompletableFuture.runAsync(() -> {
                for (CompletableFuture<ItemStack> future : stateMap.values().stream()
                        .map(Map.Entry::getValue)
                        .toList()) future.join();
                Bukkit.getScheduler().runTask(plugin, whenDone);
            });
        });
    }

    @Override
    public void handleOpen(GuiInstance<GuiData> instance, Player player) {
        if (renderOnOpen) render(instance);
    }

    @Override
    public void setup(GuiInstance<GuiData> instance) {
        render(instance);
    }
}
