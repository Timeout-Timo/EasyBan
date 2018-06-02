package de.timeout.bukkit.ban.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import de.timeout.bukkit.ban.BanGUI;

public class BungeeMessager {
	
	private static BanGUI main = BanGUI.plugin;

	public static abstract class BungeeMessageHandler implements PluginMessageListener {
		
		public void end() {
			Bukkit.getMessenger().unregisterIncomingPluginChannel(main, channel, this);
		}
		
		public void cacheResult(Object... results) {
			result = results;
		}
	}
	
	private BungeeMessageHandler handler;
	private static String channel;
	private static Object[] result;
	
	public BungeeMessager(String channel, byte[] send, PluginMessageRecipient recipient, BungeeMessageHandler handler) {
		BungeeMessager.channel = channel;
		this.handler = handler;
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(main, channel, this.handler);
		System.out.println("registriert");
		
		recipient.sendPluginMessage(main, channel, send);
		System.out.println("Gesendet");
	}

	public BungeeMessageHandler getHandler() {
		return handler;
	}

	public void setHandler(BungeeMessageHandler handler) {
		this.handler = handler;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		BungeeMessager.channel = channel;
	}

	public Object[] getResult() {
		return result;
	}
}

