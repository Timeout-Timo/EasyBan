package de.timeout.bungee.ban;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {

	private static BanSystem main = BanSystem.plugin;
	
	private static File langFile = null;
	private static Configuration langCfg = null;
	
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
}
