package fr.traqueur.morebees.recipes;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.recipes.api.hook.Hook;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MoreBeesHook implements Hook {

    private final BeePlugin plugin;

    public MoreBeesHook(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginName() {
        return this.plugin.getName();
    }

    @Override
    public Ingredient getIngredient(String data, Character sign) {
        return new ToolsIngredient(data, sign);
    }

    @Override
    public ItemStack getItemStack(String resultPart) {
        ToolsManager.Tool tool = ToolsManager.Tool.valueOf(resultPart);
        return tool.itemStack(List.of());
    }
}
