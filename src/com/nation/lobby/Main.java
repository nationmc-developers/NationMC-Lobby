package com.nation.lobby;

import com.nation.lobby.eventmodule.EventCommands;
import com.nation.lobby.eventmodule.EventListeners;
import com.nation.lobby.listeners.GeneralMovementListener;
import com.nation.lobby.pvpmanager.ArenaState;
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
        System.out.println("NationMC Lobby: Enabled");
        getCommand("e").setExecutor(new EventCommands());
        getCommand("won").setExecutor(new Win(this));
        getCommand("pvp").setExecutor(new QueuePvPCommand(this));


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
        QueuePvPCommand.PvPState = ArenaState.IDLE;
        QueuePvPCommand.prepareMode = false;
        for (World w : Bukkit.getWorlds())
        {
            EventCommands.flags.put(w, Arrays.asList(false, false, true));
        }
    }

    @Override
    public void onDisable()
    {
        System.out.println("NationMC Lobby: Disabled");
    }
}