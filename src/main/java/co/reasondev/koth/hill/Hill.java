package co.reasondev.koth.hill;/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

import co.reasondev.koth.config.Settings;
import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.StringUtil;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Hill {

    private String hillID;
    private String regionID;
    private String displayName;
    private String worldName;
    private Location chestSpawnLoc;
    private UUID hillKing;

    public Hill(String hillID, String regionID, String displayName, String worldName,  Location chestSpawnLoc) {
        this.hillID = hillID;
        this.displayName = displayName;
        this.worldName = worldName;
        this.regionID = regionID;
        this.chestSpawnLoc = chestSpawnLoc;
    }

    public String getHillID() {
        return hillID;
    }

    public String getDisplayName(boolean isColored) {
        return isColored ? StringUtil.color(displayName) : displayName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public ProtectedRegion getHillRegion() {
        return WGBukkit.getRegionManager(getWorld()).getRegion(regionID);
    }

    public Location getChestSpawnLoc() {
        return chestSpawnLoc;
    }

    public void setChestSpawnLoc(Location chestSpawnLoc) {
        this.chestSpawnLoc = chestSpawnLoc;
    }

    public boolean isHillKing(Player player) {
        return hillKing.compareTo(player.getUniqueId()) == 0;
    }

    public void setHillKing(UUID hillKing) {
        if(Bukkit.getPlayer(hillKing) == null) return;
        this.hillKing = hillKing;
        Messaging.send(Bukkit.getPlayer(hillKing), Settings.Messages.PLAYER_CLAIMED_HILL.toString().replace("%hill%", getDisplayName(true)));
        Messaging.broadcast(Settings.Broadcasts.PLAYER_CLAIMED_HILL.toString().replace("%hill%", getDisplayName(true)).replace("%player%", Bukkit.getPlayer(hillKing).getName()));
        spawnRewardChest();
    }

    private void spawnRewardChest() {
        chestSpawnLoc.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) chestSpawnLoc.getBlock().getState();
        for(ItemStack i : StringUtil.parseChestLoadout(Settings.CHEST_LOADOUT.toStringList()))
            chest.getBlockInventory().addItem(i);
    }

    public void resetHill() {
        Chest chest = chestSpawnLoc.getBlock().getType() == Material.CHEST ? (Chest) chestSpawnLoc.getBlock().getState() : null;
        if(chest != null) chest.getBlockInventory().clear();
        chestSpawnLoc.getBlock().setType(Material.AIR);
        this.hillKing = null;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> output = new HashMap<String, Object>();
        output.put("displayName", displayName);
        output.put("worldName", worldName);
        output.put("hillRegion", regionID);
        output.put("chestSpawnLocation", StringUtil.parseLocation(chestSpawnLoc));
        return output;
    }

    public static Hill deserialize(ConfigurationSection c) {
        return new Hill(c.getName(),
                c.getString("hillRegion"),
                c.getString("displayName"),
                c.getString("worldName"),
                StringUtil.parseLocationString(c.getString("chestSpawnLocation")));
    }
}
