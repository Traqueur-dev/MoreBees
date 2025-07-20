package fr.traqueur.morebees;

import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.hooks.ModelEngineHook;
import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;
import java.util.Set;

public class BeeListener implements Listener {

    private static final Set<CreatureSpawnEvent.SpawnReason> SPAWN_REASONS = Set.of(
            CreatureSpawnEvent.SpawnReason.BEEHIVE,
            CreatureSpawnEvent.SpawnReason.BREEDING,
            CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
    );

    private final BeePlugin plugin;

    public BeeListener(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        Block spawnBlock = clickedBlock.getRelative(event.getBlockFace());
        Location spawnLocation = spawnBlock.getLocation().add(0.5, 0, 0.5);

        BeeManager beeManager = plugin.getManager(BeeManager.class);
        beeManager.getBeeTypeFromEgg(event.getItem()).ifPresent(type -> {
            event.setCancelled(true);

            beeManager.spawnBee(spawnLocation, type);

            if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
        });
    }

}
