package fr.traqueur.morebees.api.util;

import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class Util {

    public static <A,B> void ifBothPresent(Optional<A> a, Optional<B> b, BiConsumer<A, B> consumer) {
        if (a.isPresent() && b.isPresent()) {
            consumer.accept(a.get(), b.get());
        }
    }

    public static boolean isValidBlock(Block block, List<String> validTypes) {
        Material type = block.getType();
        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        String itemName = hooks.stream().map(hook -> hook.getBlockName(block)).filter(Objects::nonNull).findFirst().orElse(type.name());
        return validTypes.contains(itemName);
    }

}
