package fr.traqueur.morebees.api.models;

import java.util.List;

public record Breed(List<String> parents, String child, double chance) {

    public Breed {
        if (parents.size() != 2) {
            throw new IllegalArgumentException("A breed must have exactly two parents.");
        }
        if (child == null || child.isEmpty()) {
            throw new IllegalArgumentException("Child cannot be null or empty.");
        }
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("Chance must be between 0 and 1.");
        }
    }
}
