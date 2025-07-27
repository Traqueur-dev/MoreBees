package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.Upgrade;
import org.bukkit.block.Beehive;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Manages upgrades for beehives.
 * Provides methods to retrieve upgrades from items, create displays for upgrades,
 * and load beehive states with associated entities.
 */
public interface UpgradesManager extends Manager {

    /**
     * Retrieves an upgrade from the given item stack.
     *
     * @param itemStack the item stack to check for an upgrade
     * @return an Optional containing the Upgrade if found, otherwise empty
     */
    Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack);

    /**
     * Creates an ItemDisplay for the given upgrade on the specified beehive block.
     *
     * @param beehiveBlock the block representing the beehive
     * @param upgrade      the upgrade to display
     * @return an ItemDisplay representing the upgrade
     */
    ItemDisplay createUpgradeDisplay(Block beehiveBlock, Upgrade upgrade);

    /**
     * Removes the upgrade display associated with the given UUID.
     *
     * @param displayUUID the UUID of the ItemDisplay to remove
     */
    void removeUpgradeDisplay(UUID displayUUID);

    /**
     * Loads the beehive state and associates it with the given entities.
     *
     * @param beehiveState the Beehive state to load
     * @param entities     a stream of entities to associate with the beehive
     */
    void loadBeehive(Beehive beehiveState, Stream<Entity> entities);
}
