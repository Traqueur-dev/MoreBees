package fr.traqueur.morebees.nms;

import org.bukkit.Bukkit;

import java.util.List;

public enum NMSVersion {

    V1_21_R3("1.21.4"),
    V1_21_R4("1.21.5"),
    V1_21_R5("1.21.6", "1.21.7", "1.21.8"),
    ;

    private final List<String> versions;

    public static NMSVersion CURRENT;

    public static void initialize() {
        String version = Bukkit.getMinecraftVersion();
        for (NMSVersion nmsVersion : values()) {
            if (nmsVersion.isCompatible(version)) {
                CURRENT = nmsVersion;
                return;
            }
        }
    }

    NMSVersion(String... versions) {
        this.versions = List.of(versions);
    }

    boolean isCompatible(String version) {
        return versions.contains(version);
    }

}
