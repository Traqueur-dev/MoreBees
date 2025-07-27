package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Mutation;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The BeeManager interface provides methods for managing bees in the MoreBees plugin.
 * It allows for retrieving bee types from items or entities, spawning bees, patching bees,
 * computing child types, feeding bees, and mutating them.
 */
public interface BeeManager extends Manager {

    /**
     * Retrieves the BeeType from an ItemStack if it contains a bee egg.
     *
     * @param itemStack the ItemStack to check
     * @return an Optional containing the BeeType if found, otherwise empty
     */
    Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack);

    /**
     * Retrieves the BeeType from a LivingEntity if it is a bee.
     *
     * @param entity the LivingEntity to check
     * @return an Optional containing the BeeType if found, otherwise empty
     */
    Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity);

    /**
     * Spawns a bee at the specified location with the given BeeType and spawn reason.
     *
     * @param location the location to spawn the bee
     * @param beeType the type of bee to spawn
     * @param reason the reason for spawning the bee
     * @param baby whether the bee should be a baby
     * @param nectar whether the bee should have nectar
     */
    void spawnBee(Location location, @NotNull BeeType beeType, CreatureSpawnEvent.SpawnReason reason, boolean baby, boolean nectar);

    /**
     * Patches a bee with the specified BeeType.
     * <p>
     * Patch a bee to apply all goals and properties of the BeeType to the bee.
     * </p>
     * @param bee the bee to patch
     * @param beeType the BeeType to apply to the bee
     */
    void patchBee(Bee bee, @NotNull BeeType beeType);

    /**
     * Computes the child type of bee based on its parents' types.
     *
     * @param mother the BeeType of the mother
     * @param father the BeeType of the father
     * @return the computed child BeeType
     */
    @NotNull BeeType computeChildType(@NotNull BeeType mother, @NotNull BeeType father);

    /**
     * Feeds a bee by a player, which may trigger certain behaviors or effects.
     *
     * @param player the player feeding the bee
     * @param bee the bee to be fed
     */
    void feed(@NotNull Player player, Bee bee);

    /**
     * Mutates a bee to a new location with the specified mutation.
     *
     * @param bee the bee to mutate
     * @param mutation the mutation to apply
     * @param location the location to mutate the bee to
     */
    void mutate(Bee bee, Mutation mutation, Location location);
}
