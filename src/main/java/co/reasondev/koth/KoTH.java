/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth;

import co.reasondev.koth.cmd.CommandHandler;
import co.reasondev.koth.config.ConfigHandler;
import co.reasondev.koth.hill.HillHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class KoTH extends JavaPlugin {

    private CommandHandler commandHandler;
    private ConfigHandler configHandler;
    private HillHandler hillHandler;
    private WorldGuardPlugin worldGuardPlugin;

    public void onEnable() {
        getConfigHandler().loadConfigs();
        getHillHandler().loadHills();
        getCommandHandler().registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("has been enabled");
    }

    public void onDisable() {
        getHillHandler().saveHills();
        getConfigHandler().saveConfigs();
        getLogger().info("has been disabled");
    }

    public CommandHandler getCommandHandler() {
        if(commandHandler == null) commandHandler = new CommandHandler(this);
        return commandHandler;
    }

    public ConfigHandler getConfigHandler() {
        if(configHandler == null) configHandler = new ConfigHandler(this);
        return configHandler;
    }

    public HillHandler getHillHandler() {
        if(hillHandler == null) hillHandler = new HillHandler(this);
        return hillHandler;
    }

    public WorldGuardPlugin getWorldGuard() {
        if(worldGuardPlugin == null)
            worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        return worldGuardPlugin;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        return getCommandHandler().onCommand(sender, cmd, commandLabel, args);
    }
}
