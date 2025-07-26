package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public record ItemStackWrapper(String material, @Nullable String name, @Nullable List<String> lore) {

    public ItemStack build(Formatter... formatters) {
        ItemStack base = Util.getItemFromId(material);
        base.editMeta(meta -> {
            if (name != null)
                meta.itemName(MiniMessageHelper.parse(format(name, formatters)));

            if (lore != null && !lore.isEmpty()) {
                List<Component> formattedLore = lore.stream()
                        .map(line -> format(line, formatters))
                        .filter(Objects::nonNull)
                        .map(MiniMessageHelper::parse)
                        .toList();
                meta.lore(formattedLore);
            }
        });
        return base;
    }

    private String format(String text, Formatter... formatters) {
        if (text == null) return null;
        for (Formatter formatter : formatters) {
            text = formatter.handle(BeePlugin.getPlugin(BeePlugin.class), text);
        }
        return text;
    }

}
