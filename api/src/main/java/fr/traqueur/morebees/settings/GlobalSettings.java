package fr.traqueur.morebees.settings;

import de.exlll.configlib.Comment;
import fr.traqueur.morebees.models.BeeType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record GlobalSettings(boolean debug, @Comment("Field \"model\" permits to use a custom model from model engine use \"default\" if you don't want") List<BeeType> bees) implements Settings {

    public static final Supplier<GlobalSettings> DEFAULT = () -> {
        List<BeeType> bees = new ArrayList<>();
        bees.add(new BeeType("redstone-bee", 1,"<red>Redstone Bee", Material.REDSTONE, Material.REDSTONE_BLOCK, "redstone-bee"));
        bees.add(new BeeType("emerald-bee", -1, "<green>Emerald Bee", Material.EMERALD, Material.EMERALD_BLOCK, "default"));
        bees.add(new BeeType("diamond-bee", -1,"<aqua>Diamond Bee",Material.DIAMOND, Material.DIAMOND_BLOCK, "default"));
        bees.add(new BeeType("gold-bee",-1,"<gold>Gold Bee", Material.GOLD_INGOT, Material.GOLD_BLOCK, "default"));
        bees.add(new BeeType("iron-bee", -1,"<gray>Iron Bee", Material.IRON_INGOT, Material.IRON_BLOCK, "default"));
        return new GlobalSettings(true, bees);
    };

}
