package de.timeout.bungee.ban.api;

import java.util.UUID;

import de.timeout.utils.Reason;
import net.md_5.bungee.api.plugin.Event;

public abstract class PunishEvent extends Event {

	protected UUID punishedUUID;
	protected String punishedIP;
	protected String punisher;
	
	protected Reason reason;
	protected String display;
	protected long duration;
	
	public PunishEvent(String ip, UUID uuid, String punisher, Reason reason, String display, long duration) {
		this.punishedUUID = uuid;
		this.punishedIP = ip;
		this.punisher = punisher;
		this.reason = reason;
		this.display = display;
		this.duration = duration;
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

	public Reason getReason() {
		return reason;
	}

	public String getDisplay() {
		return display;
	}

	public long getDuration() {
		return duration;
	}
}
