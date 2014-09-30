/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.util;

import co.reasondev.koth.hill.Hill;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static Player getClaimingPlayer(Hill hill) {
        for(Player player : Bukkit.getOnlinePlayers())
            if(isClaimingHill(player, hill)) return player;
        return null;
    }

    public static boolean isInRegion(Player player, ProtectedRegion region) {
        return region.contains(BukkitUtil.toVector(player.getLocation()));
    }

    public static boolean isClaimingHill(Player player, Hill hill) {
        return isInRegion(player, hill.getHillRegion());
    }

    /**
     * @deprecated
     * @param player
     * @return whether or not the given player is above Red Wool
     */
    private static boolean isAboveRedWool(Player player) {
        for(int i = 0; i < 3; i++) {
            Block block = player.getLocation().subtract(0, i, 0).getBlock();
            if(block.getType() == Material.WOOL && block.getData() == DyeColor.RED.getWoolData()) return true;
        }
        return false;
    }
}
