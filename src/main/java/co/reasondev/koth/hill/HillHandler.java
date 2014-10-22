package co.reasondev.koth.hill;

import co.reasondev.koth.KotH;
import co.reasondev.koth.Messaging;
import co.reasondev.koth.config.ConfigHandler;
import co.reasondev.koth.config.Lang;
import co.reasondev.koth.config.Settings;
import co.reasondev.koth.util.PlayerUtil;
import co.reasondev.koth.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class HillHandler {

    private KotH plugin;

    private BukkitTask hillTask;
    private String activeHill;
    private boolean isClaimable = false;
    private int timeClaimed = 0;
    private UUID claimingPlayer = null;

    private static YamlConfiguration c = ConfigHandler.getConfig("hills");
    private static Map<String, Hill> loadedHills = new TreeMap<String, Hill>(String.CASE_INSENSITIVE_ORDER);

    public HillHandler(KotH plugin) {
        this.plugin = plugin;
    }

    public void loadHills() {
        for(String hillID : c.getKeys(false))
            loadedHills.put(hillID, Hill.deserialize(c.getConfigurationSection(hillID)));
        Messaging.printInfo("Hills successfully loaded!");
        run();
    }

    public void saveHills() {
        cancel();
        for(String hillID : loadedHills.keySet())
            c.set(hillID, loadedHills.get(hillID).serialize());
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
        return loadedHills.get(hillIDs.get(new Random().nextInt(hillIDs.size())));
    }

    public static Hill getHillFromChest(Block chest) {
        for(Hill hill : loadedHills.values())
            if(hill.getChestSpawnLoc().equals(chest.getLocation())) return hill;
        return null;
    }

    public static void createHill(String hillID, String regionID, String displayName, Location chestSpawnLoc) {
        loadedHills.put(hillID, new Hill(hillID, displayName, regionID, chestSpawnLoc.getWorld().getName(), chestSpawnLoc));
    }

    public static void removeHill(Hill hill) {
        c.set(hill.getHillID(), null);
        loadedHills.remove(hill.getHillID());
    }

    //HillTask Management
    private void run() {
        if(!isClaimable) {
            hillTask = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Hill nextHill = getRandomHill();
                    if(activeHill != null && activeHill.equalsIgnoreCase(nextHill.getHillID()) && loadedHills.size() > 1)
                        while(activeHill.equalsIgnoreCase(nextHill.getHillID())) nextHill = getRandomHill();
                    setActiveHill(nextHill);
                }
            }, Settings.HILL_ROTATE_DELAY.toInt() * 20);

        } else {
            hillTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    if(canIncrementTime()) {
                        if(timeClaimed++ >= Settings.HILL_CLAIM_DELAY.toInt()) setHillKing(Bukkit.getPlayer(claimingPlayer));
                        else if(timeClaimed % Settings.HILL_CLAIM_BROADCAST.toInt() == 0) Messaging.broadcast(Lang.Broadcasts.PLAYER_CLAIMING_HILL.toString().
                                replace("%hill%", getHill(activeHill).getDisplayName(true)).
                                replace("%coords%", StringUtil.formatLocation(getHill(activeHill).getChestSpawnLoc())).
                                replace("%player%", Bukkit.getPlayer(claimingPlayer).getName()));
                    } else {
                        timeClaimed = 0;
                        claimingPlayer = null;
                        Player player = PlayerUtil.getClaimingPlayer(getHill(activeHill));
                        if(player != null) {
                            claimingPlayer = player.getUniqueId();
                            Messaging.send(player, Lang.Messages.PLAYER_CLAIMING_HILL);
                        }
                    }
                }
            }, 20, 20);
        }
    }

    private void cancel() {
        if(hillTask != null) hillTask.cancel();
        if(activeHill != null) getHill(activeHill).resetHill();
        this.hillTask = null;
        this.activeHill = null;
        this.isClaimable = false;
        this.timeClaimed = 0;
        this.claimingPlayer = null;
    }

    public void setActiveHill(Hill hill) {
        cancel();
        this.activeHill = hill.getHillID();
        this.isClaimable = true;
        Messaging.broadcast(Lang.Broadcasts.HILL_ACTIVE.toString().replace("%hill%", hill.getDisplayName(true)).replace("%coords%", StringUtil.formatLocation(hill.getChestSpawnLoc())));
        run();
    }

    private boolean canIncrementTime() {
        return claimingPlayer != null && Bukkit.getPlayer(claimingPlayer) != null && PlayerUtil.isClaimingHill(Bukkit.getPlayer(claimingPlayer), getHill(activeHill));
    }

    private void setHillKing(Player player) {
        getHill(activeHill).setHillKing(player);
        if(hillTask != null) hillTask.cancel();
        this.hillTask = null;
        this.isClaimable = false;
        this.timeClaimed = 0;
        this.claimingPlayer = null;
        run();
    }
}
