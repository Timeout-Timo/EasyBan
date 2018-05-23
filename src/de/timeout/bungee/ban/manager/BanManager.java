package de.timeout.bungee.ban.manager;

import java.util.List;
import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.ban.ConfigManager;
import de.timeout.bungee.ban.api.BanEvent;
import de.timeout.bungee.ban.api.MuteEvent;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.DateConverter;
import de.timeout.utils.Reason;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	
	private static BanSystem main = BanSystem.plugin;
	
	//BanOnline
	public static void ban(ProxiedPlayer banned, String banner, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(banned);
		BungeeSQLManager.updateHistory(banned.getUniqueId(), violences + reason.getViolencePoints());
		
		BanEvent event = new BanEvent(banned.getAddress().getAddress().getHostAddress(), banned.getUniqueId(), banner, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		banned.disconnect(new TextComponent(getString(list, event.getReason().getDisplayName(), event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
		BungeeSQLManager.addBan(banned, event.getDuration(), banner, reason.getName());
	}
	
	//BanOffline
	public static void banOffline(String ip, UUID uuid, String banner, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		BungeeSQLManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		BanEvent event = new BanEvent(ip, uuid, banner, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addBan(uuid, ip, event.getDuration(), event.getPunisher(), event.getReason().getName());
	}
	
	//Permaban
	public static void permaban(ProxiedPlayer banned, String banner) {
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		
		BanEvent event = new BanEvent(banned.getAddress().getAddress().getHostAddress(), banned.getUniqueId(), banner);
		main.getProxy().getPluginManager().callEvent(event);
		
		banned.disconnect(new TextComponent(getString(list, "§4PERMANENT", -1, event.getPunisher())));
		BungeeSQLManager.addBan(banned, -1, event.getPunisher(), event.getDisplay());
	}
	
	//PermabanOffline
	public static void permabanOffline(String ip, UUID uuid, String banner) {
		BanEvent event = new BanEvent(ip, uuid, banner);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addBan(uuid, ip, -1, event.getPunisher(), event.getDisplay());
	}
	
	//Customban
	public static void customban(ProxiedPlayer banned, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		
		BanEvent event = new BanEvent(banned.getAddress().getAddress().getHostAddress(), banned.getUniqueId(), banner, bantime);
		main.getProxy().getPluginManager().callEvent(event);
		
		banned.disconnect(new TextComponent(getString(list, "§aCustomBan", event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
		BungeeSQLManager.addBan(banned, event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//CustombanOffline
	public static void custombanOffline(UUID uuid, String ip, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		BanEvent event = new BanEvent(ip, uuid, banner, bantime);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//Mute
	public static void mute(ProxiedPlayer muted, String muter, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(muted);
		BungeeSQLManager.updateHistory(muted.getUniqueId(), violences + reason.getViolencePoints());
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		
		MuteEvent event = new MuteEvent(muted.getAddress().getAddress().getHostAddress(), muted.getUniqueId(), muter, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		muted.sendMessage(new TextComponent(getString(list, event.getReason().getDisplayName(), event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
		BungeeSQLManager.addMute(muted, event.getDuration(), event.getPunisher(), event.getReason().getName());
	}
	
	//MuteOffline
	public static void muteOffline(UUID uuid, String ip, String muter, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		BungeeSQLManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		MuteEvent event = new MuteEvent(ip, uuid, muter, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getDuration(), event.getPunisher(), event.getReason().getName());
	}
	
	//Permamute
	public static void permamute(ProxiedPlayer player, String muter) {
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		MuteEvent event = new MuteEvent(player.getAddress().getAddress().getHostAddress(), player.getUniqueId(), muter);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addMute(player, event.getDuration(), event.getPunisher(), event.getDisplay());
		player.sendMessage(new TextComponent(getString(list, event.getDisplay(), event.getDuration(), event.getPunisher())));
	}
	
	//PermamuteOffline
	public static void permamuteOffline(UUID uuid, String ip, String muter) {
		MuteEvent event = new MuteEvent(ip, uuid, muter);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//Custommute
	public static void custommute(ProxiedPlayer p, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		
		MuteEvent event = new MuteEvent(p.getAddress().getAddress().getHostAddress(), p.getUniqueId(), muter, mutetime);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addMute(p, event.getDuration(), event.getPunisher(), event.getDisplay());
		p.sendMessage(new TextComponent(getString(list, event.getDisplay(), event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
	}
	
	//CustommuteOffline
	public static void custommuteOffline(UUID uuid, String ip, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		
		MuteEvent event = new MuteEvent(ip, uuid, muter, mutetime);
		main.getProxy().getPluginManager().callEvent(event);
		
		BungeeSQLManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	private static String getString(List<String> list, String display, long time, String sender) {
		String s = "";
		for(int i = 0; i < list.size(); i++)s = s + list.get(i).replaceAll("&", "§") + "\n";
		
		s = s.replace("[reason]", display);
		s = s.replace("[date]", DateConverter.getDate(time));
		s = s.replace("[banner]", sender);
		s = s.replace("[muter]", sender);
		return s;
	}
}
