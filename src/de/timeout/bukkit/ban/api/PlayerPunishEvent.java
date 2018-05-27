package de.timeout.bukkit.ban.api;

import java.util.UUID;

import org.bukkit.event.Event;

import de.timeout.bukkit.ban.utils.BukkitReason;

public abstract class PlayerPunishEvent extends Event {

	protected UUID punishedUUID;
	protected String punishedIP;
	protected String punishedName;
	protected String punisher;
	
	protected BukkitReason reason;
	protected long duration;
	protected String display;
	
	public PlayerPunishEvent(String ip, UUID uuid, String name, String punisher, BukkitReason reason, String display, long duration) {
		this.punishedUUID = uuid;
		this.punishedIP = ip;
		this.punisher = punisher;
		this.reason = reason;
		this.display = display;
		this.duration = duration;
	}
	
	public String getPunishedName() {
		return punishedName;
	}
	
	public UUID getPunishedUUID() {
		return punishedUUID;
	}

	public String getPunishedIP() {
		return punishedIP;
	}

	public String getPunisher() {
		return punisher;
	}

	public BukkitReason getReason() {
		return reason;
	}

	public long getDuration() {
		return duration;
	}

	public String getDisplay() {
		return display;
	}
}
