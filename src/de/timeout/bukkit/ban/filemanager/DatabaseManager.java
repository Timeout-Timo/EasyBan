package de.timeout.bukkit.ban.filemanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import de.timeout.bukkit.ban.BanGUI;

public class DatabaseManager implements Listener {

	private BanGUI main = BanGUI.plugin;
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if(!main.isBungeeCordEnabled()) {
			
		}
	}
}
