package lol.pyr.simplergui.components.iteminput;

import java.util.List;

public interface GuiItemInputConfig {
    default List<Integer> slots() {
        return List.of();
    }
}
