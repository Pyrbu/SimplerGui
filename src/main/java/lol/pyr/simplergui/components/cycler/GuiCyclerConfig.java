package lol.pyr.simplergui.components.cycler;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import space.arim.dazzleconf.engine.CallableFn;

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

    default String sound() {
        return "";
    }

    default float volume() {
        return 1;
    }

    default float pitch() {
        return 1;
    }

    @SuppressWarnings("PatternValidation")
    @CallableFn
    default Sound buildSound() {
        if (sound().isEmpty()) return null;
        return Sound.sound(
                Key.key(sound()),
                Sound.Source.MASTER,
                volume(),
                pitch()
        );
    }
}
