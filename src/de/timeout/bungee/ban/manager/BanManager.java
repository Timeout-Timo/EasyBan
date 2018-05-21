package de.timeout.bungee.ban.manager;

import java.util.List;
import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.ban.ConfigManager;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.DateConverter;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.Stage;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {
	
	private static BanSystem main = BanSystem.plugin;
	
	private static String permaban = main.getLanguage("ban.perma");
	private static String customban = main.getLanguage("ban.custom");
	
	private static String permamute = main.getLanguage("mute.perma");
	private static String custommute = main.getLanguage("mute.custom");
	
	//BanOnline
	public static void ban(ProxiedPlayer banned, String banner, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(banned);
		BungeeSQLManager.updateHistory(banned.getUniqueId(), violences + reason.getViolencePoints());
		
		Stage stage = reason.getValidStage(violences);
		long bantime;
		if(stage == Stage.THIRD) {
			bantime = BungeeSQLManager.getThirdBan(reason.getName(), reason.getType());
		} else if(stage == Stage.SECOND) {
			bantime = BungeeSQLManager.getSecondBan(reason.getName(), reason.getType());
		} else bantime = BungeeSQLManager.getFirstBan(reason.getName(), reason.getType());
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		banned.disconnect(new TextComponent(getString(list, reason.getDisplayName(), bantime + System.currentTimeMillis(), banner)));
		BungeeSQLManager.addBan(banned, bantime, banner, reason.getName());
	}
	
	//BanOffline
	public static void banOffline(String ip, UUID uuid, String banner, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		BungeeSQLManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		Stage stage = reason.getValidStage(violences);
		long bantime;
		if(stage == Stage.THIRD) {
			bantime = BungeeSQLManager.getThirdBan(reason.getName(), reason.getType());
		} else if(stage == Stage.SECOND) {
			bantime = BungeeSQLManager.getSecondBan(reason.getName(), reason.getType());
		} else bantime = BungeeSQLManager.getFirstBan(reason.getName(), reason.getType());
		
		BungeeSQLManager.addBan(uuid, ip, bantime, banner, reason.getName());
	}
	
	//Permaban
	public static void permaban(ProxiedPlayer banned, String banner) {
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		banned.disconnect(new TextComponent(getString(list, "§4PERMANENT", -1, banner)));
		BungeeSQLManager.addBan(banned, -1, banner, permaban);
	}
	
	//PermabanOffline
	public static void permabanOffline(String ip, UUID uuid, String banner) {
		BungeeSQLManager.addBan(uuid, ip, -1, banner, permaban);
	}
	
	//Customban
	public static void customban(ProxiedPlayer banned, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		
		List<String> list = ConfigManager.getLanguage().getStringList("ban.screen");
		banned.disconnect(new TextComponent(getString(list, "§aCustomBan", bantime + System.currentTimeMillis(), banner)));
		
		BungeeSQLManager.addBan(banned, bantime, banner, customban);
	}
	
	//CustombanOffline
	public static void custombanOffline(UUID uuid, String ip, long days, long hours, long minutes, String banner) {
		long bantime = DateConverter.getTimeMillis(days, hours, minutes);
		BungeeSQLManager.addBan(uuid, ip, bantime, banner, customban);
	}
	
	//Mute
	public static void mute(ProxiedPlayer muted, String muter, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(muted);
		BungeeSQLManager.updateHistory(muted.getUniqueId(), violences + reason.getViolencePoints());

		Stage stage = reason.getValidStage(violences);
		long mutetime;
		if(stage == Stage.THIRD) {
			mutetime = BungeeSQLManager.getThirdBan(reason.getName(), reason.getType());
		} else if(stage == Stage.SECOND) {
			mutetime = BungeeSQLManager.getSecondBan(reason.getName(), reason.getType());
		} else mutetime = BungeeSQLManager.getFirstBan(reason.getName(), reason.getType());
		
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		muted.sendMessage(new TextComponent(getString(list, reason.getDisplayName(), mutetime + System.currentTimeMillis(), muter)));
		BungeeSQLManager.addMute(muted, mutetime, muter, reason.getName());
	}
	
	//MuteOffline
	public static void muteOffline(UUID uuid, String ip, String muter, Reason reason) {
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		BungeeSQLManager.updateHistory(uuid, violences + reason.getViolencePoints());
		
		Stage stage = reason.getValidStage(violences);
		long mutetime;
		if(stage == Stage.THIRD) {
			mutetime = BungeeSQLManager.getThirdBan(reason.getName(), reason.getType());
		} else if(stage == Stage.SECOND) {
			mutetime = BungeeSQLManager.getSecondBan(reason.getName(), reason.getType());
		} else mutetime = BungeeSQLManager.getFirstBan(reason.getName(), reason.getType());
		
		BungeeSQLManager.addMute(uuid, ip, mutetime, muter, reason.getName());
	}
	
	//Permamute
	public static void permamute(ProxiedPlayer player, String muter) {
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		BungeeSQLManager.addMute(player, -1, muter, permamute);
		player.sendMessage(new TextComponent(getString(list, permamute, -1, muter)));
	}
	
	//PermamuteOffline
	public static void permamuteOffline(UUID uuid, String ip, String muter) {
		BungeeSQLManager.addMute(uuid, ip, -1, muter, permamute);
	}
	
	//Custommute
	public static void custommute(ProxiedPlayer p, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		
		List<String> list = ConfigManager.getLanguage().getStringList("mute.screen");
		BungeeSQLManager.addMute(p, mutetime, muter, custommute);
		p.sendMessage(new TextComponent(getString(list, muter, mutetime + System.currentTimeMillis(), muter)));
	}
	
	//CustommuteOffline
	public static void custommuteOffline(UUID uuid, String ip, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		BungeeSQLManager.addMute(uuid, ip, mutetime, muter, custommute);
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
