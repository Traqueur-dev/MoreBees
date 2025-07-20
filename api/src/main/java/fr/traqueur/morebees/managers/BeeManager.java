package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.Manager;
import fr.traqueur.morebees.models.BeeType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface BeeManager extends Manager {
    Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack);

    Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity);

    void spawnBee(Location location, BeeType beeType, CreatureSpawnEvent.SpawnReason reason, boolean baby);

    BeeType computeBreed(BeeType mother, BeeType father);
}
