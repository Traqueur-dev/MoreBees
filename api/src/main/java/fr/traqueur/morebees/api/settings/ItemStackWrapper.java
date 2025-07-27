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

public record ItemStackWrapper(String material, @Nullable String name, @Nullable List<String> lore) {

    public static final ItemStackWrapper EMPTY = new ItemStackWrapper("AIR", null, null);

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
