package de.timeout.bukkit.ban.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timeout.bukkit.ban.api.BanAPI;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			Bukkit.getServer().broadcastMessage(String.valueOf(BanAPI.isBanned(p.getUniqueId())));
		}
		return false;
	}

}
