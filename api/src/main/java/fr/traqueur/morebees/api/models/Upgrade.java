package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.api.settings.ItemStackWrapper;
import fr.traqueur.morebees.api.util.Formatter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public record Upgrade(String id, ItemStackWrapper item, int maxBees, double productionMultiplier, boolean produceBlocks) {

    public static Upgrade NONE = new Upgrade("none", ItemStackWrapper.EMPTY, 3, 1.0, false);

    public ItemStack build() {
        Formatter[] formatters = Formatter.all(
                "max-bees", maxBees,
                "production-multiplier", productionMultiplier,
                "produce-blocks", produceBlocks ? Messages.PRODUCE_BLOCKS_YES.raw() : Messages.PRODUCE_BLOCKS_NO.raw()
                );
        ItemStack itemStack = item.build(formatters);
        itemStack.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.UPGRADE_ID.set(container, UpgradeDataType.INSTANCE, this);
        });
        return itemStack;
    }

}
