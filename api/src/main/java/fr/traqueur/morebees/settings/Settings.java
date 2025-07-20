package fr.traqueur.morebees.settings;

public record Settings(boolean debug) {

    public static final Settings DEFAULT = new Settings(true);

}
