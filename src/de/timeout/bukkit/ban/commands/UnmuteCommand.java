package de.timeout.bukkit.ban.commands;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.google.gson.JsonParser;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.ReasonType;

public class UnmuteCommand implements CommandExecutor {
	
	private BanGUI main = BanGUI.plugin;
	
	private String prefix = main.getLanguage("prefix");
	// [name] required PlayerName
	private String unmute = main.getLanguage("mute.unmute");
	
	private String permissions = main.getLanguage("error.permissions");
	// [command] required CommandName
	private String falseCMD = main.getLanguage("error.falseCommand");
	private String notMuted = main.getLanguage("error.notMuted");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("griefking.unmute")) {
			if(args.length == 1) {
				String name = args[0];
				if(BukkitSQLManager.isMuted(name)) {
					Reason reason = new Reason(BukkitSQLManager.getMuteReasonName(name), ReasonType.MUTE);
					UUID uuid = getUUIDFromMojangServer(name);
					BukkitSQLManager.updateHistory(uuid, -reason.getViolencePoints());
					BukkitSQLManager.unmute(uuid);
					sender.sendMessage(prefix + unmute.replace("[player]", name));
				} else sender.sendMessage(prefix + notMuted);
			} else sender.sendMessage(prefix + falseCMD.replace("[command]", "/unmute <Name>"));
		} else sender.sendMessage(prefix + permissions);
		return false;
	}

	private UUID getUUIDFromMojangServer(String name) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + name.toLowerCase();
		
		try {
			InputStreamReader reader = new InputStreamReader(new URL(url).openStream());
			String trimmedUUID = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			
			return fromTrimmed(trimmedUUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
	    if(trimmedUUID == null) throw new IllegalArgumentException();
	    StringBuilder builder = new StringBuilder(trimmedUUID.trim());
	    try {
	        builder.insert(20, "-");
	        builder.insert(16, "-");
	        builder.insert(12, "-");
	        builder.insert(8, "-");
	    } catch (StringIndexOutOfBoundsException e){
	        throw new IllegalArgumentException();
	    }
	 
	    return UUID.fromString(builder.toString());
	}
}
