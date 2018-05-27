package de.timeout.bukkit.ban.api;

import java.util.UUID;

import org.bukkit.event.HandlerList;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.Reason.Stage;

public class PlayerMuteEvent extends PlayerPunishEvent {
	
	private static HandlerList handlers;
	
	private static BanGUI main = BanGUI.plugin; 
	private static String perma = main.getLanguage("mute.perma");
	private static String custom = main.getLanguage("mute.custom");
	
	public PlayerMuteEvent(String ip, UUID uuid, String name, String banner) {
		super(ip, uuid, name, banner, null, perma, -1L);
	}
	
	public PlayerMuteEvent(String ip, UUID uuid, String name, String banner, BukkitReason reason) {
		super(ip, uuid, name, banner, reason, reason.getDisplay(), 0L);
		long violences = BukkitSQLManager.getViolencePoints(uuid);
		Stage stage = reason.getValidStage(violences);
		if(stage == Stage.THIRD)duration = reason.getThirdtime();
		else if(stage == Stage.SECOND)duration = reason.getSecondtime();
		else duration = reason.getFirsttime();
	}
	
	public PlayerMuteEvent(String ip, UUID uuid, String name, String banner, long duration) {
		super(ip, uuid, name, banner, null, custom, duration);
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
