package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a wrapper for an ItemStack with material, name, and lore.
 * This class is used to create ItemStacks with formatted names and lore.
 * It provides a method to build the ItemStack with the specified properties.
 */
public record ItemStackWrapper(String material, @Nullable String name, @Nullable List<String> lore) {

    /**
     * Get the Wrapper for the AIR item.
     * This is a static instance representing an empty ItemStack.
     * It can be used when no specific item is needed.
     */
    public static final ItemStackWrapper EMPTY = new ItemStackWrapper("AIR", null, null);

    /**
     * Constructs an ItemStack with formatters applied to the name and lore.
     *
     * @param formatters The formatters to apply to the name and lore.
     *                   These formatters can be used to replace placeholders in the name and lore.
     * @return An ItemStack with the specified material, name, and lore.
     */
    public ItemStack build(Formatter... formatters) {
        ItemStack base = Util.getItemFromId(material);
        base.editMeta(meta -> {
            if (name != null)
                meta.itemName(MiniMessageHelper.parse(Formatter.format(name, formatters)));

            if (lore != null && !lore.isEmpty()) {
                meta.lore(Util.parseLore(lore, formatters));
            }
        });
        return base;
    }



}
