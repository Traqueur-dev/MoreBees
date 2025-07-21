package fr.traqueur.morebees.api.util;

import java.util.Optional;
import java.util.function.BiConsumer;

public class Util {

    public static <A,B> void ifBothPresent(Optional<A> a, Optional<B> b, BiConsumer<A, B> consumer) {
        if (a.isPresent() && b.isPresent()) {
            consumer.accept(a.get(), b.get());
        }
    }

}
