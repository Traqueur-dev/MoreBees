package fr.traqueur.morebees.listeners;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.managers.UpgradesManager;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class UpgradesListener implements Listener {

    private final BeePlugin plugin;

    public UpgradesListener(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        UpgradesManager upgradesManager = this.plugin.getManager(UpgradesManager.class);
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        if(!(clickedBlock.getState() instanceof org.bukkit.block.Beehive beehiveState)) {
            return; // Not a beehive block
        }
        if(!(beehiveState.getBlockData() instanceof org.bukkit.block.data.type.Beehive beehiveData)) {
            return; // Not a beehive block
        }

        if (!beehiveData.getFacing().equals(event.getBlockFace())) {
            return; // Not facing the beehive
        }

        Optional<Beehive> beehiveOptional = beehiveManager.getBeehiveFromBlock(clickedBlock.getState());
        if (beehiveOptional.isEmpty()) return;
        Beehive beehive = beehiveOptional.get();

        Upgrade upgrade = beehive.getUpgrade();
        UUID currentDisplayUUID = beehive.getUpgradeId();

        System.out.println("Current upgrade: " + upgrade.id() + ", Display UUID: " + currentDisplayUUID);

        if(player.isSneaking() && !upgrade.equals(Upgrade.NONE)) {
            event.setCancelled(true);
            //only remove upgrade if player is sneaking
            ItemStack toGive = upgrade.build();
            Util.giveItem(player, toGive);
            beehiveManager.editBeehive(event.getClickedBlock(), beehiveToEdit -> {
                beehiveToEdit.setUpgrade(Upgrade.NONE);
                beehiveToEdit.setUpgradeId(null);
            });

            upgradesManager.removeUpgradeDisplay(currentDisplayUUID);

            Logger.debug("Removed upgrade {} from beehive at {}", upgrade.id(), clickedBlock.getLocation());
            return;
        }

        ItemStack upgradeItem = event.getItem();
        upgradesManager.getUpgradeFromItem(upgradeItem).ifPresent(toAdd -> {
            event.setCancelled(true);
            if(toAdd.id().equals(upgrade.id())) {
                return;
            }

            if(!upgrade.equals(Upgrade.NONE)) {
                ItemStack toGive = upgrade.build();
                Util.giveItem(player, toGive);
            }

            if(player.getGameMode() != GameMode.CREATIVE && upgradeItem != null) {
                upgradeItem.setAmount(upgradeItem.getAmount() - 1);
            }

            ItemDisplay newDisplay = upgradesManager.createUpgradeDisplay(clickedBlock, toAdd);
            UUID newDisplayUUID = newDisplay == null ? null : newDisplay.getUniqueId();

            beehiveManager.editBeehive(event.getClickedBlock(), beehiveToEdit -> {
                beehiveToEdit.setUpgrade(toAdd);
                beehiveToEdit.setUpgradeId(newDisplayUUID);
            });

            upgradesManager.removeUpgradeDisplay(currentDisplayUUID);
            Logger.debug("Added upgrade {} to beehive at {}", toAdd.id(), clickedBlock.getLocation());
        });
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        UpgradesManager upgradesManager = this.plugin.getManager(UpgradesManager.class);
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);
        Block block = event.getBlock();

        beehiveManager.getBeehiveFromBlock(block.getState()).ifPresent(beehive -> {
            UUID currentDisplayUUID = beehive.getUpgradeId();
            upgradesManager.removeUpgradeDisplay(currentDisplayUUID);
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        UpgradesManager upgradesManager = this.plugin.getManager(UpgradesManager.class);
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);
        Block block = event.getBlockPlaced();

        beehiveManager.getBeehiveFromItem(event.getItemInHand()).ifPresent(beehive -> {
            ItemDisplay display = upgradesManager.createUpgradeDisplay(block, beehive.getUpgrade());
            UUID displayUUID = display == null ? null : display.getUniqueId();
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                beehiveManager.editBeehive(block, beehiveToEdit -> {
                    beehiveToEdit.setUpgradeId(displayUUID);
                });
            }, 1L);
        });
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        UpgradesManager upgradesManager = this.plugin.getManager(UpgradesManager.class);

        Arrays.stream(event.getChunk().getTileEntities()).forEach(blockState -> {
            if (!(blockState instanceof org.bukkit.block.Beehive beehiveState)) {
                return;
            }
            upgradesManager.loadBeehive(beehiveState, Arrays.stream(event.getChunk().getEntities()));
        });
    }

}
