package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

public interface BeeManager extends Manager {
    boolean isSpawnFromBeehive(UUID beeUUID);

    Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack);

    Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity);

    void spawnBee(Location location, BeeType beeType, CreatureSpawnEvent.SpawnReason reason, boolean baby);

    BeeType computeBreed(BeeType mother, BeeType father);
}
