package fr.traqueur.morebees.recipes;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.ToolDataType;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.api.settings.UpgradeSettings;
import fr.traqueur.recipes.api.domains.Ingredient;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class ToolsIngredient extends Ingredient {

    private final BeePlugin plugin;
    private final String data;

    public ToolsIngredient(BeePlugin plugin, String data, Character sign) {
        super(sign);
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        try {
            Tool tool = Tool.valueOf(this.data.toUpperCase());
            return Keys.TOOL_ID.get(container, ToolDataType.INSTANCE).map(toolFound -> toolFound == tool).orElse(false);
        } catch (IllegalArgumentException e) {
            Upgrade toCheck = this.plugin.getSettings(UpgradeSettings.class).getUpgrade(this.data).orElse(null);
            if (toCheck == null) return false;
            return Keys.UPGRADE_ID.get(container, UpgradeDataType.INSTANCE).map(upgradeFound -> upgradeFound.id().equalsIgnoreCase(toCheck.id())).orElse(false);
        }
    }

    @Override
    public RecipeChoice choice() {
        Material material;
        try {
            Tool tool = Tool.valueOf(this.data.toUpperCase());
            material = tool.itemStack(List.of()).getType();
        } catch (IllegalArgumentException e) {
            Upgrade upgrade = this.plugin.getSettings(UpgradeSettings.class).getUpgrade(this.data).orElse(null);
            if (upgrade == null) {
                throw new IllegalArgumentException("Invalid tool or upgrade: " + this.data, e);
            }
            material = upgrade.build().getType();
        }
        return new RecipeChoice.MaterialChoice(material);
    }

}