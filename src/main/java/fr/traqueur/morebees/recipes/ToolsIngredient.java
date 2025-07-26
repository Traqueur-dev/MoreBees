package fr.traqueur.morebees.recipes;

import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.ToolDataType;
import fr.traqueur.recipes.api.domains.Ingredient;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class ToolsIngredient extends Ingredient {

    private final Tool tool;

    public ToolsIngredient(String data, Character sign) {
        super(sign);
        this.tool = Tool.valueOf(data);
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
        Material material = this.tool.itemStack(List.of()).getType();
        return new RecipeChoice.MaterialChoice(material);
    }

}