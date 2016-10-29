package com.github._josephcw.itempickuplock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemPickupLock extends JavaPlugin implements Listener {

	/*
	 * TODO - Return list of iLocked Players
	 * 	Use MongoDB to persist locked players across restart
	 * 	Add all players to locked? / Remove all players from locked?
	 * 	Temporary lock for a set duration?
	 * 	Lock from pulling items out of chest, furnace, etc
	 */
	
	private List<Player> lockedItemPlayers;
	private final String commandUsage = ChatColor.RED + "Command Usage: //il" + "CUSED" + " <player>";
	
	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		lockedItemPlayers = new ArrayList<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(commandUsage.replace("CUSED", commandLabel));
		} else {
			Player p = Bukkit.getPlayerExact(args[0]);
			if (p != null) {
				if (lockedItemPlayers.contains(p)) {
					lockedItemPlayers.remove(p);
					sender.sendMessage(ChatColor.DARK_GREEN + p.getName() + " can now pickup items!");
				} else {
					lockedItemPlayers.add(p);
					sender.sendMessage(ChatColor.DARK_RED + p.getName() + " can no longer pickup items!");
				}
			}
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if(lockedItemPlayers.contains(e.getPlayer()))
			e.setCancelled(true);
	}
}
