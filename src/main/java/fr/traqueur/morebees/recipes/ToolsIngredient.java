package fr.traqueur.morebees.recipes;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.ToolDataType;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.recipes.api.domains.Ingredient;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class ToolsIngredient extends Ingredient {

    private final BeePlugin plugin;
    private final ToolsManager.Tool tool;

    public ToolsIngredient(BeePlugin plugin, String data, Character sign) {
        super(sign);
        this.plugin = plugin;
        this.tool = ToolsManager.Tool.valueOf(data);
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return Keys.TOOL_ID.get(container, ToolDataType.INSTANCE).map(toolFound -> toolFound == tool)
                .orElse(false);
    }

    @Override
    public RecipeChoice choice() {
        return switch (this.tool) {
            case BEE_BOX -> new RecipeChoice.MaterialChoice(this.plugin.getSettings(GlobalSettings.class).emptyBeeBox().getType());
            case BEE_JAR -> new RecipeChoice.MaterialChoice(this.plugin.getSettings(GlobalSettings.class).emptyBeeJar().getType());
        };
    }

}