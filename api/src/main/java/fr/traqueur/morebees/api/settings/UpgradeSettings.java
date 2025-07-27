package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.Upgrade;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents the settings for upgrades in the More Bees API.
 * This class contains a list of available upgrades and provides methods to access them.
 */
public record UpgradeSettings(List<Upgrade> upgrades) implements Settings {

    /**
     * A default supplier for UpgradeSettings that provides a predefined list of upgrades.
     * This can be used to initialize the settings with some default values.
     */
    public static Supplier<UpgradeSettings> DEFAULT = () -> {
        List<Upgrade> upgrades = new ArrayList<>();

        upgrades.add(new Upgrade("level-1",
                new ItemStackWrapper(Material.COPPER_INGOT.name(),
                        "Level 1 Upgrade",
                        List.of("This is level 1 beehive upgrade",
                                "max bees: %max-bees%",
                                "production-multiplier: %production-multiplier%",
                                "produce-blocks: %produce-blocks%")),
                3,
                1.5,
                false)
        );
        upgrades.add(new Upgrade("level-2",
                new ItemStackWrapper(Material.IRON_INGOT.name(),
                        "Level 2 Upgrade",
                        List.of("This is level 2 beehive upgrade",
                                "max bees: %max-bees%",
                                "production-multiplier: %production-multiplier%",
                                "produce-blocks: %produce-blocks%")),
                6,
                2.0,
                false)
        );
        upgrades.add(new Upgrade("level-3",
                new ItemStackWrapper(Material.GOLD_INGOT.name(),
                        "Level 3 Upgrade",
                        List.of("This is level 3 beehive upgrade",
                                "max bees: %max-bees%",
                                "production-multiplier: %production-multiplier%",
                                "produce-blocks: %produce-blocks%")),
                6,
                1,
                true)
        );

        return new UpgradeSettings(upgrades);
    };

    /**
     * Retrieves an upgrade by its ID.
     *
     * @param id the ID of the upgrade to retrieve
     * @return an Optional containing the Upgrade if found, or empty if not found
     */
    public Optional<Upgrade> getUpgrade(String id) {
        return upgrades.stream().filter(upgrade -> upgrade.id().equals(id)).findFirst();
    }
}
