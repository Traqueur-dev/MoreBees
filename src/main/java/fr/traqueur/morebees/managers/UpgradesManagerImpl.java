package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.api.managers.UpgradesManager;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.listeners.UpgradesListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

public class UpgradesManagerImpl implements UpgradesManager {

    public UpgradesManagerImpl() {
        this.getPlugin().registerListener(new UpgradesListener(this.getPlugin()));
    }

    @Override
    public Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return Optional.empty();
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return Keys.UPGRADE_ID.get(persistentDataContainer, UpgradeDataType.INSTANCE);
    }



}
