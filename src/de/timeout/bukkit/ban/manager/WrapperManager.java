package de.timeout.bukkit.ban.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.utils.BukkitReason;

public class WrapperManager {
	
	private static BanGUI main = BanGUI.plugin;
	
	@SuppressWarnings("deprecation")
	public static void manageBan(String banned, BukkitReason reason, Player banner) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Ban");
			out.writeUTF(banned);
			out.writeUTF(reason.getName());
			out.writeUTF(banner.getDisplayName());
			
			banner.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.ban(Bukkit.getServer().getPlayer(banned), reason, banner.getDisplayName());
		} else BanManager.banOffline(null, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banned, banner.getDisplayName(), reason);
	}
	
	@SuppressWarnings("deprecation")
	public static void managePermaban(String banned, Player banner) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PermaBan");
			out.writeUTF(banned);
			out.writeUTF(banner.getDisplayName());
			
			banner.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.permaban(Bukkit.getServer().getPlayer(banned), banner.getDisplayName());
		} else BanManager.permabanOffline(null, banned, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banner.getDisplayName());
	}
			
	@SuppressWarnings("deprecation")
	public static void manageCustomban(String banned, long days, long hours, long minutes, Player banner) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("CustomBan");
			out.writeUTF(banned);
			out.writeLong(days);
			out.writeLong(hours);
			out.writeLong(minutes);
			out.writeUTF(banner.getDisplayName());
			
			banner.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.customban(Bukkit.getServer().getPlayer(banned), banner.getDisplayName(), days, hours, minutes);
		} else BanManager.customban(null, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banned, banner.getDisplayName(), days, hours, minutes);
	}
	
	@SuppressWarnings("deprecation")
	public static void manageMute(String muted, BukkitReason reason, Player muter) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Mute");
			out.writeUTF(muted);
			out.writeUTF(reason.getName());
			out.writeUTF(muter.getDisplayName());
			
			muter.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.mute(Bukkit.getServer().getPlayer(muted), muter.getDisplayName(), reason);
		} else BanManager.mute(null, Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), muted, muter.getDisplayName(), reason);
	}
	
	@SuppressWarnings("deprecation")
	public static void managePermamute(String muted, Player muter) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("PermaMute");
			out.writeUTF(muted);
			out.writeUTF(muter.getDisplayName());
			
			muter.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.permamute(Bukkit.getServer().getPlayer(muted), muter.getDisplayName());
		} else BanManager.permamute(Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), null, muted, muter.getDisplayName());
	}
	
	@SuppressWarnings("deprecation")
	public static void manageCustomMute(String muted, long days, long hours, long minutes, Player muter) {
		if(main.isBungeeCordEnabled()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("CustomMute");
			out.writeUTF(muted);
			out.writeLong(days);
			out.writeLong(hours);
			out.writeLong(minutes);
			out.writeUTF(muter.getDisplayName());
			
			muter.sendPluginMessage(main, "BanSystem", out.toByteArray());
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.custommute(Bukkit.getServer().getPlayer(muted), days, hours, minutes, muter.getDisplayName());
		} else BanManager.custommute(Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), null, muted, days, hours, minutes, muter.getDisplayName());
	}
	
//	private static UUID getUUIDFromMojangServer(String name) {
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
//	private static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
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
