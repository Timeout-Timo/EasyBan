package de.timeout.bukkit.ban.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.timeout.bukkit.ban.utils.BukkitReason;

public abstract class ReasonEvent extends Event implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private boolean cancel;
	
	protected BukkitReason reason;
	
	public ReasonEvent(BukkitReason reason) {
		this.reason = reason;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public abstract void setReason(BukkitReason arg0);
	
	public abstract BukkitReason getReason();

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancel = arg0;
	}
}
