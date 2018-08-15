package de.timeout.bungee.ban;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {

	private static BanSystem main = BanSystem.plugin;
	
	private static File databaseFolder = null;
	
	private static File langFile = null;
	private static Configuration langCfg = null;
	
	private static File ipFile = null;
	private static Configuration ipCfg = null;
	
	private static File reasonsFile = null;
	private static Configuration reasonCfg = null;
	
	public static void loadLanguageConfig() throws IOException {
		if(langFile == null) {
			String name = main.getConfig().getString("language") + ".yml";
			langFile = new File(main.getDataFolder().getPath() + "/language", name).exists() ?
					new File(main.getDataFolder().getPath() + "/language", name) : new File(main.getDataFolder().getPath() + "/language", "de_DE.yml");
		}
		langCfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(langFile);
	}
	
	public static Configuration getLanguage() {
			try {
				if(langCfg == null)loadLanguageConfig();
				return langCfg;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
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
	
	public static Configuration getPlayerDatabase(UUID uuid) {
		try {
			Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getPlayerDatabaseFile(uuid));
			return cfg;
		} catch (IOException e) {e.printStackTrace();}
		return null;
	}
	
	public static void saveDatabase(UUID uuid, Configuration cfg) {
		try {
			File file = getPlayerDatabaseFile(uuid);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadIPCache() {
		try {
			if(ipFile == null)ipFile = new File(main.getDataFolder().getPath() + "/databases", "iphistory.yml");
			ipCfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ipFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration getIPCache() {
		if(ipCfg == null)reloadIPCache();
		return ipCfg;
	}
	
	public static void saveIPCache(Configuration cfg) {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, ipFile);
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
	
	public static void reloadReasons() {
		try {
			if(reasonsFile == null)reasonsFile = new File("/databases/reasons.yml");
			reasonCfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(reasonsFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration getReasons() {
		if(reasonCfg == null)reloadReasons();
		return reasonCfg;
	}
	
	public static void saveReasons() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(reasonCfg, reasonsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 }
