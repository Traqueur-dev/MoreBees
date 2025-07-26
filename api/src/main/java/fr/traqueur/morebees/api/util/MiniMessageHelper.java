package fr.traqueur.morebees.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class MiniMessageHelper {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static String unparse(@NotNull Component component) {
        return  MINI_MESSAGE.serialize(component);
    }
}
