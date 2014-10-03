/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.hill;

import co.reasondev.koth.KoTH;
import co.reasondev.koth.config.ConfigHandler;
import co.reasondev.koth.util.Messaging;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class HillHandler {

    private KoTH plugin;
    private static HillTask hillTask;
    private static YamlConfiguration hillC = ConfigHandler.getConfig("hills");
    private static Map<String, Hill> loadedHills = new TreeMap<String, Hill>(String.CASE_INSENSITIVE_ORDER);
    private static Random random = new Random();

    public HillHandler(KoTH plugin) {
        this.plugin = plugin;
        hillTask = new HillTask(plugin);
    }

    public static HillTask getHillTask() {
        return hillTask;
    }

    public void loadHills() {
        for(String hillID : hillC.getKeys(false))
            loadedHills.put(hillID, Hill.deserialize(hillC.getConfigurationSection(hillID)));
        Messaging.printInfo("Hills successfully loaded!");
    }

    public void saveHills() {
        hillTask.cancel();
        for(String hillID : loadedHills.keySet())
            hillC.set(hillID, loadedHills.get(hillID).serialize());
        Messaging.printInfo("Hills successfully saved!");
    }

    public static Collection<Hill> getLoadedHills() {
        return loadedHills.values();
    }

    public static boolean hillExists(String hillID) {
        return loadedHills.containsKey(hillID);
    }

    public static Hill getHill(String hillID) {
        return loadedHills.get(hillID);
    }

    public static Hill getRandomHill() {
        List<String> hillIDs = new ArrayList<String>(loadedHills.keySet());
        return loadedHills.get(hillIDs.get(random.nextInt(hillIDs.size())));
    }

    public Hill getHillFromChest(Block chest) {
        for(String hillID : loadedHills.keySet())
            if(isInHill(hillID, chest.getLocation())) return loadedHills.get(hillID);
        return null;
    }

    public static void createHill(String hillID, String regionID, String displayName, Location chestSpawnLoc) {
        loadedHills.put(hillID, new Hill(hillID, regionID, displayName, chestSpawnLoc.getWorld().getName(), chestSpawnLoc));
    }

    public static void removeHill(String hillID) {
        hillC.set(hillID, null);
        loadedHills.remove(hillID);
    }

    public static boolean isInHill(String hillID, Location location) {
        return loadedHills.get(hillID).getHillRegion().contains(BukkitUtil.toVector(location));
    }
}
