package de.timeout.bungee.ban.manager;

import java.util.List;
import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.ban.ConfigManager;
import de.timeout.bungee.ban.api.BanEvent;
import de.timeout.bungee.ban.api.MuteEvent;
import de.timeout.bungee.ban.filemanager.DecidationManager;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.DateConverter;
import de.timeout.utils.Reason;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	
	private static BanSystem main = BanSystem.plugin;
	
	//BanOnline
	public static void ban(ProxiedPlayer banned, String banner, Reason reason) {
		long violences = DecidationManager.getPlayerViolences(banned.getUniqueId());
		DecidationManager.updateHistory(banned.getUniqueId(), violences + reason.getViolencePoints());
		
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? banned.getAddress().getAddress().getHostAddress() : null, banned.getUniqueId(), banned.getName(), banner, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		banned.disconnect(new TextComponent(getString(list, event.getReason().getDisplayName(), event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
		DecidationManager.addBan(event.getPunishedUUID(), main.isIPBanEnabled() ? event.getPunishedIP() : null,
				event.getPunishedName(), event.getDuration(), event.getReason().getDisplay(), banner);
	}
	
	//BanOffline
	public static void banOffline(String ip, UUID uuid, String name, String banner, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		DecidationManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? ip : null, uuid, name, banner, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getReason().getName(), event.getPunisher());
	}
	
	//Permaban
	public static void permaban(ProxiedPlayer banned, String banner) {
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? banned.getAddress().getAddress().getHostAddress() : null, banned.getUniqueId(), banned.getName(), banner);
		main.getProxy().getPluginManager().callEvent(event);
		
		banned.disconnect(new TextComponent(getString(list, "§4PERMANENT", -1, event.getPunisher())));
		DecidationManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), event.getPunisher());
	}
	
	//PermabanOffline
	public static void permabanOffline(String ip, UUID uuid, String name, String banner) {
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? ip : null, uuid, name, banner);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), event.getPunisher());
	}
	
	//Customban
	public static void customban(ProxiedPlayer banned, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? banned.getAddress().getAddress().getHostAddress() : null, banned.getUniqueId(), banned.getName(), banner, bantime);
		main.getProxy().getPluginManager().callEvent(event);
		
		banned.disconnect(new TextComponent(getString(list, "§aCustomBan", event.getDuration() + System.currentTimeMillis(), event.getPunisher())));
		DecidationManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), banner);
	}
	
	//CustombanOffline
	public static void custombanOffline(UUID uuid, String ip, String name, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		BanEvent event = new BanEvent(main.isIPBanEnabled() ? ip : null, uuid, name, banner, bantime);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), event.getPunisher());
	}
	
	//Mute
	public static void mute(ProxiedPlayer muted, String muter, Reason reason) {
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		muteOffline(muted.getUniqueId(), muted.getAddress().getAddress().getHostAddress(), muted.getName(), muter, reason);
		muted.sendMessage(new TextComponent(getString(list, reason.getDisplay(), DecidationManager.getUnmuteTime(muted.getUniqueId()), muter)));
	}
	
	//MuteOffline
	public static void muteOffline(UUID uuid, String ip, String name, String muter, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		DecidationManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		MuteEvent event = new MuteEvent(main.isIPBanEnabled() ? ip : null, uuid, name, muter, reason);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getReason().getName());
	}
	
	//Permamute
	public static void permamute(ProxiedPlayer muted, String muter) {
		permamuteOffline(muted.getUniqueId(), muted.getAddress().getAddress().getHostAddress(), muted.getName(), muter);
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		
		muted.sendMessage(new TextComponent(getString(list, DecidationManager.getPunishedMuteDisplay(muted.getUniqueId()), DecidationManager.getUnmuteTime(muted.getUniqueId()), muter)));
	}
	
	//PermamuteOffline
	public static void permamuteOffline(UUID uuid, String ip, String name, String muter) {
		MuteEvent event = new MuteEvent(main.isIPBanEnabled() ? ip : null, uuid, name, muter);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), event.getPunisher());
	}
	
	//Custommute
	public static void custommute(ProxiedPlayer p, long days, long hours, long minutes, String muter) {
		custommuteOffline(p.getUniqueId(), p.getAddress().getAddress().getHostAddress(), p.getName(), days, hours, minutes, muter);
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		p.sendMessage(new TextComponent(getString(list, DecidationManager.getPunishedMuteDisplay(p.getUniqueId()), DecidationManager.getUnmuteTime(p.getUniqueId()), muter)));
	}
	
	//CustommuteOffline
	public static void custommuteOffline(UUID uuid, String ip, String name, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		
		MuteEvent event = new MuteEvent(main.isIPBanEnabled() ? ip : null, uuid, name, muter, mutetime);
		main.getProxy().getPluginManager().callEvent(event);
		
		DecidationManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getDisplay());
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
