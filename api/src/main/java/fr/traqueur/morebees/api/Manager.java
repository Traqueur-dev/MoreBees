package fr.traqueur.morebees.api;

public interface Manager {

    default BeePlugin getPlugin() {
        return BeePlugin.getPlugin(BeePlugin.class);
    }

}
