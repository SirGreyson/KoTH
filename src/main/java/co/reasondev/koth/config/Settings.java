package co.reasondev.koth.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public enum Settings {

    HILL_ROTATE_DELAY,
    HILL_CLAIM_DELAY,
    HILL_CLAIM_BROADCAST,
    CHEST_LOADOUT;

    private static YamlConfiguration c = ConfigHandler.getConfig("config");

    public String toString() {
        return c.getString(name());
    }

    public int toInt() {
        return c.getInt(name());
    }

    public List<String> toStringList() {
        return c.getStringList(name());
    }
}
