package lol.pyr.simplergui.components.asynclist;

import lol.pyr.simplergui.components.list.GuiListConfig;

public interface GuiAsyncListConfig extends GuiListConfig {
    default int loadingIconSlot() {
        return 0;
    }

    default String loadingIconItem() {
        return "";
    }
}
