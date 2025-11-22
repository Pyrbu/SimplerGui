package lol.pyr.simplergui.components.cycler;

import java.util.List;

public interface GuiCyclerConfig {
    default int slot() {
        return 0;
    }

    default String baseItem() {
        return "";
    }

    default String modeFormat() {
        return "";
    }

    default String modeSelectedFormat() {
        return "";
    }

    default List<String> extraLore() {
        return List.of();
    }
}
