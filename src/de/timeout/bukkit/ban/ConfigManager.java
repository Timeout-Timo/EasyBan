package de.timeout.bukkit.ban;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.timeout.bukkit.ban.utils.UTFConfig;
import de.timeout.bungee.ban.ConfigCreator;

public class ConfigManager {

	private static BanGUI main = BanGUI.plugin;
	
	private static File langFile = null;
	private static UTFConfig langCfg = null;
	
	private static File databaseFolder = null;
	
	private static File ipFile = null;
	private static UTFConfig ipCfg = null;
	
	public static void loadLanguageConfig() {
		if(langFile == null) {
			String name = main.getConfig().getString("language") + ".yml";
			langFile = new File(main.getDataFolder().getPath() + "/language", name).exists() ?
					new File(main.getDataFolder().getPath() + "/language", name) : new File(main.getDataFolder().getPath() + "/language", "de_DE.yml");
		}
		langCfg = new UTFConfig(langFile);
	}
	
	public static UTFConfig getLanguageConfig() {
		if(langCfg == null)loadLanguageConfig();
		return langCfg;
	}
	
	public static void reloadDatabases() {
		databaseFolder = new File(main.getDataFolder() + main.getConfig().getString("file.path"));
		if(!databaseFolder.exists())databaseFolder.mkdirs();
	}
	
	private static File getPlayerDatabaseFile(UUID uuid) {
		if(databaseFolder == null)reloadDatabases();
		File file = new File(databaseFolder, uuid.toString() + ".yml");
		if(!file.exists())ConfigCreator.createPlayerFile(file, uuid);
		return file;
	}
	
	public static File getDataBase() {
		return databaseFolder;
	}
	
	public static UTFConfig getPlayerDatabase(UUID uuid) {
		try {
			UTFConfig cfg = new UTFConfig(getPlayerDatabaseFile(uuid));
			return cfg;
		} catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static void saveDatabase(UUID uuid, UTFConfig config) {
		try {
			File file = getPlayerDatabaseFile(uuid);
			config.save(file);
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void reloadIPCache() {
		if(ipFile == null)ipFile = new File(main.getDataFolder().getPath() + "/databases", "iphistory.yml");
		ipCfg = new UTFConfig(ipFile);
	}
	
	public static UTFConfig getIPCache() {
		if(ipCfg == null)reloadIPCache();
		return ipCfg;
	}
	
	public static void saveIPCache(UTFConfig cfg) {
		try {
			cfg.save(ipFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getKnownUUIDs(String ip) {
		try {
			List<String> uuids = getIPCache().getStringList(ip);
			return uuids;
		} catch(NullPointerException e) {}
		return null;
	}
}
