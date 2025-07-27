package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.Upgrade;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface UpgradesManager extends Manager {
    Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack);
}
