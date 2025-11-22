package lol.pyr.simplergui;

import org.bukkit.inventory.ItemStack;
import space.arim.dazzleconf.engine.CallableFn;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public interface GuiBlueprintConfig {
    default String guiName() {
        return "";
    }

    default int guiSize() {
        return 0;
    }

    default Map<String, String> items() {
        return Map.of();
    }

    default Map<Integer, String> defaultLayout() {
        return Map.of();
    }

    @CallableFn
    default Map<String, ItemStack> makeItems() {
        Map<String, ItemStack> map = new HashMap<>();
        for (Map.Entry<String, String> entry : items().entrySet())
            map.put(entry.getKey(), ItemStack.deserializeBytes(Base64.getDecoder().decode(entry.getValue())));
        return map;
    }
}
