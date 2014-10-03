/*
 * Copyright © ReasonDev 2014
 * All Rights Reserved
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.hill;

import co.reasondev.koth.KoTH;
import co.reasondev.koth.config.Settings;
import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.PlayerUtil;
import co.reasondev.koth.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HillTask {

    public enum Stage { WAITING, RUNNING }

    private KoTH plugin;
    private Stage currentStage;
    private BukkitTask hillTask;
    private String activeHill;
    private int timeClaimed = 0;
    private UUID claimingPlayer = null;

    public HillTask(KoTH plugin) {
        this.plugin = plugin;
        setStage(Stage.WAITING);
    }

    public Stage getStage() { return currentStage; }

    public void setStage(Stage currentStage) {
        this.currentStage = currentStage;
        if(hillTask != null) hillTask.cancel();
        run();
    }

    public void run() {
        if(currentStage == Stage.WAITING) {
            hillTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Hill nextHill = HillHandler.getRandomHill();
                    if(HillHandler.getLoadedHills().size() > 1 && nextHill.getHillID().equalsIgnoreCase(activeHill))
                        while(nextHill.getHillID().equalsIgnoreCase(activeHill)) nextHill = HillHandler.getRandomHill();
                    setActiveHill(nextHill);
                }
            }.runTaskLater(plugin, Settings.HILL_ROTATE_DELAY.toInt() * 20);

        } else if(currentStage == Stage.RUNNING) {
            hillTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if(canIncrementTime()) {
                        if (timeClaimed++ >= Settings.HILL_CLAIM_DELAY.toInt()) setKing(claimingPlayer);
                        else if(timeClaimed % Settings.HILL_CLAIM_BROADCAST.toInt() == 0) Messaging.broadcast(Settings.Broadcasts.PLAYER_CLAIMING_HILL.toString()
                                .replace("%hill%", getActiveHill().getDisplayName(true))
                                .replace("%coords%", StringUtil.formatLocation(getActiveHill().getChestSpawnLoc()))
                                .replace("%player%", Bukkit.getPlayer(claimingPlayer).getName()));
                    } else {
                        if(timeClaimed != 0) timeClaimed = 0;
                        if(claimingPlayer != null) claimingPlayer = null;
                        Player player = PlayerUtil.getClaimingPlayer(getActiveHill());
                        if(player != null) setClaimingPlayer(player);
                    }
                }
            }.runTaskTimer(plugin, 20, 20);
        }
    }

    public void cancel() {
        if(hillTask != null) hillTask.cancel();
        if(activeHill != null) getActiveHill().resetHill();
        this.hillTask = null;
    }

    public Hill getActiveHill() {
        return plugin.getHillHandler().getHill(activeHill);
    }

    public void setActiveHill(Hill hill) {
        if(activeHill != null) getActiveHill().resetHill();
        this.activeHill = hill.getHillID();
        Messaging.broadcast(Settings.Broadcasts.HILL_ACTIVE.toString().replace("%hill%", hill.getDisplayName(true)).replace("%coords%", StringUtil.formatLocation(hill.getChestSpawnLoc())));
        setStage(HillTask.Stage.RUNNING);
    }

    private void setKing(UUID hillKing) {
        getActiveHill().setHillKing(hillKing);
        this.timeClaimed = 0;
        this.claimingPlayer = null;
        setStage(Stage.WAITING);
    }

    private boolean canIncrementTime() {
        return claimingPlayer != null && Bukkit.getPlayer(claimingPlayer) != null && PlayerUtil.isClaimingHill(Bukkit.getPlayer(claimingPlayer), getActiveHill());
    }

    private void setClaimingPlayer(Player player) {
        this.claimingPlayer = player.getUniqueId();
        Messaging.send(player, Settings.Messages.PLAYER_CLAIMING_HILL);
    }
}
