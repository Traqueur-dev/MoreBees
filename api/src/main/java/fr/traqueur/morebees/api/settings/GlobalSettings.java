package fr.traqueur.morebees.api.settings;

import de.exlll.configlib.Comment;
import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record GlobalSettings(boolean debug,
                             @Comment("Define the name of the flying animation for all bee model") String flyAnimation,
                             @Comment("Field \"model\" permits to use a custom model from model engine remove it to not use a custom model") List<BeeType> bees) implements Settings {

    public static final Supplier<GlobalSettings> DEFAULT = () -> {
        List<BeeType> bees = new ArrayList<>();
        bees.add(new BeeType("redstone-bee", 1,"<red>Redstone Bee", List.of(Material.REDSTONE.name()), List.of(Material.REDSTONE_BLOCK.name()), Material.REDSTONE_ORE.name(), "redstone-bee"));
        bees.add(new BeeType("emerald-bee", 2, "<green>Emerald Bee", List.of(Material.EMERALD.name()), List.of(Material.EMERALD_BLOCK.name()), Material.EMERALD_ORE.name(), null));
        bees.add(new BeeType("diamond-bee", 3,"<aqua>Diamond Bee",List.of(Material.DIAMOND.name()), List.of(Material.DIAMOND_BLOCK.name()), Material.DIAMOND_ORE.name(),null));
        bees.add(new BeeType("gold-bee",4,"<gold>Gold Bee", List.of(Material.GOLD_INGOT.name()), List.of(Material.GOLD_BLOCK.name()), Material.GOLD_ORE.name(), null));
        bees.add(new BeeType("iron-bee", 5,"<gray>Iron Bee", List.of(Material.IRON_INGOT.name()), List.of(Material.IRON_BLOCK.name()), Material.IRON_ORE.name(), null));
        return new GlobalSettings(true, "flying", bees);
    };

    public Optional<BeeType> getBeeType(String type) {
        return bees.stream()
                .filter(b -> b.type().equals(type))
                .findFirst();
    }

    public boolean contains(String... type) {
        for (String t : type) {
            if (bees.stream().noneMatch(b -> b.type().equals(t))) {
                return false;
            }
        }
        return true;
    }

}
