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

public interface BeeManager extends Manager {

    Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack);

    Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity);

    void spawnBee(Location location, BeeType beeType, CreatureSpawnEvent.SpawnReason reason, boolean baby, boolean nectar);

    void patchBee(Bee bee, BeeType beeType);

    BeeType computeChildType(BeeType mother, BeeType father);

    void feed(@NotNull Player player, Bee bee);

    void mutate(Bee bee, Mutation mutation, Location to);
}
