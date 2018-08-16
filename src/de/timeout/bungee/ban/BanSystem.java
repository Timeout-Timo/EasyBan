package de.timeout.bungee.ban;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import de.timeout.bungee.ban.manager.FileExecutorManager;
import de.timeout.bungee.ban.manager.SQLExecutorManager;
import de.timeout.bungee.ban.manager.WrapperManager;
import de.timeout.bungee.ban.utils.TabCompleterManager;
import de.timeout.utils.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BanSystem extends Plugin {
	
	public static BanSystem plugin;
	private static Configuration config;
	
	private boolean ip;
	private boolean file;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigCreator.loadConfigurations();
		reloadConfig();
		ip = getConfig().getBoolean("ip");
		String database = getConfig().getString("database");
		
		BungeeCord.getInstance().registerChannel("BanSystem");
				
		if(database.equalsIgnoreCase("mysql") || database.equalsIgnoreCase("sql")) {
			MySQL.connect(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"),
					getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"));
			if(MySQL.isConnected()) {
				setupMySQL();
				getProxy().getConsole().sendMessage(new TextComponent(getLanguage("prefix") + getLanguage("mysql.connected")));
				registerListener();
			} else {
				plugin.getProxy().getConsole().sendMessage(new TextComponent(getLanguage("prefix") + getLanguage("mysql.failed")));
			}
		} else if(database.equalsIgnoreCase("file")) {
			file = true;
			ConfigCreator.loadDatabases();
		} 
	}

	@Override
	public void onDisable() {
		if(MySQL.isConnected())MySQL.disconnect();
	}
	
	private void registerListener() {
		BungeeCord.getInstance().getPluginManager().registerListener(this, new WrapperManager());
		if(MySQL.isConnected())BungeeCord.getInstance().getPluginManager().registerListener(this, new SQLExecutorManager());
		else BungeeCord.getInstance().getPluginManager().registerListener(this, new FileExecutorManager());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new TabCompleterManager());
	}
		
	private void setupMySQL() {
		try {
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Settings(Name VARCHAR(100), FirstBan BIGINT, SecondBan BIGINT, ThirdBan BIGINT, First BIGINT, Second BIGINT, Points BIGINT, Type VARCHAR(100), Display VARCHAR(100), Title TEXT)").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS History(UUID VARCHAR(100), Violence BIGINT)").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bans(UUID VARCHAR(100), IP VARCHAR(100), Name VARCHAR(100), Reason VARCHAR(100), Unban BIGINT, Banner VARCHAR(100))").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Mutes(UUID VARCHAR(100), IP VARCHAR(100), Name VARCHAR(100), Reason VARCHAR(100), Unmute BIGINT, Muter VARCHAR(100))").executeUpdate();
		} catch (SQLException e) {}
	}
	
	public boolean isIPBanEnabled() {
		return ip;
	}
	
	public boolean isUsingFiles() {
		return file;
	}
	
	public void reloadConfig() {
		if(config == null) {
			try {
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public Configuration getConfig() {
		reloadConfig();
		return config;
	}
	
	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getLanguage(String path) {
		return ConfigManager.getLanguage().getString(path).replaceAll("&", "§");
	}
}