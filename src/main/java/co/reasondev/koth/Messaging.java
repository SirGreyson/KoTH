package co.reasondev.koth;

import co.reasondev.koth.config.Lang;
import co.reasondev.koth.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class Messaging {

    private static Logger log = KotH.getInstance().getLogger();
    private static String PREFIX = Lang.Messages.PREFIX.toString();
    private static String BROADCAST_PREFIX = Lang.Broadcasts.PREFIX.toString();

    public static void printInfo(String message) {
        log.info(ChatColor.YELLOW + message);
    }

    public static void printErr(String message) {
        log.severe(ChatColor.RED + message);
    }

    public static void printErr(String message, String source) {
        printErr(message + " [" + source + "]");
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(StringUtil.color(PREFIX + " " + message));
    }

    public static void send(CommandSender sender, Lang.Messages message) {
        send(sender, message.toString());
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(StringUtil.color(BROADCAST_PREFIX + " " + message));
    }

    public static void broadcast(Lang.Broadcasts message) {
        broadcast(message.toString());
    }
}
