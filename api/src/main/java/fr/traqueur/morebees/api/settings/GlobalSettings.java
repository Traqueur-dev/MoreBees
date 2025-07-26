package fr.traqueur.morebees.api.settings;

import de.exlll.configlib.Comment;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.ItemStackWrapper;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.ToolDataType;
import fr.traqueur.morebees.api.util.Formatter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record GlobalSettings(boolean debug,
                             @Comment("Define the name of the flying animation for all bee model") String flyAnimation,
                             @Comment("Field \"model\" permits to use a custom model from model engine remove it to not use a custom model") List<BeeType> bees,
                             @Comment("This key represent the beebox tool to get bees inside") ItemStackWrapper beeBox,
                             int beeBoxSize,
                             @Comment("This key represent the bee jar tool to get one bee inside") ItemStackWrapper beeJar
                             ) implements Settings {

    public static final Supplier<GlobalSettings> DEFAULT = () -> {
        List<BeeType> bees = new ArrayList<>();
        bees.add(new BeeType("redstone-bee", 1,"<red>Redstone Bee", List.of(Material.REDSTONE.name()), List.of(Material.REDSTONE_BLOCK.name()), Material.REDSTONE_ORE.name(), "redstone-bee"));
        bees.add(new BeeType("emerald-bee", 2, "<green>Emerald Bee", List.of(Material.EMERALD.name()), List.of(Material.EMERALD_BLOCK.name()), Material.EMERALD_ORE.name(), null));
        bees.add(new BeeType("diamond-bee", 3,"<aqua>Diamond Bee",List.of(Material.DIAMOND.name()), List.of(Material.DIAMOND_BLOCK.name()), Material.DIAMOND_ORE.name(),null));
        bees.add(new BeeType("gold-bee",4,"<gold>Gold Bee", List.of(Material.GOLD_INGOT.name()), List.of(Material.GOLD_BLOCK.name()), Material.GOLD_ORE.name(), null));
        bees.add(new BeeType("iron-bee", 5,"<gray>Iron Bee", List.of(Material.IRON_INGOT.name()), List.of(Material.IRON_BLOCK.name()), Material.IRON_ORE.name(), null));

        ItemStackWrapper beeBox = new ItemStackWrapper(Material.PAPER.name(), "Bee box", List.of("This is a beebox", "%bees%"));
        ItemStackWrapper beeJar = new ItemStackWrapper(Material.GLASS_BOTTLE.name(), "Bee jar", List.of("This is a bee jar", "%bee%"));

        return new GlobalSettings(true, "flying", bees, beeBox, 10, beeJar);
    };

    public Optional<BeeType> getBeeType(String type) {
        return bees.stream()
                .filter(b -> b.type().equals(type))
                .findFirst();
    }

    public boolean contains(String... type) {
        for (String t : type) {
            if (bees.stream().noneMatch(b -> b.type().equals(t))) {
                return false;
            }
        }
        return true;
    }

    public ItemStack emptyBeeBox() {
        ItemStack base = beeBox.build();

        base.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.MAX_BEES.get(container, PersistentDataType.INTEGER, beeBoxSize);
            Keys.TOOL_ID.get(container, ToolDataType.INSTANCE, ToolsManager.Tool.BEE_BOX);
            meta.lore(ToolsManager.Tool.BEE_BOX.lore(List.of()));
        });
        return base;
    }

    public ItemStack emptyBeeJar() {
        ItemStack base = beeJar.build();
        base.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.MAX_BEES.get(container, PersistentDataType.INTEGER, 1);
            Keys.TOOL_ID.get(container, ToolDataType.INSTANCE, ToolsManager.Tool.BEE_JAR);
            meta.lore(ToolsManager.Tool.BEE_JAR.lore(List.of()));
        });
        return base;
    }

}
