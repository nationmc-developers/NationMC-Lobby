package com.nation.lobby;

import com.nation.lobby.eventmodule.EventCommands;
import com.nation.lobby.eventmodule.EventListeners;
import com.nation.lobby.listeners.GeneralMovementListener;
import com.nation.lobby.pvpmanager.PvPListener;
import com.nation.lobby.pvpmanager.QueuePvPCommand;
import com.nation.lobby.pvpmanager.Win;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;


public class Main extends JavaPlugin {



    @Override
    public void onEnable()
    {
        System.out.println("[NationMC] NationMC Core: Enabled");
        getCommand("e").setExecutor(new EventCommands());
        getCommand("won").setExecutor(new Win(this));
        getCommand("pvp").setExecutor(new QueuePvPCommand());


        Bukkit.getPluginManager().registerEvents(new PvPListener(), this);
        Bukkit.getPluginManager().registerEvents(new GeneralMovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new EventListeners(), this);


        if (Bukkit.getServer().getPluginManager().getPlugin("TitleAPI").isEnabled())
        {
            System.out.println("Detected TitleAPI. Using as Hook.");
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard").isEnabled())
        {
            System.out.println("Detected WorldGuard. Using as Hook.");
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").isEnabled())
        {
            System.out.println("Detected WorldEdit. Using as Hook.");
        }
        QueuePvPCommand.PvPState = "IDLE";
        for (World w : Bukkit.getWorlds())
        {
            EventCommands.flags.put(w, Arrays.asList(false, false, true));
        }
    }

    @Override
    public void onDisable()
    {
        System.out.println("NationMC Core: Disabled");
    }
}