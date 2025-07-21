package fr.traqueur.morebees.api.util;

import fr.traqueur.morebees.api.BeePlugin;

import java.util.function.Function;

public class Formatter {

    private final String pattern;
    private final Function<BeePlugin, String> supplier;

    public static Formatter[] all(Object... objects) {
        if(objects.length % 2 != 0) {
            throw new IllegalArgumentException("You must provide an even number of arguments (pattern, supplier) pairs.");
        }
        Formatter[] formatters = new Formatter[objects.length / 2];
        for (int i = 0; i < objects.length; i += 2) {
            if (!(objects[i] instanceof String)) {
                throw new IllegalArgumentException("Each pattern must be a String and each supplier must be a Object.");
            }
            formatters[i / 2] = new Formatter((String) objects[i], objects[i + 1]);
        }
        return formatters;
    }

    private Formatter(String pattern, Object supplier) {
        this.pattern = pattern;
        this.supplier = (api) -> supplier.toString();
    }

    private Formatter(String pattern, Function<BeePlugin, String> supplier) {
        this.pattern = pattern;
        this.supplier = supplier;
    }

    public static Formatter format(String pattern, Object supplier) {
        return new Formatter(pattern, supplier);
    }

    public static Formatter format(String pattern, Function<BeePlugin, String> supplier) {
        return new Formatter(pattern, supplier);
    }

    public String handle(BeePlugin api, String message) {
        String content = this.supplier.apply(api);
        return message.replaceAll("%" + this.pattern + "%", content);
    }
}