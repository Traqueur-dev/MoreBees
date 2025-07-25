package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Breed;
import fr.traqueur.morebees.api.models.Mutation;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record BreedSettings(List<Breed> breeds, List<Mutation> mutations) implements Settings {

    public static final Supplier<BreedSettings> DEFAULT = () -> {
        List<Breed> breeds = new ArrayList<>();

        breeds.add(new Breed(List.of("redstone-bee", "diamond-bee"), "emerald-bee", 1));


        List<Mutation> mutations = new ArrayList<>();
        mutations.add(new Mutation("redstone-bee", "emerald-bee", List.of("REDSTONE_ORE")));

        return new BreedSettings(breeds, mutations);
    };

    public Optional<Mutation> getMutation(BeeType parent, Block block) {
        return this.mutations.stream()
                .filter(mutation -> mutation.parent().equals(parent.type()) && mutation.canMutate(block))
                .findFirst();
    }
}
