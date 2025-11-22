package lol.pyr.simplergui.components.list;

import java.util.List;

public interface GuiListConfig {
    default List<Integer> displaySlots() {
        return List.of();
    }

    default int nextPageSlot() {
        return 0;
    }

    default String nextPageItem() {
        return "";
    }

    default int previousPageSlot() {
        return 0;
    }

    default String previousPageItem() {
        return "";
    }
}

