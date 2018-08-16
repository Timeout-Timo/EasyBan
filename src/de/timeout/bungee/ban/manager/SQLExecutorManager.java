package de.timeout.bungee.ban.manager;

import java.util.List;
import java.util.UUID;

import de.timeout.bungee.ban.ConfigManager;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.DateConverter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SQLExecutorManager implements Listener {

	@EventHandler
	public void onBanExecute(LoginEvent event) {
		PendingConnection p = event.getConnection();
		UUID uuid = p.getUniqueId();
		String ip = p.getAddress().getAddress().getHostAddress();
		if(BungeeSQLManager.isBanned(ip, uuid)) {
			long ipban = BungeeSQLManager.getBanTime(ip);
			long uuidban = BungeeSQLManager.getBanTime(uuid);
			long millis = System.currentTimeMillis();
			
			if((millis >= ipban && millis >= uuidban) && uuidban > -1 && ipban > -1) {
				BungeeSQLManager.unbanPlayer(ip);
				BungeeSQLManager.unbanPlayer(uuid);
			} else if(millis < uuidban || uuidban == -1) {
				String reason = BungeeSQLManager.getBanReason(uuid);
				String banner = BungeeSQLManager.getBanner(uuid);
				List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
				
				p.disconnect(new TextComponent(getString(list, reason, uuidban, banner)));
				BungeeSQLManager.updateIPAddress(ip, p.getName(), uuid);
			} else {
				String reason = BungeeSQLManager.getBanReason(ip);
				String banner = BungeeSQLManager.getBanner(ip);
				List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
				
				p.disconnect(new TextComponent(getString(list, reason, ipban, banner)));
			}
		}
	}
	
	@EventHandler
	public void onMuteExecute(ChatEvent event) {
		if(!event.isCommand()) {
			if(event.getSender() instanceof ProxiedPlayer) {
				ProxiedPlayer p = (ProxiedPlayer) event.getSender();
				
				if(BungeeSQLManager.isMuted(p.getAddress().getAddress().getHostAddress(), p.getUniqueId())) {
					long ipmute = BungeeSQLManager.getMuteTime(p.getUniqueId());
					long uuidmute = BungeeSQLManager.getMuteTime(p.getAddress().getAddress().getHostAddress());
					long millis = System.currentTimeMillis();
					
					if((millis >= ipmute && millis >= uuidmute) && ipmute > -1 && uuidmute > -1) {
						BungeeSQLManager.unmutePlayer(p.getUniqueId());
						BungeeSQLManager.unmutePlayer(p.getAddress().getAddress().getHostAddress());
					} else {
						event.setCancelled(true);
						List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
						String reason = BungeeSQLManager.getMuteReason(p.getUniqueId());
						String muter = BungeeSQLManager.getMuter(p);
						
						p.sendMessage(new TextComponent(getString(list, reason, uuidmute > ipmute ? uuidmute : ipmute, muter)));
					}
				}
			}
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
