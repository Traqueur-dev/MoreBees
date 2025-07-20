package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.Manager;
import fr.traqueur.morebees.models.BeeType;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface BeeManager extends Manager {
    Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack);
}
