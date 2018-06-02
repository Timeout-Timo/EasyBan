package de.timeout.bukkit.ban;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteStreams;

public class ConfigCreator {

	private static BanGUI main = BanGUI.plugin;
	
	public static void loadConfigs() {
		loadResource(main, "bukkit/config.yml");
		loadResource(main, "bukkit/language/de_DE.yml");
		loadResource(main, "bukkit/language/en_US.yml");
	}
	
	public static void loadDatabases() {
		loadResource(main, "bukkit/databases/iphistory.yml");
		loadResource(main, "bukkit/databases/reasons.yml");
	}
	
	public static void createPlayerFiles(File file, UUID uuid) {
		try {
			file.createNewFile();
			try(InputStream in = main.getResource("playerstats.yml");
					OutputStream out = new FileOutputStream(file)) {
				ByteStreams.copy(in, out);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		if(!folder.exists())folder.mkdirs();
		File resourceFile = new File(folder, resource);
		try {
			if(resource.contains("bukkit/")) {
				String[] folders = resource.substring(7).split("/");
				resourceFile = plugin.getDataFolder();
				for(int i = 0; i < folders.length -1; i++) {
					resourceFile = new File(resourceFile, folders[i]);
					if(!resourceFile.exists())resourceFile.mkdirs();
				}
				resourceFile = new File(resourceFile, folders[folders.length -1]);
			}
			if (!resourceFile.exists()) {
				Bukkit.getConsoleSender().sendMessage("§8[§aOut-Configuration§8] §a" + resource + " §7konnte nicht geladen werden. §aGeneriere Datei...");
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResource(resource);
				OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage("§8[§aOut-Configuration§8] §a" + resource + " §fwurde geladen!");
	}
}
