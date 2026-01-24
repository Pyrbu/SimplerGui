package lol.pyr.simplergui;

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
}
