/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.cmd;

import co.reasondev.koth.KoTH;
import co.reasondev.koth.util.Messaging;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHandler {

    private KoTH plugin;
    private CommandsManager<CommandSender> commands;

    public CommandHandler(KoTH plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String s) {
                return sender.isOp() || sender.hasPermission(s);
            }
        };
        CommandsManagerRegistration reg = new CommandsManagerRegistration(plugin, commands);
        reg.register(CommandHandler.class);
        Messaging.printInfo("Commands successfully registered!");
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            Messaging.send(sender, "&cYou do not have permission to use this command!");
        } catch (MissingNestedCommandException e) {
            Messaging.send(sender, ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            Messaging.send(sender, ChatColor.RED + e.getMessage());
            Messaging.send(sender, ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                Messaging.send(sender, "&cNumber expected, string received instead.");
            } else {
                Messaging.send(sender, "&cAn error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            Messaging.send(sender, ChatColor.RED + e.getMessage());
        }
        return true;
    }

    @Command(aliases = {"koth"}, desc = "KoTH Plugin command")
    @NestedCommand(KoTHCommand.class)
    @CommandPermissions("koth.admin")
    public static void kothCommand(CommandContext args, CommandSender sender) {
    }
}
