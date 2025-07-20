package fr.traqueur.morebees;

public interface Manager {

    default BeePlugin getPlugin() {
        return BeePlugin.getPlugin(BeePlugin.class);
    }

}
