package de.timeout.bukkit.ban.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.manager.DecidationManager;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.ReasonType;

public class UnbanCommand implements CommandExecutor {
	
	private BanGUI main = BanGUI.plugin;
	
	private String prefix = main.getLanguage("prefix");
	// [name] required Playername
	private String unban = main.getLanguage("ban.unban");
	private String permission = main.getLanguage("error.permissions");
	private String notbanned = main.getLanguage("error.notBanned");
	// [command] required CommandName
	private String falsecmd = main.getLanguage("error.falseCommand");

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("easyban.unban")) {
			if(args.length == 1) {
				String name = args[0];
				OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(name);
				if(DecidationManager.isBanned(op.getUniqueId())) {
					Reason reason = new Reason(BukkitSQLManager.getBanReasonName(name), ReasonType.BAN);
					UUID uuid = Bukkit.getServer().getOfflinePlayer(name).getUniqueId();
					BukkitSQLManager.updateHistory(uuid, -reason.getViolencePoints());
					BukkitSQLManager.unban(uuid);
					sender.sendMessage(prefix + unban.replace("[name]", name));
				} else sender.sendMessage(prefix + notbanned);
			} else sender.sendMessage(prefix + falsecmd.replace("[command]", "/unban <Name>"));
		} else sender.sendMessage(prefix + permission);
		return false;
	}

//	private UUID getUUIDFromMojangServer(String name) {
//		String url = "https://api.mojang.com/users/profiles/minecraft/" + name.toLowerCase();
//		
//		try {
//			InputStreamReader reader = new InputStreamReader(new URL(url).openStream());
//			String trimmedUUID = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
//			
//			return fromTrimmed(trimmedUUID);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	private UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
//	    if(trimmedUUID == null) throw new IllegalArgumentException();
//	    StringBuilder builder = new StringBuilder(trimmedUUID.trim());
//	    try {
//	        builder.insert(20, "-");
//	        builder.insert(16, "-");
//	        builder.insert(12, "-");
//	        builder.insert(8, "-");
//	    } catch (StringIndexOutOfBoundsException e){
//	        throw new IllegalArgumentException();
//	    }
//	 
//	    return UUID.fromString(builder.toString());
//	}
}
