package fr.traqueur.morebees.models;

import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;

public record BeeDataImpl(BeeType type, boolean hasNectar, boolean isAdult) implements BeeData {
}
