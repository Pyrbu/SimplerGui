package lol.pyr.simplergui.components.asynclist;

public class GuiAsyncListState {
    private long page = 0;
    private int renderCount = 0;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getRenderCount() {
        return renderCount;
    }

    public int incrementAndGetRenderCount() {
        return ++renderCount;
    }
}
