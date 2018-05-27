package de.timeout.bukkit.ban.manager;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.api.PlayerBanEvent;
import de.timeout.bukkit.ban.api.PlayerMuteEvent;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.DateConverter;

public class BanManager {
	
	private static BanGUI main = BanGUI.plugin;

	//Ban
	public static void ban(Player banned, BukkitReason reason, String banner) {
		banOffline(banned.getAddress().getAddress().getHostAddress(), banned.getUniqueId(), banned.getName(), banner, reason);
		List<String> list = main.getConfig().getStringList("ban.screen");
		banned.kickPlayer(getString(list, reason.getDisplay(), BukkitSQLManager.getBanTime(banned.getUniqueId()), banner));
	}
	
	public static void banOffline(String ip, UUID uuid, String name, String banner, BukkitReason reason) {
		long violences = BukkitSQLManager.getViolencePoints(uuid);
		BukkitSQLManager.updateHistory(uuid, violences);
		
		PlayerBanEvent event = new PlayerBanEvent(ip, uuid, name, banner, reason);
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getDisplay(), event.getReason().getName());
	}
	
	//Permaban
	public static void permaban(Player banned, String banner) {
		permabanOffline(banned.getAddress().getAddress().getHostAddress(), banned.getName(), banned.getUniqueId(), banner);
		List<String> list = main.getConfig().getStringList("ban.screen");
		banned.kickPlayer(getString(list, "§4PERMANENT", BukkitSQLManager.getBanTime(banned.getUniqueId()), banner));
	}
	
	public static void permabanOffline(String ip, String name, UUID uuid, String banner) {
		PlayerBanEvent event = new PlayerBanEvent(ip, uuid, name, banner);
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//Customban
	public static void customban(Player banned, String banner, long days, long hours, long minutes) {
		customban(banned.getAddress().getAddress().getHostAddress(), banned.getUniqueId(), banned.getName(), banner, days, hours, minutes);
		List<String> list = main.getConfig().getStringList("ban.screen");
		banned.kickPlayer(getString(list, "§aCustomBan", BukkitSQLManager.getBanTime(banned.getUniqueId()), banner));
	}
	
	public static void customban(String ip, UUID uuid, String name, String banner, long days, long hours, long minutes) {
		PlayerBanEvent event = new PlayerBanEvent(ip, uuid, name, banner, DateConverter.getTimeMillis(days, hours, minutes));
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//Mute
	public static void mute(Player muted, String muter, BukkitReason reason) {
		mute(muted.getAddress().getAddress().getHostAddress(), muted.getUniqueId(), muted.getName(), muter, reason);
		List<String> list = main.getConfig().getStringList("mute.screen");
		muted.sendMessage(getString(list, reason.getDisplay(), BukkitSQLManager.getBanTime(muted.getUniqueId()), muter));
	}
	
	public static void mute(String ip, UUID uuid, String name, String banner, BukkitReason reason) {
		PlayerMuteEvent event = new PlayerMuteEvent(ip, uuid, name, banner, reason);
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addBan(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getDisplay());
	}
	
	//Permamute
	public static void permamute(Player muted, String muter) {
		permamute(muted.getUniqueId(), muted.getAddress().getAddress().getHostAddress(), muted.getName(), muter);
		List<String> list = main.getConfig().getStringList("mute.screen");
		muted.sendMessage(getString(list, "§4PERMANENT", -1, muter));
	}
	
	public static void permamute(UUID uuid, String ip, String name, String muter) {
		PlayerMuteEvent event = new PlayerMuteEvent(ip, uuid, name, muter);
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), -1, event.getPunisher(), event.getDisplay());
	}
	
	//Customban
	public static void custommute(Player muted, long days, long hours, long minutes, String muter) {
		custommute(muted.getUniqueId(), muted.getAddress().getAddress().getHostAddress(), muted.getName(), days, hours, minutes, muter);
		List<String> list = main.getConfig().getStringList("mute.screen");
		muted.sendMessage(getString(list, "§aCustomBan", BukkitSQLManager.getBanTime(muted.getUniqueId()), muter));
	}
	
	public static void custommute(UUID uuid, String ip, String name, long days, long hours, long minutes, String muter) {
		long mutetime = DateConverter.getTimeMillis(days, hours, minutes);
		PlayerMuteEvent event = new PlayerMuteEvent(ip, uuid, name, muter, mutetime);
		main.getServer().getPluginManager().callEvent(event);
		
		BukkitSQLManager.addMute(event.getPunishedUUID(), event.getPunishedIP(), event.getPunishedName(), event.getDuration(), event.getPunisher(), event.getDisplay());
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
