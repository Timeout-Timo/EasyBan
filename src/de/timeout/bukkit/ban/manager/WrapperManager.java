package de.timeout.bukkit.ban.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.netty.packets.PacketPlayOutBan;
import de.timeout.bukkit.ban.netty.packets.PacketPlayOutMute;
import de.timeout.bukkit.ban.utils.BukkitReason;

public class WrapperManager {
	
	private static BanGUI main = BanGUI.plugin;
	
	@SuppressWarnings("deprecation")
	public static void manageBan(String banned, BukkitReason reason, Player banner) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutBan packet = new PacketPlayOutBan(banned, reason.getName(), banner.getDisplayName());
			main.getNettyClient().getMainChannel().writeAndFlush(packet);
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.ban(Bukkit.getServer().getPlayer(banned), reason, banner.getDisplayName());
		} else BanManager.banOffline(null, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banned, banner.getDisplayName(), reason);
	}
	
	@SuppressWarnings("deprecation")
	public static void managePermaban(String banned, Player banner) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutBan ban = new PacketPlayOutBan(banned, banner.getDisplayName());
			main.getNettyClient().getMainChannel().writeAndFlush(ban);
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.permaban(Bukkit.getServer().getPlayer(banned), banner.getDisplayName());
		} else BanManager.permabanOffline(null, banned, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banner.getDisplayName());
	}
			
	@SuppressWarnings("deprecation")
	public static void manageCustomban(String banned, long days, long hours, long minutes, Player banner) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutBan ban = new PacketPlayOutBan(banned, banner.getDisplayName(), days, hours, minutes);
			main.getNettyClient().getMainChannel().writeAndFlush(ban);
		} else if(Bukkit.getServer().getOfflinePlayer(banned).isOnline()) {
			BanManager.customban(Bukkit.getServer().getPlayer(banned), banner.getDisplayName(), days, hours, minutes);
		} else BanManager.customban(null, Bukkit.getServer().getOfflinePlayer(banned).getUniqueId(), banned, banner.getDisplayName(), days, hours, minutes);
	}
	
	@SuppressWarnings("deprecation")
	public static void manageMute(String muted, BukkitReason reason, Player muter) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutMute mute = new PacketPlayOutMute(muted, reason.getName(), muter.getDisplayName());
			main.getNettyClient().getMainChannel().writeAndFlush(mute);
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.mute(Bukkit.getServer().getPlayer(muted), muter.getDisplayName(), reason);
		} else BanManager.mute(null, Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), muted, muter.getDisplayName(), reason);
	}
	
	@SuppressWarnings("deprecation")
	public static void managePermamute(String muted, Player muter) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutMute mute = new PacketPlayOutMute(muted, muter.getDisplayName());
			main.getNettyClient().getMainChannel().writeAndFlush(mute);
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.permamute(Bukkit.getServer().getPlayer(muted), muter.getDisplayName());
		} else BanManager.permamute(Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), null, muted, muter.getDisplayName());
	}
	
	@SuppressWarnings("deprecation")
	public static void manageCustomMute(String muted, long days, long hours, long minutes, Player muter) {
		if(main.isBungeeCordEnabled()) {
			PacketPlayOutMute mute = new PacketPlayOutMute(muted, muter.getDisplayName(), days, hours, minutes);
			main.getNettyClient().getMainChannel().writeAndFlush(mute);
		} else if(Bukkit.getServer().getOfflinePlayer(muted).isOnline()) {
			BanManager.custommute(Bukkit.getServer().getPlayer(muted), days, hours, minutes, muter.getDisplayName());
		} else BanManager.custommute(Bukkit.getServer().getOfflinePlayer(muted).getUniqueId(), null, muted, days, hours, minutes, muter.getDisplayName());
	}
}
