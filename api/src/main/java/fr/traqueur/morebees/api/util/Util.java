package fr.traqueur.morebees.api.util;

import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
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

    public static ItemStack getItemFromId(String id) {
        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        return hooks.stream()
                .map(hook -> hook.getItemFromId(id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ItemStack.of(Material.valueOf(id)));
    }

    public static List<Component> parseLore(List<String> lore, Formatter... formatters) {
        return lore.stream()
                .map(line -> Formatter.format(line, formatters))
                .filter(Objects::nonNull)
                .flatMap(line -> Arrays.stream(line.split("\n")))
                .filter(s -> !s.isEmpty())
                .map(MiniMessageHelper::parse)
                .map(component -> component.decoration(TextDecoration.ITALIC, false))
                .toList();
    }

    public static void giveItem(Player player, ItemStack toGive) {
        player.getInventory().addItem(toGive).forEach((slot, item) -> {
            Item itemDropped = player.getWorld().dropItem(player.getLocation(), item);
            itemDropped.setOwner(player.getUniqueId());
        });
    }
}
