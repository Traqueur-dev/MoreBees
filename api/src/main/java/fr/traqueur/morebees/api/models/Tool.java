package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.settings.ItemStackWrapper;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum Tool {
        BEE_BOX(() -> BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeBoxSize(),
                () ->  BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeBox(),
                "bees",
                (placeholder, bees) -> {
                    String template = Messages.BEE_BOX_CONTENT.raw();
                    Map<BeeType, Long> beeCount = new HashMap<>();

                    boolean find = false;
                    for (BeeData bee : bees) {
                        BeeType type = bee.type();
                        for (Map.Entry<BeeType, Long> beeTypeLongEntry : beeCount.entrySet()) {
                            BeeType key = beeTypeLongEntry.getKey();
                            if(key.type().equals(type.type())) {
                                beeCount.merge(key, 1L, Long::sum);
                                find = true;
                                break;
                            }
                        }

                        if(!find) {
                            beeCount.put(type, 1L);
                        }
                        find = false;
                    }

                    StringBuilder builder = new StringBuilder();
                    beeCount.forEach((type, count) -> {
                        if (!builder.isEmpty()) {
                            builder.append("\n");
                        }
                        builder.append(Formatter.format(template, Formatter.all("beetype", type.displayName(), "amount", count)));
                    });

                    if (builder.isEmpty()) {
                        builder.append(Messages.EMPTY_BEE_BOX.raw());
                    }

                    return Formatter.all(placeholder, builder.toString());
                }),

        BEE_JAR(() -> 1,
                () ->  BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeJar(),
                "bee",
                (placeholder, bees) -> {
                    String template = Messages.BEE_JAR_CONTENT.raw();

                    if(bees.isEmpty())
                        return Formatter.all(placeholder, Messages.EMPTY_BEE_JAR.raw());

                    String row = Formatter.format(template, Formatter.all("beetype", bees.stream().map(BeeData::type).toList().getFirst().displayName()));

                    return Formatter.all(placeholder, row);
                });

        private final Supplier<Integer> maxBees;
        private final Supplier<ItemStackWrapper> itemStackSupplier;
        private final BiFunction<String, List<BeeData>, Formatter[]> formatters;
        private final String placeholder;

        Tool(Supplier<Integer> maxBees, Supplier<ItemStackWrapper> itemStackSupplier, String placeholder, BiFunction<String,List<BeeData>, Formatter[]> formatters) {
            this.maxBees = maxBees;
            this.itemStackSupplier = itemStackSupplier;
            this.placeholder = placeholder;
            this.formatters = formatters;
        }

        public int maxBees() {
            return this.maxBees.get();
        }

        public ItemStack itemStack(List<BeeData> bees) {
            ItemStackWrapper itemStack = itemStackSupplier.get();
            return itemStack.build(this.formatters.apply(this.placeholder, bees));
        }

        public List<Component> lore(List<BeeData> bees) {
            List<String> lore = this.itemStackSupplier.get().lore();
            if(lore == null) {
                return List.of();
            }
            return Util.parseLore(lore, this.formatters.apply(this.placeholder, bees));
        }
    }