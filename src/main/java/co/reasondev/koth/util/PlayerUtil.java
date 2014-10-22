package co.reasondev.koth.util;

import co.reasondev.koth.hill.Hill;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static Player getClaimingPlayer(Hill hill) {
        for(Player player : Bukkit.getOnlinePlayers())
            if(isClaimingHill(player, hill)) return player;
        return null;
    }

    public static boolean isClaimingHill(Player player, Hill hill) {
        return hill.getHillRegion().contains(BukkitUtil.toVector(player.getLocation()));
    }
}
