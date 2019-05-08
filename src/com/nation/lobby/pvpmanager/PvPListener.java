package com.nation.lobby.pvpmanager;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PvPListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if (QueuePvPCommand.PvPState == ArenaState.ACTIVE)
        {
            try
            {
                if (p == QueuePvPCommand.QueueOne)
                {
                    if (!(WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation()).getRegions().contains(WGBukkit.getRegionManager(p.getWorld()).getRegion("pvp"))))
                    {
                        QueuePvPCommand.endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, QueuePvPCommand.QueueTwo);
                    }
                } else if (p == QueuePvPCommand.QueueTwo)
                {
                    if (!(WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation()).getRegions().contains(WGBukkit.getRegionManager(p.getWorld()).getRegion("pvp"))))
                    {
                        QueuePvPCommand.endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, QueuePvPCommand.QueueOne);
                    }
                }
            } catch (NullPointerException n)
            {
            }
            if (QueuePvPCommand.prepareMode)
            {
                if (p == QueuePvPCommand.QueueOne || p == QueuePvPCommand.QueueTwo)
                {
                    if (new Random().nextInt(2) == 1)
                    {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        if (e.getPlayer() == QueuePvPCommand.QueueOne)
        {

            if (QueuePvPCommand.PvPState == ArenaState.ACTIVE)
            {
                QueuePvPCommand.endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, QueuePvPCommand.QueueTwo);
            } else
            {
                QueuePvPCommand.QueueOne = null;
            }

        } else if (e.getPlayer() == QueuePvPCommand.QueueTwo)
        {
            QueuePvPCommand.endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, QueuePvPCommand.QueueOne);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            Player p = e.getEntity();
            if (QueuePvPCommand.QueueOne == p || QueuePvPCommand.QueueTwo == p)
            {
                QueuePvPCommand.endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, p.getKiller());
            }
        }
    }
}

