package com.nation.lobby.eventmodule;

import com.nation.lobby.functions.F;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCommands implements CommandExecutor {
    /*
    Position 0: Block Place
    Position 1: Block Break
    Position 2: Movement

     */
    public static Map<World, List<Boolean>> flags = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player p = (Player) sender;
        World w = p.getWorld();
        if (args[0].equalsIgnoreCase("blockplace") || args[0].equalsIgnoreCase("bp"))
        {

            if (flags.get(w).get(0))
            {
                flags.replace(w, Arrays.asList(false, flags.get(w).get(1), flags.get(w).get(2)));
                message(w, 0, false);
            } else if (!flags.get(w).get(0))
            {
                flags.replace(w, Arrays.asList(true, flags.get(w).get(1), flags.get(w).get(2)));
                message(w, 0, true);
            }

        } else if (args[0].equalsIgnoreCase("blockbreak") || args[0].equalsIgnoreCase("bb"))
        {


            if (flags.get(w).get(1))
            {
                flags.replace(w, Arrays.asList(flags.get(w).get(0), false, flags.get(w).get(2)));
                message(w, 1, false);
            } else if (!flags.get(w).get(1))
            {
                flags.replace(w, Arrays.asList(flags.get(w).get(0), true, flags.get(w).get(2)));
                message(w, 1, true);
            }

        } else if (args[0].equalsIgnoreCase("movement") || args[0].equalsIgnoreCase("m"))
        {

            if (flags.get(w).get(2))
            {
                flags.replace(w, Arrays.asList(flags.get(w).get(0), flags.get(w).get(1), false));
                message(w, 2, false);
            } else if (!flags.get(w).get(2))
            {
                flags.replace(w, Arrays.asList(flags.get(w).get(0), flags.get(w).get(1), true));
                message(w, 2, true);

            }

        } else if (args[0].equalsIgnoreCase("list"))
        {
            String sb = "";
            sb = sb + (F.main("Settings", "Listing values... \n"));
            sb = sb + (F.getdividerWithSpace() + F.eventModule("Block Place", flags.get(w).get(0)) + "\n");
            sb = sb + (F.getdividerWithSpace() + F.eventModule("Block Break", flags.get(w).get(1)) + "\n");
            sb = sb + (F.getdividerWithSpace() + F.eventModule("Movement", flags.get(w).get(2)) + "\n");
            p.sendMessage(sb);
        } else
        {
            p.sendMessage(F.help("Event Module", Arrays.asList("/e blockbreak(bb)", "/e blockplace(bp)", "/e movement(m)", "/e list"),
                    Arrays.asList("Toggle block break", "Toggle block place", "Toggle movement", "List the current values of the event module")));
        }

        return false;
    }

    private void message(World targetWorld, Integer i, Boolean value)
    {
        if (i == 0)
        {
            for (Player p : targetWorld.getPlayers())
            {
                p.sendMessage(F.eventModule("Block Place", value));
                F.ping(p);
            }
        } else if (i == 1)
        {
            for (Player p : targetWorld.getPlayers())
            {
                p.sendMessage(F.eventModule("Block Break", value));
                F.ping(p);
            }
        } else if (i == 2)
        {
            for (Player p : targetWorld.getPlayers())
            {
                p.sendMessage(F.eventModule("Movement", value));
                F.ping(p);
            }
        }
    }
}
