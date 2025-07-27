package fr.traqueur.morebees.listeners;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.managers.UpgradesManager;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

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
        if(player.isSneaking() && !upgrade.equals(Upgrade.NONE)) {
            event.setCancelled(true);
            //only remove upgrade if player is sneaking
            ItemStack toGive = upgrade.build();
            Util.giveItem(player, toGive);
            beehiveManager.editBeehive(event.getClickedBlock(), beehiveToEdit -> {
                beehiveToEdit.setUpgrade(Upgrade.NONE);
            });
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

            beehiveManager.editBeehive(event.getClickedBlock(), beehiveToEdit -> {
                beehiveToEdit.setUpgrade(toAdd);
            });
            Logger.debug("Added upgrade {} to beehive at {}", toAdd.id(), clickedBlock.getLocation());
        });

    }

}
