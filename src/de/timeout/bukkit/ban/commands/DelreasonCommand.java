package de.timeout.bukkit.ban.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.api.DelReasonEvent;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.BukkitSQLManager;

public class DelreasonCommand implements CommandExecutor {
	
	private static BanGUI main = BanGUI.plugin;
	
	private String prefix = main.getLanguage("prefix");
	
	private String success = main.getLanguage("delreason.success");
	private String notExists = main.getLanguage("delreason.notExists");
	
	private String permissions = main.getLanguage("error.permissions");
	private String falseCMD = main.getLanguage("error.falseCommand");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("easyban.delreason")) {
			if(args.length == 1) {
				String name = args[0];
				if(BukkitSQLManager.reasonExists(name)) {
					BukkitReason reason = new BukkitReason(name, BukkitSQLManager.getType(name));
					DelReasonEvent event = new DelReasonEvent(reason);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						BukkitSQLManager.removeReason(name);
						sender.sendMessage(prefix + success.replace("[reason]", name));
					}
				} else sender.sendMessage(prefix + notExists);
			} else sender.sendMessage(prefix + falseCMD.replace("[command]", "/delreason <Reason>"));
		} else sender.sendMessage(prefix + permissions);
		return false;
	}
}
