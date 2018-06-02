package de.timeout.bungee.ban.manager;

import java.util.List;
import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.ban.ConfigManager;
import de.timeout.bungee.ban.filemanager.ProxyFileManager;
import de.timeout.utils.DateConverter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class FileExecutorManager implements Listener {
	
	private BanSystem main = BanSystem.plugin;
	
	@EventHandler
	public void onConnect(LoginEvent event) {
		PendingConnection c = event.getConnection();
		String ip = c.getAddress().getAddress().getHostAddress();
		UUID uuid = c.getUniqueId();
		cacheUUID(ip, uuid);
		
		if(ProxyFileManager.isBanned(uuid)) {
			long unban = ProxyFileManager.getUnbanTime(uuid);
			long millis = System.currentTimeMillis();
			if(unban > millis) {
				List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
				
				String banner = ProxyFileManager.getBanner(uuid);
				String display = ProxyFileManager.getPunishedBanDisplay(uuid);
				c.disconnect(new TextComponent(getString(list, display, unban, banner)));
			} else ProxyFileManager.unban(uuid);
			if(main.isIPBanEnabled())ProxyFileManager.updateIPAdress(uuid, ip);
			return;
		}
		
		if(main.isIPBanEnabled()) {
			List<String> uuids = ConfigManager.getKnownUUIDs(ip);
			uuids.forEach(id -> {
				UUID uniqueID = UUID.fromString(id);
				if(ProxyFileManager.isBanned(uniqueID)) {
					long unban = ProxyFileManager.getUnbanTime(uniqueID);
					long millis = System.currentTimeMillis();
					if(unban > millis) {
						List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
						
						String banner = ProxyFileManager.getBanner(uniqueID);
						String display = ProxyFileManager.getPunishedBanDisplay(uniqueID);
						c.disconnect(new TextComponent(getString(list, display, unban, banner)));
					}
					ProxyFileManager.updateIPAdress(uuid, ip);
					return;
				}
			});
		}
	}
	
	@EventHandler
	public void onChat(ChatEvent event) {
		if(!event.isCommand()) {
			if(event.getSender() instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) event.getSender();
				String ip = p.getAddress().getAddress().getHostAddress();
				UUID uuid = p.getUniqueId();
				cacheUUID(ip, uuid);
				
				if(ProxyFileManager.isMuted(uuid)) {
					long unmute = ProxyFileManager.getUnmuteTime(uuid);
					long millis = System.currentTimeMillis();
					if(unmute > millis) {
						event.setCancelled(true);
						List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
						
						String muter = ProxyFileManager.getMuter(uuid);
						String display = ProxyFileManager.getPunishedMuteDisplay(uuid);
						p.sendMessage(new TextComponent(getString(list, display, unmute, muter)));
					} else ProxyFileManager.unmute(uuid);
					if(main.isIPBanEnabled())ProxyFileManager.updateIPAdress(uuid, ip);
					return;
				}
				
				if(!main.isIPBanEnabled()) {
					List<String> uuids = ConfigManager.getKnownUUIDs(ip);
					uuids.forEach(uid -> {
						UUID uniqueID = UUID.fromString(uid);
						if(ProxyFileManager.isMuted(uuid)) {
							long unmute = ProxyFileManager.getUnmuteTime(uuid);
							long millis = System.currentTimeMillis();
							if(unmute > millis) {
								event.setCancelled(true);
								List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
								
								String muter = ProxyFileManager.getMuter(uniqueID);
								String display = ProxyFileManager.getPunishedMuteDisplay(uniqueID);
								p.sendMessage(new TextComponent(getString(list, display, unmute, muter)));
							}
							if(main.isIPBanEnabled())ProxyFileManager.updateIPAdress(uuid, ip);
							return;
						}
					});
				}
			}
		}
	}
	
	private void cacheUUID(String ip, UUID uuid) {
		if(main.isIPBanEnabled()) {
			List<String> uuids = ConfigManager.getKnownUUIDs(ip);
			if(!uuids.contains(uuid.toString())) uuids.add(uuid.toString());
			Configuration cfg = ConfigManager.getIPCache();
			cfg.set(ip, uuids);
			ConfigManager.saveIPCache(cfg);
		}
	}
		
	private static String getString(List<String> list, String display, long bantime, String banner) {
		String s = "";
		for(int i = 0; i < list.size(); i++)s = s + list.get(i).replaceAll("&", "§") + "\n";
		
		s = s.replace("[reason]", display);
		s = s.replace("[date]", DateConverter.getDate(bantime));
		s = s.replace("[banner]", banner);
		s = s.replace("[muter]", banner);
		return s;
	}
}
