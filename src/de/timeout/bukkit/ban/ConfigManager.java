package de.timeout.bukkit.ban;

import java.io.File;

import de.timeout.bukkit.ban.utils.UTFConfig;

public class ConfigManager {

	private static BanGUI main = BanGUI.plugin;
	
	private static File langFile = null;
	private static UTFConfig langCfg = null;
	
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
	
}
