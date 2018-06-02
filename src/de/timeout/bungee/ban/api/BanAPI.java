package de.timeout.bungee.ban.api;

import java.util.UUID;

import de.timeout.bungee.ban.filemanager.DecidationManager;
import de.timeout.bungee.ban.manager.BanManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.ReasonType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanAPI {
	public static void banPlayer(ProxiedPlayer banned, String banner, String reason) {
		Reason r = new Reason(reason, ReasonType.BAN);
		BanManager.ban(banned, banner, r);
	}

	public static void banPlayer(String ip, UUID uuid, String name, String banner, String reason) {
		Reason r = new Reason(reason, ReasonType.BAN);
		BanManager.banOffline(ip, uuid, name, banner, r);
	}
	
	public static void permabanPlayer(ProxiedPlayer banned, String banner) {
		BanManager.permaban(banned, banner);
	}
	
	public static void permabanPlayer(UUID uuid, String ip, String name, String banner) {
		BanManager.permabanOffline(ip, uuid, name, banner);
	}
	
	public static void custombanPlayer(ProxiedPlayer banned, String banner, long days, long hours, long minutes) {
		BanManager.customban(banned, days, hours, minutes, banner);
	}
	
	public static void custombanPlayer(String ip, UUID uuid, String name, String banner, long days, long hours, long minutes) {
		BanManager.custombanOffline(uuid, ip, name, days, hours, minutes, banner);
	}
	
	public static void mutePlayer(ProxiedPlayer muted, String muter, String reason) {
		Reason r = new Reason(reason, ReasonType.MUTE);
		BanManager.mute(muted, muter, r);
	}

	public static void mutePlayer(String ip, UUID uuid, String name, String muter, String reason) {
		Reason r = new Reason(reason, ReasonType.MUTE);
		BanManager.muteOffline(uuid, ip, name, muter, r);
	}
	
	public static void permamutePlayer(ProxiedPlayer muted, String muter) {
		BanManager.permamute(muted, muter);
	}
	
	public static void permamutePlayer(String ip, UUID uuid, String name, String muter) {
		BanManager.permamuteOffline(uuid, ip, name, muter);
	}
	
	public static void custommutePlayer(ProxiedPlayer muted, String muter, long days, long hours, long minutes) {
		BanManager.custommute(muted, days, hours, minutes, muter);
	}
	
	public static void custommutePlayer(String ip, UUID uuid, String name, String muter, long days, long hours, long minutes) {
		BanManager.custommuteOffline(uuid, ip, name, days, hours, minutes, muter);
	}
	
	public static boolean isMuted(ProxiedPlayer player) {
		return isMuted(player.getUniqueId());
	}
	
	public static boolean isMuted(UUID uuid) {
		return DecidationManager.isMuted(uuid);
	}
	
	public static boolean isBanned(ProxiedPlayer player) {
		return isBanned(player.getUniqueId());
	}
	
	public static boolean isBanned(UUID uuid) {
		return DecidationManager.isBanned(uuid);
	}
	
}
