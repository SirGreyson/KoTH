/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Random;

public enum Settings {

    /**
     * Main ConfigurationSection Enum
     */
    HILL_ROTATE_DELAY,
    HILL_CLAIM_DELAY,
    HILL_CLAIM_BROADCAST,
    CHEST_LOADOUT;

    private static YamlConfiguration c = ConfigHandler.getConfig("config");
    private static Random random = new Random();

    public String toString() {
        return c.getString(this.name());
    }

    public int toInt() {
        return c.getInt(this.name());
    }

    public List<String> toStringList() { return c.getStringList(this.name()); }

    /**
     * Messages ConfigurationSection Enum
     */
    public enum Messages {

        PREFIX,
        PLAYER_CLAIMING_HILL,
        PLAYER_CLAIMED_HILL,
        PLAYER_NOT_KING;

        public String toString() {
            return c.getString("messages." + this.name());
        }
    }

    /**
     * Broadcasts ConfigurationSection Enum
     */
    public enum Broadcasts {

        PREFIX,
        HILL_ACTIVE,
        PLAYER_CLAIMING_HILL,
        PLAYER_CLAIMED_HILL;

        public String toString() {
            return c.getString("broadcasts." + this.name());
        }
    }
}
