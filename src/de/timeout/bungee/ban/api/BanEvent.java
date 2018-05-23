package de.timeout.bungee.ban.api;

import java.util.UUID;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.Stage;

public class BanEvent extends PunishEvent {
	
	private static BanSystem main = BanSystem.plugin;
	private static String perma = main.getLanguage("ban.perma");
	private static String custom = main.getLanguage("ban.custom");
	

	public BanEvent(String ip, UUID uuid, String banner) {
		super(ip, uuid, banner, null, perma, -1L);
	}
	
	public BanEvent(String ip, UUID uuid, String banner, Reason reason) {
		super(ip, uuid, banner, reason, reason.getName(), 0L);
		long violences = BungeeSQLManager.getPlayerViolences(uuid);
		Stage stage = reason.getValidStage(violences);
		if(stage == Stage.THIRD)duration = reason.getThirdtime();
		else if(stage == Stage.SECOND)duration = reason.getSecondtime();
		else duration = reason.getFirsttime();
		
	}
	
	public BanEvent(String ip, UUID uuid, String banner, long duration) {
		super(ip, uuid, banner, null, custom, duration);
	}
}
