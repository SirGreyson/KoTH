package co.reasondev.koth.cmd;

import co.reasondev.koth.KotH;
import co.reasondev.koth.Messaging;
import co.reasondev.koth.hill.Hill;
import co.reasondev.koth.hill.HillHandler;
import co.reasondev.koth.util.PlayerUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KotHCommand {

    private static KotH plugin = KotH.getInstance();

    @Command(aliases = {"start"}, desc = "Hill force-starting command", max = 0, help = "Starts the next Hill automatically")
    public static void startHill(CommandContext args, CommandSender sender) throws CommandException {
        Messaging.send(sender, "&aForce starting Hill selection...");
        plugin.getHillHandler().setActiveHill(HillHandler.getRandomHill());
    }

    @Command(aliases = {"create"}, desc = "Hill creation command", usage = "<ID> <RegionID> [-d <displayName>]", flags = "d:", min = 2
            , help = "Creates a new Hill with a given ID and sets its chest spawn Location at the sender's current Location")
    public static void createHill(CommandContext args, CommandSender sender) throws CommandException {
        if(!(sender instanceof Player)) Messaging.send(sender, "&cError! This command cannot be run from the Console!");
        else if(HillHandler.hillExists(args.getString(0))) Messaging.send(sender, "&cError! There is already a Hill with that ID!");
        else if(!WGBukkit.getRegionManager(((Player) sender).getWorld()).hasRegion(args.getString(1))) Messaging.send(sender, "&cError! That Region does not exist in this World!");
        else if(!WGBukkit.getRegionManager(((Player) sender).getWorld()).getRegion(args.getString(1)).contains(BukkitUtil.toVector(((Player) sender).getLocation())))
            Messaging.send(sender, "&cError! You must be in the specified Region!");
        else {
            HillHandler.createHill(args.getString(0), args.getString(1), args.hasFlag('d') ? args.getFlag('d') : args.getString(0), ((Player) sender).getLocation());
            Messaging.send(sender, "&aSuccessfully created a new Hill with ID &e" + args.getString(0) + " &aand set chest spawn to your Location!");
        }
    }

    @Command(aliases = {"remove"}, desc = "Hill removal command", usage = "<ID>", min = 1, max = 1, help = "Removes and deletes an existing Hill")
    public static void removeHill(CommandContext args, CommandSender sender) throws CommandException {
        if(!HillHandler.hillExists(args.getString(0))) Messaging.send(sender, "&cError! There is no Hill with that ID!");
        else {
            HillHandler.removeHill(HillHandler.getHill(args.getString(0)));
            Messaging.send(sender, "&aSuccessfully removed Hill with ID &e" + args.getString(0));
        }
    }

    @Command(aliases = {"list"}, desc = "Hill listing command", max = 0, help = "Lists all currently loaded Hills")
    public static void listHills(CommandContext args, CommandSender sender) throws CommandException {
        StringBuilder sb = new StringBuilder("&6&nCurrently Loaded Hills:");
        for(Hill hill : HillHandler.getLoadedHills())
            sb.append("\n&8[" + hill.getDisplayName(true) + "&8] &7" + hill.getHillID());
        Messaging.send(sender, sb.toString());
    }

    @Command(aliases = {"teleport", "tp"}, desc = "Hill teleporting command", usage = "<ID>", min = 1, max = 1, help = "Teleport to a Hill's chest spawn Location")
    public static void goToHill(CommandContext args, CommandSender sender) throws CommandException {
        if(!(sender instanceof Player)) Messaging.send(sender, "&cError! This command cannot be run from the Console!");
        else if(!HillHandler.hillExists(args.getString(0))) Messaging.send(sender, "&cError! There is no Hill with that ID!");
        else {
            ((Player) sender).teleport(HillHandler.getHill(args.getString(0)).getChestSpawnLoc());
            Messaging.send(sender, "&aTeleporting to &e" + args.getString(0));
        }
    }

    @Command(aliases = {"setspawn"}, desc = "Hill chest spawn setting command", usage = "<ID>", min = 1, max = 1, help = "Sets a Hill's chest spawn Location")
    public static void setChestSpawn(CommandContext args, CommandSender sender) throws CommandException {
        if(!(sender instanceof Player)) Messaging.send(sender, "&cError! This command cannot be run from the Console!");
        else if(!HillHandler.hillExists(args.getString(0))) Messaging.send(sender, "&cError! There is no Hill with that ID!");
        else if(!PlayerUtil.isClaimingHill((Player) sender, HillHandler.getHill(args.getString(0)))) Messaging.send(sender, "&cError! The chest spawn must be inside of the Hill Region!");
        else {
            HillHandler.getHill(args.getString(0)).setChestSpawnLoc(((Player) sender).getLocation());
            Messaging.send(sender, "&aChest spawn for Hill &e" + args.getString(0) + " &aset to your Location!");
        }
    }
}
