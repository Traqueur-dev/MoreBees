package fr.traqueur.morebees.recipes;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.settings.UpgradeSettings;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.recipes.api.hook.Hook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record MoreBeesHook(BeePlugin plugin) implements Hook {

    @Override
    public String getPluginName() {
        return this.plugin.getName();
    }

    @Override
    public Ingredient getIngredient(String data, Character sign) {
        return new ToolsIngredient(this.plugin, data, sign);
    }

    @Override
    public ItemStack getItemStack(Player player, String resultPart) {
        try {
            Tool tool = Tool.valueOf(resultPart.toUpperCase());
            return tool.itemStack(List.of());
        } catch (Exception e) {
            return this.plugin.getSettings(UpgradeSettings.class).getUpgrade(resultPart)
                    .map(Upgrade::build)
                    .orElse(null);
        }
    }
}
