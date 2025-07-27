package fr.traqueur.morebees.api.models;

/**
 * Represents the data of a bee, including its type, nectar status, and age.
 * This interface is used to encapsulate the properties of a bee in the MoreBees API.
 */
public interface BeeData {

    /**
     * Gets the type of the bee.
     *
     * @return the type of the bee
     */
    BeeType type();

    /**
     * Checks if the bee has nectar.
     *
     * @return true if the bee has nectar, false otherwise
     */
    boolean hasNectar();

    /**
     * Checks if the bee is an adult.
     *
     * @return true if the bee is an adult, false otherwise
     */
    boolean isAdult();

}
