package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.structura.annotations.Options;

import java.util.List;
import java.util.Optional;

/**
 * Global settings for the MoreBees plugin.
 * This class holds the configuration for bees, bee tools, and other global settings.
 */
public record GlobalSettings(boolean debug,
                             String flyAnimation,
                             List<BeeType> bees,
                             ItemStackWrapper beeBox,
                             int beeBoxSize,
                             ItemStackWrapper beeJar,
                             @Options(optional = true) List<String> beehiveLore
                             ) implements Settings {

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
