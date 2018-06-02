package de.timeout.bungee.ban.filemanager;

import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.utils.BungeeSQLManager;

public class DecidationManager {
	
	private static BanSystem main = BanSystem.plugin;

	private static boolean isUsingFiles = main.isUsingFiles();
	
	public static boolean isMuted(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.isMuted(uuid) : BungeeSQLManager.isMutedByUUID(uuid);
	}
	
	public static boolean isBanned(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.isBanned(uuid) : BungeeSQLManager.isBannedByUUID(uuid);
	}
	
	public static long getPlayerViolences(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.getViolences(uuid) : BungeeSQLManager.getPlayerViolences(uuid);
	}
	
	public static void updateHistory(UUID uuid, long violations) {
		if(isUsingFiles)ProxyFileManager.updatePlayerViolences(uuid, violations);
		else BungeeSQLManager.updateHistory(uuid, violations);
	}
	
	public static void addBan(UUID uuid, String ip, String name, long duration, String reason, String banner) {
		if(isUsingFiles)ProxyFileManager.addBan(uuid, ip, name, duration, banner, reason);
		else BungeeSQLManager.addBan(uuid, ip, name, duration, banner, reason);
	}
	
	public static void addMute(UUID uuid, String ip, String name, long duration, String reason, String muter) {
		if(isUsingFiles)ProxyFileManager.addMute(uuid, ip, name, duration, muter, reason);
		else BungeeSQLManager.addMute(uuid, ip, name, duration, muter, reason);
	}
	
	public static long getUnmuteTime(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.getUnmuteTime(uuid) : BungeeSQLManager.getMuteTime(uuid);
	}
	
	public static long getUnbanTime(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.getUnbanTime(uuid) : BungeeSQLManager.getBanTime(uuid);
	}
	
	public static String getPunishedBanDisplay(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.getPunishedBanDisplay(uuid) : BungeeSQLManager.getBanReason(uuid);
	}
	
	public static String getPunishedMuteDisplay(UUID uuid) {
		return isUsingFiles ? ProxyFileManager.getPunishedMuteDisplay(uuid) : BungeeSQLManager.getMuteReason(uuid);
	}
}