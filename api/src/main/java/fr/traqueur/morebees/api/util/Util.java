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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Utility class providing various helper methods for the MoreBees API.
 * This class includes methods for handling optional values, validating blocks,
 * retrieving items by ID, parsing lore, and giving items to players.
 */
public class Util {

    /**
     * Checks if both optional values are present and applies the given consumer to them.
     *
     * @param a        the first optional value
     * @param b        the second optional value
     * @param consumer the consumer to apply if both values are present
     * @param <A>      the type of the first optional value
     * @param <B>      the type of the second optional value
     */
    public static <A,B> void ifBothPresent(Optional<A> a, Optional<B> b, BiConsumer<A, B> consumer) {
        if (a.isPresent() && b.isPresent()) {
            consumer.accept(a.get(), b.get());
        }
    }

    /**
     * Checks if the given block is valid based on the provided list of valid types.
     *
     * @param block       the block to check
     * @param validTypes  a list of valid block types
     * @return true if the block is valid, false otherwise
     */
    public static boolean isValidBlock(Block block, List<String> validTypes) {
        Material type = block.getType();
        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        String itemName = hooks.stream().map(hook -> hook.getBlockName(block)).filter(Objects::nonNull).findFirst().orElse(type.name());
        return validTypes.contains(itemName);
    }

    /**
     * Retrieves an ItemStack from the given ID, using registered ItemProviderHooks.
     * If no hook provides the item, it defaults to creating an ItemStack from the Material.
     *
     * @param id the ID of the item
     * @return the ItemStack corresponding to the ID
     */
    public static ItemStack getItemFromId(String id) {
        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        return hooks.stream()
                .map(hook -> hook.getItemFromId(id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ItemStack.of(Material.valueOf(id)));
    }

    /**
     * Parses a list of lore strings into a list of Adventure Components.
     * Each line is formatted using the provided formatters, split by new lines,
     * and parsed into components with italic text decoration disabled.
     *
     * @param lore       the list of lore strings to parse
     * @param formatters optional formatters to apply to each line
     * @return a list of parsed Adventure Components
     */
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

    /**
     * Gives an item to a player, dropping it at the player's location if the inventory is full.
     *
     * @param player   the player to give the item to
     * @param toGive   the ItemStack to give
     */
    public static void giveItem(Player player, @NotNull ItemStack toGive) {
        player.getInventory().addItem(toGive).forEach((slot, item) -> {
            Item itemDropped = player.getWorld().dropItem(player.getLocation(), item);
            itemDropped.setOwner(player.getUniqueId());
        });
    }
}
