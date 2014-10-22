package co.reasondev.koth;

import co.reasondev.koth.config.Lang;
import co.reasondev.koth.hill.Hill;
import co.reasondev.koth.hill.HillHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.CHEST) return;
        Hill hill = HillHandler.getHillFromChest(e.getClickedBlock());
        if(hill != null) {
            e.setCancelled(!hill.isHillKing(e.getPlayer()));
            if(e.isCancelled()) Messaging.send(e.getPlayer(), Lang.Messages.PLAYER_NOT_KING);
        }
    }
}
