package de.timeout.bukkit.ban.manager;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.timeout.bukkit.ban.ConfigManager;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.DateConverter;

public class ExecutorManager implements Listener {
	
	@EventHandler
	public void onBanExecute(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		UUID uuid = p.getUniqueId();
		String ip = p.getAddress().getAddress().getHostAddress();
		if(BukkitSQLManager.isBanned(ip, uuid)) {
			long ipban = BukkitSQLManager.getBanTime(ip);
			long uuidban = BukkitSQLManager.getBanTime(uuid);
			long millis = System.currentTimeMillis();
			
			if((millis >= ipban && millis >= uuidban) && uuidban > -1 && ipban > -1) {
				BukkitSQLManager.unban(uuid);
				BukkitSQLManager.unban(ip);
			} else if(millis < uuidban) {
				String reason = BukkitSQLManager.getBanReasonName(uuid);
				String banner = BukkitSQLManager.getBanner(uuid);
				List<String> list = ConfigManager.getLanguageConfig().getStringList("ban.screen");
				
				p.kickPlayer(getString(list, reason, uuidban, banner));
				BukkitSQLManager.updateIPAddress(ip, p.getName(), uuid);
			} else {
				String reason = BukkitSQLManager.getBanReason(ip);
				String banner = BukkitSQLManager.getBanner(ip);
				List<String> list = ConfigManager.getLanguageConfig().getStringList("ban.screen");
				
				p.kickPlayer(getString(list, reason, ipban, banner));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		if(!event.getMessage().startsWith("/")) {
			Player p = event.getPlayer();
			String ip = p.getAddress().getAddress().getHostAddress();
			UUID uuid = p.getUniqueId();
			
			if(BukkitSQLManager.isMuted(ip, uuid)) {
				long ipmute = BukkitSQLManager.getMuteTime(ip);
				long uuidmute = BukkitSQLManager.getMuteTime(uuid);
				long millis = System.currentTimeMillis();
				
				if((millis >= ipmute && millis >= uuidmute) && ipmute > -1 && uuidmute > -1) {
					BukkitSQLManager.unmute(ip);
					BukkitSQLManager.unmute(uuid);
				} else {
					event.setCancelled(true);
					List<String> list = ConfigManager.getLanguageConfig().getStringList("mute.screen");
					String reason = BukkitSQLManager.getMuteReason(uuid);
					String muter = BukkitSQLManager.getMuter(uuid);
					
					p.sendMessage(getString(list, reason, uuidmute > ipmute ? uuidmute : ipmute, muter));
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
