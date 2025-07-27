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

public interface UpgradesManager extends Manager {
    Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack);

    ItemDisplay createUpgradeDisplay(Block beehiveBlock, Upgrade upgrade);

    void removeUpgradeDisplay(UUID displayUUID);

    void loadBeehive(Beehive beehiveState, Stream<Entity> entities);
}
