package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.Upgrade;

import java.util.List;
import java.util.Optional;

/**
 * Represents the settings for upgrades in the More Bees API.
 * This class contains a list of available upgrades and provides methods to access them.
 */
public record UpgradeSettings(List<Upgrade> upgrades) implements Settings {

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
