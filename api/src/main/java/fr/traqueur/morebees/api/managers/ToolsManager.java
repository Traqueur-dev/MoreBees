package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Bee;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface ToolsManager extends Manager {

    Optional<Tool> getTool(ItemStack itemStack);

    boolean isFull(ItemStack itemStack);

    BeeData toData(Bee bee, BeeType beeType);

    void catchBee(ItemStack tool, Bee bee, BeeType beeType);

    List<BeeData> releaseBee(ItemStack tool, boolean all);

    enum Tool {
        BEE_BOX(() -> BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeBoxSize(),
                () ->  BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeBox().lore(),
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
                () ->  BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beeJar().lore(),
                "bee",
                (placeholder, bees) -> {
                    String template = Messages.BEE_JAR_CONTENT.raw();

                    if(bees.isEmpty())
                        return Formatter.all(placeholder, Messages.EMPTY_BEE_JAR.raw());

                    String row = Formatter.format(template, Formatter.all("beetype", bees.stream().map(BeeData::type).toList().getFirst().displayName()));

                    return Formatter.all(placeholder, row);
                });

        private final Supplier<Integer> maxBees;
        private final Supplier<List<String>> lore;
        private final BiFunction<String,List<BeeData>, Formatter[]> formatters;
        private final String placeholder;

        Tool(Supplier<Integer> maxBees, Supplier<List<String>> lore, String placeholder, BiFunction<String,List<BeeData>, Formatter[]> formatters) {
            this.maxBees = maxBees;
            this.lore = lore;
            this.placeholder = placeholder;
            this.formatters = formatters;
        }

        public int maxBees() {
            return this.maxBees.get();
        }

        public List<Component> lore(List<BeeData> bees) {
            List<String> lore = this.lore.get();
            if(lore == null) {
                return List.of();
            }
            return lore.stream()
                    .map(line -> Formatter.format(line, this.formatters.apply(this.placeholder, bees)))
                    .filter(Objects::nonNull)
                    .flatMap(line -> Arrays.stream(line.split("\n")))
                    .filter(s -> !s.isEmpty())
                    .map(MiniMessageHelper::parse)
                    .map(component -> component.decoration(TextDecoration.ITALIC, false))
                    .toList();
        }
    }

}
