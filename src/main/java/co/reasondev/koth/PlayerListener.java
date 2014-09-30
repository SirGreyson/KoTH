/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth;

import co.reasondev.koth.config.Settings;
import co.reasondev.koth.hill.Hill;
import co.reasondev.koth.util.Messaging;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    private KoTH plugin;

    public PlayerListener(KoTH plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.CHEST) return;
        Hill hill = plugin.getHillHandler().getHillFromChest(e.getClickedBlock());
        if(hill != null) {
            e.setCancelled(!hill.isHillKing(e.getPlayer()));
            if(e.isCancelled()) Messaging.send(e.getPlayer(), Settings.Messages.PLAYER_NOT_KING);
        }
    }
}
