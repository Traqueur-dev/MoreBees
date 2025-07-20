package fr.traqueur.morebees;

import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

public class BeeManagerImpl implements BeeManager {

    @Override
    public Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return Optional.empty();
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return  Keys.BEETYPE.get(container, BeeTypeDataType.INSTANCE);
    }

}
