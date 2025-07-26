package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.serialization.BeeDataDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.ToolDataType;
import fr.traqueur.morebees.listeners.ToolsListener;
import fr.traqueur.morebees.models.BeeDataImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Bee;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolsManagerImpl implements ToolsManager {

    public ToolsManagerImpl() {
        this.getPlugin().registerListener(new ToolsListener(this.getPlugin()));
    }

    @Override
    public Optional<Tool> getTool(ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        return Keys.TOOL_ID.get(dataContainer, ToolDataType.INSTANCE);
    }

    @Override
    public boolean isFull(ItemStack itemStack) {
        return this.getTool(itemStack).map(tool -> {
            PersistentDataContainer dataContainer = itemStack.getItemMeta().getPersistentDataContainer();
            int maxBees = tool.maxBees();
            List<BeeData> bees = Keys.BEES.get(dataContainer, PersistentDataType.LIST.listTypeFrom(BeeDataDataType.INSTANCE), new ArrayList<>());
            return bees.size() >= maxBees;
        }).orElse(true); // Default to true if no tool is found
    }

    @Override
    public BeeData toData(Bee bee, BeeType beeType) {
        return new BeeDataImpl(beeType, bee.hasNectar(), bee.isAdult());
    }

    @Override
    public void catchBee(ItemStack tool, Bee bee, BeeType beeType) {
        if (tool == null || this.getTool(tool).isEmpty()) {
            return;
        }
        Tool toolType = this.getTool(tool).orElseThrow(() -> new IllegalArgumentException("ItemStack is not a valid tool"));
        List<BeeData> bees = this.getBeesInsideTool(tool);

        if (bees.size() >= toolType.maxBees()) {
            return; // Tool is full
        }

        bee.remove();
        bees.add(this.toData(bee, beeType));

        List<Component> newLore = toolType.lore(bees);
        tool.editMeta(meta -> {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            Keys.BEES.set(dataContainer, PersistentDataType.LIST.listTypeFrom(BeeDataDataType.INSTANCE), bees);
            meta.lore(newLore);
        });
    }

    @Override
    public List<BeeData> releaseBee(ItemStack tool, boolean all) {
        if (tool == null || this.getTool(tool).isEmpty()) {
            return List.of();
        }
        Tool toolType = this.getTool(tool).orElseThrow(() -> new IllegalArgumentException("ItemStack is not a valid tool"));
        List<BeeData> bees = this.getBeesInsideTool(tool);

        if (bees.isEmpty()) {
            return List.of();
        }

        int nbBees = all ? bees.size() : 1;

        List<BeeData> releasedBees = new ArrayList<>();
        for (int i = 0; i < nbBees && !bees.isEmpty(); i++) {
            releasedBees.add(bees.removeLast());
        }

        List<Component> newLore = toolType.lore(bees);
        tool.editMeta(meta -> {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            Keys.BEES.set(dataContainer, PersistentDataType.LIST.listTypeFrom(BeeDataDataType.INSTANCE), bees);
            meta.lore(newLore);
        });
        return releasedBees;
    }

    private List<BeeData> getBeesInsideTool(ItemStack tool) {
        if (tool == null || this.getTool(tool).isEmpty()) {
            return List.of();
        }
        PersistentDataContainer dataContainer = tool.getItemMeta().getPersistentDataContainer();
        List<BeeData> bees = Keys.BEES.get(dataContainer, PersistentDataType.LIST.listTypeFrom(BeeDataDataType.INSTANCE), new ArrayList<>());
        return new ArrayList<>(bees);
    }


}
