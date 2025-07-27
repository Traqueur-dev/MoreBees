package fr.traqueur.morebees.api.settings;

import de.exlll.configlib.Comment;
import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Global settings for the MoreBees plugin.
 * This class holds the configuration for bees, bee tools, and other global settings.
 */
public record GlobalSettings(boolean debug,
                             @Comment("Define the name of the flying animation for all bee model") String flyAnimation,
                             @Comment("Field \"model\" permits to use a custom model from model engine remove it to not use a custom model") List<BeeType> bees,
                             @Comment("This key represent the beebox tool to get bees inside") ItemStackWrapper beeBox,
                             int beeBoxSize,
                             @Comment("This key represent the bee jar tool to get one bee inside") ItemStackWrapper beeJar,
                             @Comment("This key represent the additonal lore for beehive patch") @Nullable List<String> beehiveLore
                             ) implements Settings {

    /**
     * Default global settings supplier.
     * This supplier provides a default configuration for the MoreBees plugin.
     * It includes a list of bee types, bee box and jar configurations, and beehive lore.
     */
    public static final Supplier<GlobalSettings> DEFAULT = () -> {
        List<BeeType> bees = getBeeTypeList();

        ItemStackWrapper beeBox = new ItemStackWrapper(Material.PAPER.name(), "Bee box", List.of("This is a beebox", "%bees%"));
        ItemStackWrapper beeJar = new ItemStackWrapper(Material.GLASS_BOTTLE.name(), "Bee jar", List.of("This is a bee jar", "%bee%"));

        List<String> beehiveLore = List.of(
                "This is a beehive patch",
                "You can use it to patch your beehive",
                "It will add a new lore to your beehive",
                "production multiplier: %production-multiplier%",
                "produce-blocks: %produce-blocks%",
                "max-bees: %max-bees%"
        );

        return new GlobalSettings(true, "flying", bees, beeBox, 10, beeJar, beehiveLore);
    };

    /**
     * Creat the list of default bee types.
     * This method initializes a list of predefined bee types with their respective properties.
     * Each bee type includes a unique identifier, a display name, and associated materials for production,
     * block representation, and ore type.
     * The list includes various types such as Redstone Bee, Emerald Bee, Diamond Bee,
     * Gold Bee, and Iron Bee, each with specific characteristics.
     * @return A list of default BeeType objects representing different bee types.
     */
    private static @NotNull List<BeeType> getBeeTypeList() {
        List<BeeType> bees = new ArrayList<>();
        bees.add(new BeeType("redstone-bee", 1,"<red>Redstone Bee", List.of(Material.REDSTONE.name()), List.of(Material.REDSTONE_BLOCK.name()), Material.REDSTONE_ORE.name(), "redstone-bee"));
        bees.add(new BeeType("emerald-bee", null, "<green>Emerald Bee", List.of(Material.EMERALD.name()), List.of(Material.EMERALD_BLOCK.name()), Material.EMERALD_ORE.name(), null));
        bees.add(new BeeType("diamond-bee", null,"<aqua>Diamond Bee",List.of(Material.DIAMOND.name()), List.of(Material.DIAMOND_BLOCK.name()), Material.DIAMOND_ORE.name(),null));
        bees.add(new BeeType("gold-bee",null,"<gold>Gold Bee", List.of(Material.GOLD_INGOT.name()), List.of(Material.GOLD_BLOCK.name()), Material.GOLD_ORE.name(), null));
        bees.add(new BeeType("iron-bee", null,"<gray>Iron Bee", List.of(Material.IRON_INGOT.name()), List.of(Material.IRON_BLOCK.name()), Material.IRON_ORE.name(), null));
        return bees;
    }

    /**
     * Retrieves a bee type by its identifier.
     * This method searches through the list of bee types and returns the one that matches the provided
     * type identifier.
     * If no matching bee type is found, it returns an empty Optional.
     * @param type The identifier of the bee type to retrieve.
     * @return An Optional containing the BeeType if found, otherwise empty.
     */
    public Optional<BeeType> getBeeType(String type) {
        return bees.stream()
                .filter(b -> b.type().equals(type))
                .findFirst();
    }

    /**
     * Checks if the global settings contain all specified bee types.
     * This method verifies if the provided bee types are present in the global settings.
     * It returns true if all specified types are found, otherwise false.
     * @param type The bee types to check for.
     * @return True if all specified bee types are present, false otherwise.
     */
    public boolean contains(String... type) {
        for (String t : type) {
            if (bees.stream().noneMatch(b -> b.type().equals(t))) {
                return false;
            }
        }
        return true;
    }

}
