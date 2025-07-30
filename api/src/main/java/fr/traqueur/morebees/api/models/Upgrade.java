package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.api.settings.ItemStackWrapper;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public record Upgrade(String id, ItemStackWrapper item, int maxBees, double productionMultiplier, boolean produceBlocks) implements Loadable {

    public static Upgrade NONE = new Upgrade("none", ItemStackWrapper.EMPTY, 3, 1.0, false);

    public Formatter[] formatters() {
        return Formatter.all(
                "max-bees", maxBees,
                "production-multiplier", productionMultiplier,
                "produce-blocks", produceBlocks ? Messages.PRODUCE_BLOCKS_YES.raw() : Messages.PRODUCE_BLOCKS_NO.raw()
        );
    }

    public @NotNull ItemStack build() {
        ItemStack itemStack = item.build(this.formatters());
        itemStack.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.UPGRADE_ID.set(container, UpgradeDataType.INSTANCE, this);
        });
        return itemStack;
    }

}
