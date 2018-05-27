package de.timeout.bukkit.ban.api;

import java.util.UUID;

import org.bukkit.event.HandlerList;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.Reason.Stage;

public class PlayerBanEvent extends PlayerPunishEvent {
	
	private static BanGUI main = BanGUI.plugin;
	private static String perma = main.getLanguage("ban.perma");
	private static String custom = main.getLanguage("ban.custom");
	
	private static HandlerList handlers = new HandlerList();

	public PlayerBanEvent(String ip, UUID uuid, String name, String banner) {
		super(ip, uuid, name, banner, null, perma, -1L);
	}
	
	public PlayerBanEvent(String ip, UUID uuid, String name, String banner, BukkitReason reason) {
		super(ip, uuid, name, banner, reason, reason.getDisplay(), 0L);
		long violences = BukkitSQLManager.getViolencePoints(uuid);
		Stage stage = reason.getValidStage(violences);
		if(stage == Stage.THIRD)duration = reason.getThirdtime();
		else if(stage == Stage.SECOND)duration = reason.getSecondtime();
		else duration = reason.getFirsttime();
	}
	
	public PlayerBanEvent(String ip, UUID uuid, String name, String banner, long duration) {
		super(ip, uuid, name, banner, null, custom, duration);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
