package co.reasondev.koth;

import co.reasondev.koth.cmd.CommandHandler;
import co.reasondev.koth.config.ConfigHandler;
import co.reasondev.koth.hill.HillHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class KotH extends JavaPlugin {

    private static KotH instance;

    private CommandHandler commandHandler;
    private ConfigHandler configHandler;
    private HillHandler hillHandler;

    public void onEnable() {
        instance = this;
        getConfigHandler().loadConfigs();
        getHillHandler().loadHills();
        getCommandHandler().registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("has been enabled");
    }

    public void onDisable() {
        getHillHandler().saveHills();
        getConfigHandler().saveConfigs();
        getLogger().info("has been disabled");
        instance = null;
    }

    public static KotH getInstance() {
        return instance;
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

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        return getCommandHandler().onCommand(sender, cmd, commandLabel, args);
    }
}
