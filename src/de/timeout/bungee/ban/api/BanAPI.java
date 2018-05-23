package de.timeout.bungee.ban.api;

import java.util.UUID;

import de.timeout.bungee.ban.manager.BanManager;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.ReasonType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanAPI {
	public static void banPlayer(ProxiedPlayer banned, String banner, String reason) {
		Reason r = new Reason(reason, ReasonType.BAN);
		BanManager.ban(banned, banner, r);
	}

	public static void banPlayer(String ip, UUID uuid, String banner, String reason) {
		Reason r = new Reason(reason, ReasonType.BAN);
		BanManager.banOffline(ip, uuid, banner, r);
	}
	
	public static void permabanPlayer(ProxiedPlayer banned, String banner) {
		BanManager.permaban(banned, banner);
	}
	
	public static void permabanPlayer(UUID uuid, String ip, String banner) {
		BanManager.permabanOffline(ip, uuid, banner);
	}
	
	public static void custombanPlayer(ProxiedPlayer banned, String banner, long days, long hours, long minutes) {
		BanManager.customban(banned, days, hours, minutes, banner);
	}
	
	public static void custombanPlayer(String ip, UUID uuid, String banner, long days, long hours, long minutes) {
		BanManager.custombanOffline(uuid, ip, days, hours, minutes, banner);
	}
	
	public static void mutePlayer(ProxiedPlayer muted, String muter, String reason) {
		Reason r = new Reason(reason, ReasonType.MUTE);
		BanManager.mute(muted, muter, r);
	}

	public static void mutePlayer(String ip, UUID uuid, String muter, String reason) {
		Reason r = new Reason(reason, ReasonType.MUTE);
		BanManager.muteOffline(uuid, ip, muter, r);
	}
	
	public static void permamutePlayer(ProxiedPlayer muted, String muter) {
		BanManager.permamute(muted, muter);
	}
	
	public static void permamutePlayer(String ip, UUID uuid, String muter) {
		BanManager.permamuteOffline(uuid, ip, muter);
	}
	
	public static void custommutePlayer(ProxiedPlayer muted, String muter, long days, long hours, long minutes) {
		BanManager.custommute(muted, days, hours, minutes, muter);
	}
	
	public static void custommutePlayer(String ip, UUID uuid, String muter, long days, long hours, long minutes) {
		BanManager.custommuteOffline(uuid, ip, days, hours, minutes, muter);
	}
	
	public static boolean isMuted(ProxiedPlayer player) {
		return isMuted(player.getAddress().getAddress().getHostAddress(), player.getUniqueId());
	}
	
	public static boolean isMuted(String ip, UUID uuid) {
		return BungeeSQLManager.isMuted(ip, uuid);
	}
	
	public static boolean isBanned(ProxiedPlayer player) {
		return isMuted(player.getAddress().getAddress().getHostAddress(), player.getUniqueId());
	}
	
	public static boolean isBanned(String ip, UUID uuid) {
		return BungeeSQLManager.isBanned(ip, uuid);
	}
	
}
