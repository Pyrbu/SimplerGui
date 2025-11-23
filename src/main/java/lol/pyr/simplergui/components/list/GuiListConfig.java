package lol.pyr.simplergui.components.list;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import space.arim.dazzleconf.engine.CallableFn;

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

    default String nextPageSound(){
        return "";
    }

    default float nextPageVolume() {
        return 1;
    }

    default float nextPagePitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildNextPageSound() {
        if (nextPageSound().isEmpty()) return null;
        return Sound.sound(
                Key.key(nextPageSound()),
                Sound.Source.MASTER,
                nextPageVolume(),
                nextPagePitch()
        );
    }

    default int previousPageSlot() {
        return 0;
    }

    default String previousPageItem() {
        return "";
    }

    default String previousPageSound() {
        return "";
    }

    default float previousPageVolume() {
        return 1;
    }

    default float previousPagePitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildPreviousPageSound() {
        if (previousPageSound().isEmpty()) return null;
        return Sound.sound(
                Key.key(previousPageSound()),
                Sound.Source.MASTER,
                previousPageVolume(),
                previousPagePitch()
        );
    }

    default String selectSound() {
        return "";
    }

    default float selectVolume() {
        return 1;
    }

    default float selectPitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildSelectSound() {
        if (selectSound().isEmpty()) return null;
        return Sound.sound(
                Key.key(selectSound()),
                Sound.Source.MASTER,
                selectVolume(),
                selectPitch()
        );
    }
}

