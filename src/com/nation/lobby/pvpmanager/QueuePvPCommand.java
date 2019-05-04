package com.nation.lobby.pvpmanager;

import com.nation.lobby.functions.F;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    public static String PvPState;
    private static ItemStack helmet;
    private static ItemStack chestplate;
    private static ItemStack leggings;
    private static ItemStack boots;

    private static final int[] PVP_COORDS = { 1, 90, -32 };
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player p = null;
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
                    PvPState = "IDLE";
                    sender.sendMessage(F.adminAction("PvP", "Force-ending PvP"));
                    for (Player lp : Arrays.asList(QueueOne, QueueTwo))
                    {
                        lp.getInventory().clear();
                        lp.sendMessage(F.adminAction("PvP", "PvP has been force-ended. There is no winner"));
                        lp.chat("/spawn");
                        lp.getInventory().setHelmet(null);
                        lp.getInventory().setChestplate(null);
                        lp.getInventory().setLeggings(null);
                        lp.getInventory().setBoots(null);
                    }
                    QueuePvPCommand.QueueOne = null;
                    QueuePvPCommand.QueueTwo = null;
                    return true;
                } else
                {
                    p = Bukkit.getPlayer(args[0]);
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
        PvPState = "ACTIVE";
        for (Player p : Arrays.asList(p1, p2))
        {
            p.teleport(new Location(p.getWorld(), PVP_COORDS[0] , PVP_COORDS[1], PVP_COORDS[2]));
            p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1F, 1F);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit pvp " + p.getName());
            p.setHealth(20L);
            p.setSaturation(20L);
            p.sendMessage(F.miniAnnouncement("PvP", "Put on your armor and FIGHT!"));
            p.getInventory().setHelmet(helmet);
            p.getInventory().setChestplate(chestplate);
            p.getInventory().setLeggings(leggings);
            p.getInventory().setBoots(boots);
        }
    }

    public static void endpvp(Player p1, Player p2, Player winner)
    {
        PvPState = "IDLE";
        for (Player p : Arrays.asList(p1, p2))
        {
            p.getInventory().clear();
            p.sendMessage(F.main("PvP", "PvP ended, winner is..." + F.value(winner.getName(), ".")));
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
