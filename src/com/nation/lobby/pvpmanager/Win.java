package com.nation.lobby.pvpmanager;

import com.nation.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class Win implements CommandExecutor {
    private Main main;

    public Win(Main main)
    {
        this.main = main;
    }

    public static Player p;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        p = Bukkit.getPlayer(args[0]);
        for (int i = 0; i < 30; i++)
        {
            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run()
                {
                    p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                }
            }, 3L * i);
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.MAGIC + "___" + ChatColor.RESET + " " + ChatColor.AQUA + ChatColor.BOLD + p.getName() + " has completed a parkour challenge! " + ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.MAGIC + "___");
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run()
            {
                if (!(p.hasPermission("group.admin")))
                {
                    p.chat("/spawn");
                }
            }
        }, 80L);
        return false;
    }
}
