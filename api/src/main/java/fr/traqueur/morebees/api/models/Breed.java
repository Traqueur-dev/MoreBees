package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.structura.api.Loadable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Breed(List<String> parents, String child, double chance) implements Loadable {

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

        Set<String> types = new HashSet<>(parents);
        types.add(child);

        if (!BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).contains(types.toArray(String[]::new))) {
            Logger.warning("Some bee types in breed {} are not defined in settings: {}", child, types);
        }
    }
}
