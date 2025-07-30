package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Breed;
import fr.traqueur.morebees.api.models.Mutation;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Optional;

/**
 * Represents the settings for breeding bees, including the breeds and mutations available.
 * This class provides methods to retrieve mutations based on parent bee types and blocks.
 */
public record BreedSettings(List<Breed> breeds, List<Mutation> mutations) implements Settings {

    /**
     * Retrieves the mutation for a given parent bee type and block.
     *
     * @param parent the parent bee type
     * @param block  the block to check for mutation compatibility
     * @return an Optional containing the Mutation if found, otherwise empty
     */
    public Optional<Mutation> getMutation(BeeType parent, Block block) {
        return this.mutations.stream()
                .filter(mutation -> mutation.parent().equals(parent.type()) && mutation.canMutate(block))
                .findFirst();
    }
}
