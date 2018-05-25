package de.timeout.bukkit.ban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.gui.AddreasonGUI;

public class AddreasonCommand implements CommandExecutor {
	
	private BanGUI main = BanGUI.plugin;
	
	private String prefix = main.getLanguage("prefix");
	private String permission = main.getLanguage("error.permissions");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("easyban.addreason")) {
				if(args.length == 2) {
					String name = args[0].toLowerCase();
					String display = args[1];
					AddreasonGUI.addReasonGUI(p, name, display.replaceAll("&", "§"));
				}
			} else p.sendMessage(prefix + permission);
		}
		return false;
	}

}
