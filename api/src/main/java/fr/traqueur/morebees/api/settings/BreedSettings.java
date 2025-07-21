package fr.traqueur.morebees.api.settings;

import fr.traqueur.morebees.api.models.Breed;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record BreedSettings(List<Breed> breeds) implements Settings {

    public static final Supplier<BreedSettings> DEFAULT = () -> {
        List<Breed> breeds = new ArrayList<>();

        breeds.add(new Breed(List.of("redstone-bee", "diamond-bee"), "emerald-bee", 1));

        return new BreedSettings(breeds);
    };
}
