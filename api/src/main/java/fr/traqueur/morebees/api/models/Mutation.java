package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.util.Util;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Mutation(String parent, String child, List<String> blocks) implements Loadable {

    public Mutation {
        if (blocks.isEmpty()) {
            Logger.warning("Mutation with empty blocks: {} -> {}", parent, child);
        }
        Set<String> types = new HashSet<>();
        types.add(child);
        types.add(parent);

        if (!BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).contains(types.toArray(String[]::new))) {
            Logger.warning("Some bee types in mutation {} -> {} are not defined in settings: {}", parent, child);
        }
    }

    public boolean canMutate(Block block) {
        return Util.isValidBlock(block, blocks);
    }

}
