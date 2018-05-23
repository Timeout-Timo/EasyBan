package de.timeout.bukkit.ban.api;

import de.timeout.bukkit.ban.utils.BukkitReason;

public class DelReasonEvent extends ReasonEvent {

	public DelReasonEvent(BukkitReason reason) {
		super(reason);
	}

	@Override
	public void setReason(BukkitReason arg0) {
		reason = arg0;
	}

	@Override
	public BukkitReason getReason() {
		return reason;
	}

}
