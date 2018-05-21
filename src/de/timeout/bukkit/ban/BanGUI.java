package de.timeout.bukkit.ban;

import java.io.File;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.timeout.bukkit.ban.commands.AddreasonCommand;
import de.timeout.bukkit.ban.commands.BanCommand;
import de.timeout.bukkit.ban.commands.DelreasonCommand;
import de.timeout.bukkit.ban.commands.MuteCommand;
import de.timeout.bukkit.ban.commands.UnbanCommand;
import de.timeout.bukkit.ban.commands.UnmuteCommand;
import de.timeout.bukkit.ban.gui.AddreasonGUI;
import de.timeout.bukkit.ban.gui.BanCommandGUI;
import de.timeout.bukkit.ban.gui.MuteCommandGUI;
import de.timeout.bukkit.ban.utils.UTFConfig;
import de.timeout.utils.MySQL;

public class BanGUI extends JavaPlugin {
	
	public static BanGUI plugin;
	private UTFConfig config;

	@Override
	public void onEnable() {
		plugin = this;
		ConfigCreator.loadConfigs();
		config = new UTFConfig(new File(getDataFolder(), "config.yml"));
		
		registerCommands();
		registerListener();
		
		//MySQL
		MySQL.connect(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"),
				getConfig().getString("mysql.username"), getConfig().getString("mysql.password"));
		if(MySQL.isConnected())Bukkit.getConsoleSender().sendMessage(getLanguage("prefix") + getLanguage("mysql.connected"));
		setupMySQL();
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BanSystem");
	}
	
	@Override
	public void onDisable() {
		MySQL.disconnect();
		if(!MySQL.isConnected())Bukkit.getConsoleSender().sendMessage(getLanguage("prefix") + getLanguage("mysql.disconnected"));
	}
	
	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(new BanCommandGUI(), this);
		Bukkit.getPluginManager().registerEvents(new MuteCommandGUI(), this);
		Bukkit.getPluginManager().registerEvents(new AddreasonGUI(), this);
	}
	
	private void registerCommands() {
		this.getCommand("ban").setExecutor(new BanCommand());
		this.getCommand("mute").setExecutor(new MuteCommand());
		this.getCommand("addreason").setExecutor(new AddreasonCommand());
		this.getCommand("delreason").setExecutor(new DelreasonCommand());
		this.getCommand("unban").setExecutor(new UnbanCommand());
		this.getCommand("unmute").setExecutor(new UnmuteCommand());
	}
	
	private void setupMySQL() {
		try {
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Settings(Name VARCHAR(100), FirstBan BIGINT, SecondBan BIGINT, ThirdBan BIGINT, First BIGINT, Second BIGINT, Points BIGINT, Type VARCHAR(100), Display VARCHAR(100), Title TEXT)").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS History(UUID VARCHAR(100), Violence BIGINT)").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bans(UUID VARCHAR(100), IP VARCHAR(100), Name VARCHAR(100), Reason VARCHAR(100), Unban BIGINT, Banner VARCHAR(100))").executeUpdate();
			MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Mutes(UUID VARCHAR(100), IP VARCHAR(100), Name VARCHAR(100), Reason VARCHAR(100), Unmute BIGINT, Muter VARCHAR(100))").executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}
	}

	@Override
	public UTFConfig getConfig() {
		return config;
	}
	
	public String getLanguage(String path) {
		return ConfigManager.getLanguageConfig().getString(path).replaceAll("&", "§");
	}
}
