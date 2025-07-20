package fr.traqueur.morebees;

import fr.traqueur.morebees.settings.Settings;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BeePlugin extends JavaPlugin {

    public abstract Settings getSettings();

}
