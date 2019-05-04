package com.nation.lobby.eventmodule;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListeners implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {


        if (!EventCommands.flags.get(e.getPlayer().getWorld()).get(2))
        {
            if (!(e.getPlayer().hasPermission("group.admin")))
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (!EventCommands.flags.get(e.getPlayer().getWorld()).get(1))
        {
            if (!(e.getPlayer().hasPermission("group.admin") || e.getPlayer().hasPermission("group.builder")))
            {
                if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                {
                    e.setCancelled(true);
                }
            } else
            {
                if (!(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)))
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if (!EventCommands.flags.get(e.getPlayer().getWorld()).get(0))
        {
            if (!(e.getPlayer().hasPermission("group.admin") || e.getPlayer().hasPermission("group.builder")))
            {
                if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                {
                    e.setCancelled(true);
                }
            } else
            {
                if (!(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)))
                {
                    e.setCancelled(true);
                }
            }
        }

    }
}
