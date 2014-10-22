package co.reasondev.koth.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {

    private static YamlConfiguration c = ConfigHandler.getConfig("lang");

    public enum Messages {

        PREFIX,
        PLAYER_CLAIMING_HILL,
        PLAYER_CLAIMED_HILL,
        PLAYER_NOT_KING;

        public String toString() {
            return c.getString("messages." + name());
        }
    }

    public  enum Broadcasts {

        PREFIX,
        HILL_ACTIVE,
        PLAYER_CLAIMING_HILL,
        PLAYER_CLAIMED_HILL;

        public String toString() {
            return c.getString("broadcasts." + name());
        }
    }
}
