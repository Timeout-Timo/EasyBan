package de.timeout.bukkit.ban.filemanager;

import java.util.HashMap;
import java.util.UUID;

import de.timeout.bukkit.ban.ConfigManager;
import de.timeout.bukkit.ban.utils.UTFConfig;

public class BukkitFileManager {
	
	private static HashMap<UUID, UTFConfig> configCache = new HashMap<UUID, UTFConfig>();

	public static boolean isBanned(UUID uuid) {
		try {
			if(!configCache.containsKey(uuid)) {
				UTFConfig cfg = ConfigManager.getPlayerDatabase(uuid);
				configCache.put(uuid, cfg);
				return cfg.getBoolean("banned");
			} else return configCache.get(uuid).getBoolean("banned");
		} catch(Exception e) {e.printStackTrace();}
		return false;
	}
}
