package fr.traqueur.morebees.api.models;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a beehive in the MoreBees API.
 * This interface provides methods to manage upgrades, honey comb counts, and honey operations.
 */
public interface Beehive {

    /**
     * Gets the upgrade associated with this beehive.
     *
     * @return the upgrade
     */
    @NotNull Upgrade getUpgrade();

    /**
     * Sets the upgrade for this beehive.
     *
     * @param upgrade the upgrade to set
     */
    void setUpgrade(@NotNull Upgrade upgrade);

    /**
     * Gets the unique identifier of the item display for the upgrade.
     *
     * @return the UUID of the upgrade
     */
    @Nullable UUID getUpgradeId();

    /**
     * Sets the unique identifier of the item display for the upgrade.
     *
     * @param upgradeId the UUID to set
     */
    void setUpgradeId(@Nullable UUID upgradeId);

    /**
     * Gets the count of honey combs for each bee type in this beehive.
     *
     * @return a map of bee types to their respective honey comb counts
     */
    Map<BeeType, Integer> getHoneyCombCounts();

    /**
     * Gets the count of honey combs for a specific bee type.
     *
     * @param beeType the type of bee
     * @return the count of honey combs for the specified bee type
     */
    int getHoneyCombCount(@NotNull BeeType beeType);

    /**
     * Adds honey combs for a specific bee type.
     *
     * @param beeType the type of bee
     * @param count   the number of honey combs to add
     */
    void addHoney(@NotNull BeeType beeType, int count);

    /**
     * Removes honey combs for a specific bee type.
     *
     * @param beeType the type of bee
     * @param count       the number of honey combs to remove
     */
    void removeHoney(@NotNull BeeType beeType, int count);

    @NotNull ItemStack patch(@NotNull ItemStack item);
}
