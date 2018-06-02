package de.timeout.bungee.ban.filemanager;

import java.util.HashMap;
import java.util.UUID;

import de.timeout.bungee.ban.ConfigManager;
import net.md_5.bungee.config.Configuration;

public class ProxyFileManager {
		
	private static HashMap<UUID, Configuration> configCache = new HashMap<UUID, Configuration>();

	public static boolean isMuted(UUID uuid) {
		try {
			if(!configCache.containsKey(uuid)) {
				Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
				configCache.put(uuid, cfg);
				return cfg.getBoolean("muted");
			} else return configCache.get(uuid).getBoolean("muted");
		} catch (Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean isBanned(UUID uuid) {
		try {
			if(!configCache.containsKey(uuid)) {
				Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
				configCache.put(uuid, cfg);
				return cfg.getBoolean("banned");
			} else return configCache.get(uuid).getBoolean("banned");
		} catch (Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static long getViolences(UUID uuid) {
		try {
			if(!configCache.containsKey(uuid)) {
				Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
				configCache.put(uuid, cfg);
				return cfg.getLong("violences");
			} else return configCache.get(uuid).getLong("violences");
		} catch (Exception e) {e.printStackTrace();}
		return 0L;
	}
	
	public static void updatePlayerViolences(UUID uuid, long violences) {
		try {
			Configuration cfg;
			if(!configCache.containsKey(uuid)) {
				cfg = ConfigManager.getPlayerDatabase(uuid);
				configCache.put(uuid, cfg);
			} else cfg = configCache.get(uuid);
			
			cfg.set("violences", violences);
			ConfigManager.saveDatabase(uuid, cfg);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private static int getFreeSlot(Configuration section) {
		int i = 0;
		while(section.get(String.valueOf(i)) != null)i++;
		return i;
	}
	
	public static void addBan(UUID uuid, String ip, String name, long duration, String banner, String reason) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			cfg.set("ip", ip);
			cfg.set("banned", true);
			cfg.set("name", name);
			long oldtime = cfg.getLong("unban");
			if(oldtime > 0)cfg.set("unban", duration > 0 ? oldtime + duration : duration);
			else if(oldtime == 0) cfg.set("unban", duration > 0 ? duration + System.currentTimeMillis() : duration);
			int freeslot = getFreeSlot(cfg.getSection("history.bans"));
			cfg.set("history.bans." + freeslot + ".banner", banner);
			cfg.set("history.bans." + freeslot + ".reason", reason);
			cfg.set("history.bans." + freeslot + ".unban", cfg.getLong("unban"));
			ConfigManager.saveDatabase(uuid, cfg);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void addMute(UUID uuid, String ip, String name, long duration, String muter, String reason) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			cfg.set("ip", ip);
			cfg.set("muted", true);
			cfg.set("name", name);
			long oldtime = cfg.getLong("unmute");
			if(oldtime > 0) cfg.set("unmute", duration > 0 ? oldtime + duration : duration);
			else if(oldtime == 0) cfg.set("unmute", duration > 0 ? duration + System.currentTimeMillis() : duration);
			int freeslot = getFreeSlot(cfg.getSection("history.mutes"));
			cfg.set("history.mutes." + freeslot + ".muter", muter);
			cfg.set("history.mutes." + freeslot + ".reason", reason);
			cfg.set("history.mutes." + freeslot + ".unmute", cfg.getLong("unmute"));
			ConfigManager.saveDatabase(uuid, cfg);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static long getUnmuteTime(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			return cfg.getLong("unmute");
		} catch(Exception e) {e.printStackTrace();}
		return 0L;
	}
	
	public static long getUnbanTime(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			return cfg.getLong("unban");
		} catch (Exception e) {e.printStackTrace();}
		return 0L;
	}
	
	public static String getPunishedBanDisplay(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			int lasthis = getFreeSlot(cfg.getSection("history.bans")) -1;
			return cfg.getString("history.bans." + lasthis + ".reason");
		} catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static String getPunishedMuteDisplay(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			int lasthis = getFreeSlot(cfg.getSection("history.mutes")) -1;
			return cfg.getString("history.mutes." + lasthis + ".reason");
		} catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static String getBanner(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			int last = getFreeSlot(cfg.getSection("history.bans")) -1;
			return cfg.getString("history.bans." + last + ".banner");
		} catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static String getMuter(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			int last = getFreeSlot(cfg.getSection("history.mutes")) -1;
			return cfg.getString("history.mutes." + last + ".muter");
		} catch(Exception e ) {e.printStackTrace();}
		return null;
	}
	
	public static void unban(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			cfg.set("unban", 0);
			cfg.set("banned", false);
			ConfigManager.saveDatabase(uuid, cfg);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void unmute(UUID uuid) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			cfg.set("unmute", 0);
			cfg.set("muted", false);
			ConfigManager.saveDatabase(uuid, cfg);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void updateIPAdress(UUID uuid, String ip) {
		try {
			Configuration cfg = ConfigManager.getPlayerDatabase(uuid);
			cfg.set("ip", ip);
			ConfigManager.saveDatabase(uuid, cfg);
		} catch(Exception e) {e.printStackTrace();}
	}
}
