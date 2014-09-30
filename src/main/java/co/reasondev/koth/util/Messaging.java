/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.util;

import co.reasondev.koth.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class Messaging {

    private static Logger log = Bukkit.getPluginManager().getPlugin("KoTH").getLogger();
    private static String MESSAGE_PREFIX = Settings.Messages.PREFIX.toString();
    private static String BROADCAST_PREFIX = Settings.Broadcasts.PREFIX.toString();

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(StringUtil.color(MESSAGE_PREFIX + " " + message));
    }

    public static void send(CommandSender sender, Settings.Messages message) {
        send(sender, message.toString());
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(StringUtil.color(BROADCAST_PREFIX + " " + message));
    }

    public static void broadcast(Settings.Broadcasts message) {
        broadcast(message.toString());
    }

    public static void printInfo(String message) {
        log.info(message);
    }

    public static void printErr(String message) {
        log.severe(message);
    }
}
