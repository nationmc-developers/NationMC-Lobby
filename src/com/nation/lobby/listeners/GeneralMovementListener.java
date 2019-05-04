package com.nation.lobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GeneralMovementListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        final int TP_Y = -38;
        Player p = e.getPlayer();

        if (p.getLocation().getY() < TP_Y)
        {
            if (!(p.getGameMode().equals(GameMode.CREATIVE)))
            {
                Location sp = p.getWorld().getSpawnLocation();
                p.teleport(new Location(p.getWorld(), sp.getX(), sp.getY(), sp.getZ()));
            }
        }
    }
}
