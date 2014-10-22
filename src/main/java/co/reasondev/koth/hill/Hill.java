package co.reasondev.koth.hill;

import co.reasondev.koth.Messaging;
import co.reasondev.koth.config.Lang;
import co.reasondev.koth.config.Settings;
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
    private String displayName;
    private String regionID;
    private String worldName;
    private Location chestSpawnLoc;

    private UUID hillKing = null;

    public Hill(String hillID, String displayName, String regionID, String worldName, Location chestSpawnLoc) {
        this.hillID = hillID;
        this.displayName = displayName;
        this.regionID = regionID;
        this.worldName = worldName;
        this.chestSpawnLoc = chestSpawnLoc;
    }

    public String getHillID() {
        return hillID;
    }

    public String getDisplayName(boolean colored) {
        return colored ? StringUtil.color(displayName) : displayName;
    }

    public ProtectedRegion getHillRegion() {
        return WGBukkit.getRegionManager(getWorld()).getRegion(regionID);
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Location getChestSpawnLoc() {
        return chestSpawnLoc;
    }

    public void setChestSpawnLoc(Location chestSpawnLoc) {
        this.chestSpawnLoc = chestSpawnLoc;
    }

    public boolean isHillKing(Player player) {
        return hillKing != null && hillKing.equals(player.getUniqueId());
    }

    public void setHillKing(Player player) {
        this.hillKing = player.getUniqueId();
        Messaging.send(player, Lang.Messages.PLAYER_CLAIMED_HILL.toString().replace("%hill%", getDisplayName(true)));
        Messaging.broadcast(Lang.Broadcasts.PLAYER_CLAIMED_HILL.toString().replace("%hill%", getDisplayName(true)).replace("%player%", player.getName()));
        spawnChest();
    }

    private void spawnChest() {
        chestSpawnLoc.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) chestSpawnLoc.getBlock().getState();
        for(ItemStack i : StringUtil.parseChestLoadout(Settings.CHEST_LOADOUT.toStringList()))
            chest.getBlockInventory().addItem(i);
    }

    public void resetHill() {
        this.hillKing = null;
        if(chestSpawnLoc.getBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) chestSpawnLoc.getBlock().getState();
            chest.getBlockInventory().clear();
        }
        chestSpawnLoc.getBlock().setType(Material.AIR);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> output = new HashMap<String, Object>();
        output.put("displayName", displayName);
        output.put("regionID", regionID);
        output.put("worldName", worldName);
        output.put("chestSpawnLoc", StringUtil.parseLocation(chestSpawnLoc));
        return output;
    }

    public static Hill deserialize(ConfigurationSection c) {
        return new Hill(c.getName(),
                c.getString("displayName"),
                c.getString("regionID"),
                c.getString("worldName"),
                StringUtil.parseLocationString(c.getString("chestSpawnLoc")));
    }
}
