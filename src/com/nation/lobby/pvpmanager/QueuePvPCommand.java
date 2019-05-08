package com.nation.lobby.pvpmanager;

import com.connorlinfoot.titleapi.TitleAPI;
import com.nation.lobby.Main;
import com.nation.lobby.functions.F;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class QueuePvPCommand implements CommandExecutor {
    public static Player QueueOne;
    public static Player QueueTwo;
    public static ArenaState PvPState;
    private static ItemStack helmet;
    private static ItemStack chestplate;
    private static ItemStack leggings;
    private static ItemStack boots;
    private static Player lastQueuer;
    public static boolean prepareMode;
    private static Main main;

    private static final int[] PVP_COORDS = {1, 90, -32};

    public QueuePvPCommand(Main main1)
    {
        this.main = main1;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player p;
        //pvp <player> - Queues player for PvP (console command)
        helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        if (sender.hasPermission("group.admin"))
        {
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("end"))
                {
                    PvPState = ArenaState.IDLE;
                    sender.sendMessage(F.adminAction("PvP", "Force-ending PvP"));
                    endpvp(QueuePvPCommand.QueueOne, QueuePvPCommand.QueueTwo, null);
                    return true;
                } else
                {
                    try
                    {
                        p = Bukkit.getPlayer(args[0]);
                    } catch (Exception e)
                    {
                        sender.sendMessage(F.error("PvP", "That user does not exist."));
                        return true;
                    }
                    sender.sendMessage(F.adminAction("PvP", "Force-queued PvP for:" + F.value(p.getName(), ".")));
                }
            } else
            {
                p = (Player) sender;
            }
        } else
        {
            p = (Player) sender;
        }

        if (QueueOne == null)
        {
            QueueOne = p;
            p.sendMessage(F.main("PvP", "You have been queued for PvP."));
            if (lastQueuer != p)
            {
                F.world(p.getWorld(), true, F.miniAnnouncement("PvP", F.MAvalue(p.getName(), "has queued for PvP!")));
                F.world(p.getWorld(), false, F.header("TIP") + ChatColor.DARK_GRAY + "Use " + ChatColor.GRAY + "/pvp" + ChatColor.DARK_GRAY + " to fight! " + F.getValueColor() + "(1/2)");
            }
            lastQueuer = p;

        } else if (QueueTwo == null)
        {
            if (p != QueueOne)
            {
                QueueTwo = p;
                startpvp(QueueOne, QueueTwo);
                p.sendMessage(F.main("PvP", "You have been queued for PvP."));
                QueueOne.sendMessage(F.main("PvP", "Your match is starting..."));
                QueueTwo.sendMessage(F.main("PvP", "Your match is starting..."));
            } else
            {
                p.sendMessage(F.main("PvP", "You have been unqueued from PvP."));
                QueueOne = null;
            }
        } else if (QueueOne != null && QueueTwo != null)
        {
            p.sendMessage(F.error("PvP", "Two players are already queued, please wait and try again later."));
        }
        return false;
    }

    public static void startpvp(Player p1, Player p2)
    {
        PvPState = ArenaState.ACTIVE;
        Player[] players = { p1, p2 };
        int[][] corrds = { {-8, 90, -38}, {10, 90, -38}};
        int i = 0;
        for (Player p : players)
        {
            p.teleport(new Location(p.getWorld(), PVP_COORDS[0], PVP_COORDS[1], PVP_COORDS[2]));
            p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, .5F, 1F);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit pvp " + p.getName());
            p.setHealth(20L);
            p.setSaturation(20L);
            p.getInventory().setHelmet(helmet);
            p.getInventory().setChestplate(chestplate);
            p.getInventory().setLeggings(leggings);
            p.getInventory().setBoots(boots);
            int[] singleCoords = corrds[i];
            players[i].teleport(new Location(p.getWorld(), singleCoords[0], singleCoords[1], singleCoords[2]));
            p.playSound(p.getLocation(), Sound.CLICK, 1F, 1F);
            p.sendMessage(F.main("PvP", "Your PvP match will start in 5 seconds... PREPARE FOR BATTLE!"));
            i++;

        }
        prepareMode = true;
        Bukkit.getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run()
            {
                prepareMode = false;
                if (PvPState.equals(ArenaState.ACTIVE))
                {
                    for (Player p : Arrays.asList(QueueOne, QueueTwo))
                    {
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
                        TitleAPI.sendTitle(p, 10, 20, 10, ChatColor.RED + "" + ChatColor.BOLD + "PVP MATCH", ChatColor.BOLD + "FIGHT!");
                    }
                }
            }
        }, 100L);
    }

    public static void endpvp(Player p1, Player p2, Player winner)
    {
        PvPState = ArenaState.IDLE;
        for (Player p : Arrays.asList(p1, p2))
        {
            p.getInventory().clear();
            if (winner == null)
            {
                p.sendMessage(F.adminAction("PvP", "PvP has been force-ended. There is no winner"));
            } else
            {
                p.sendMessage(F.main("PvP", "PvP ended, winner is..." + F.value(winner.getName(), ".")));
            }
            p.chat("/spawn");
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            p.getInventory().setBoots(null);
        }
        QueuePvPCommand.QueueOne = null;
        QueuePvPCommand.QueueTwo = null;
    }
}
