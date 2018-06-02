package de.timeout.bukkit.ban.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonParser;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.filemanager.DecidationManager;
import de.timeout.utils.BukkitSQLManager;

public class BanAPI {
	
	private static BanGUI main = BanGUI.plugin;
	
	public static void banPlayer(Player banned, String banner, String reason) {
		banPlayer(banned.getName(), banner, reason);
	}

	public static void banPlayer(String banned, String banner, String reason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Ban");
		out.writeUTF(banned);
		out.writeUTF(reason);
		out.writeUTF(banner);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static void permabanPlayer(Player banned, String banner) {
		permabanPlayer(banned.getName(), banner);
	}
	
	public static void permabanPlayer(String banned, String banner) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("PermaBan");
		out.writeUTF(banned);
		out.writeUTF(banner);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static void custombanPlayer(Player banned, String banner, long days, long hours, long minutes) {
		custombanPlayer(banned.getName(), banner, days, hours, minutes);
	}
	
	public static void custombanPlayer(String banned, String banner, long days, long hours, long minutes) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("CustomBan");
		out.writeUTF(banned);
		out.writeLong(days);
		out.writeLong(hours);
		out.writeLong(minutes);
		out.writeUTF(banner);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static void mutePlayer(Player banned, String muter, String reason) {
		banPlayer(banned.getName(), muter, reason);
	}

	public static void mutePlayer(String muted, String muter, String reason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Mute");
		out.writeUTF(muted);
		out.writeUTF(reason);
		out.writeUTF(muter);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static void permamutePlayer(Player muted, String muter) {
		permabanPlayer(muted.getName(), muter);
	}
	
	public static void permamutePlayer(String muted, String muter) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("PermaMute");
		out.writeUTF(muted);
		out.writeUTF(muter);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static void custommutePlayer(Player muted, String muter, long days, long hours, long minutes) {
		custombanPlayer(muted.getName(), muter, days, hours, minutes);
	}
	
	public static void custommutePlayer(String muted, String muter, long days, long hours, long minutes) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("CustomMute");
		out.writeUTF(muted);
		out.writeLong(days);
		out.writeLong(hours);
		out.writeLong(minutes);
		out.writeUTF(muter);
		
		main.getServer().sendPluginMessage(main, "BanSystem", out.toByteArray());
	}
	
	public static boolean isMuted(Player player) {
		return isMuted(getUUIDFromMojangServer(player.getName()));
	}
	
	public static boolean isMuted(UUID uuid) {
		return BukkitSQLManager.isMuted(uuid);
	}
	
	private static UUID getUUIDFromMojangServer(String name) {
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
	
	private static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
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
	
	public static boolean isIPBanned(String ip) {
		return BukkitSQLManager.isIPBanned(ip);
	}
	
	public static boolean isBanned(UUID uuid) {
		return DecidationManager.isBanned(uuid);
	}
	
	public static boolean isBanned(Player player) {
		return isBanned(getUUIDFromMojangServer(player.getName()));
	}
}
