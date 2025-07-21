package fr.traqueur.morebees.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageHelper {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

}
