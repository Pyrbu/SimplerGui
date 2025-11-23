package lol.pyr.simplergui.components.iteminput;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import space.arim.dazzleconf.engine.CallableFn;

import java.util.List;

public interface GuiItemInputConfig {
    default List<Integer> slots() {
        return List.of();
    }

    default String addItemSound() {
        return "";
    }

    default float addItemVolume() {
        return 1;
    }

    default float addItemPitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildAddSound() {
        if (addItemSound().isEmpty()) return null;
        return Sound.sound(
                Key.key(addItemSound()),
                Sound.Source.MASTER,
                addItemVolume(),
                addItemPitch()
        );
    }

    default String removeItemSound() {
        return "";
    }

    default float removeItemVolume() {
        return 1;
    }

    default float removeItemPitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildRemoveSound() {
        if (removeItemSound().isEmpty()) return null;
        return Sound.sound(
                Key.key(removeItemSound()),
                Sound.Source.MASTER,
                removeItemVolume(),
                removeItemPitch()
        );
    }
}
