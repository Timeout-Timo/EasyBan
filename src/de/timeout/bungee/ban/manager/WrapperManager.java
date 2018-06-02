package de.timeout.bungee.ban.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonParser;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.ban.filemanager.DecidationManager;
import de.timeout.utils.Reason;
import de.timeout.utils.Reason.ReasonType;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WrapperManager implements Listener {
	
	private BanSystem main = BanSystem.plugin;
	
	private String prefix = main.getLanguage("prefix");
	private String bansuccess = main.getLanguage("ban.success");
	private String mutesuccess = main.getLanguage("mute.success");
	
	@EventHandler
	public void onMessageReceive(PluginMessageEvent event) {
		if(event.getTag().equalsIgnoreCase("BanSystem")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = in.readUTF();
				Connection con = event.getReceiver();
				if(channel.equalsIgnoreCase("Ban")) {
					String name = in.readUTF();
					String reason = in.readUTF();
					
					String banner = in.readUTF();
					Reason r = new Reason(reason, ReasonType.BAN);
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.ban(t, banner, r);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					} else {
						BanManager.banOffline(null, getUUIDFromMojangServer(name), name, banner, r);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("PermaBan")) {
					String name = in.readUTF();
					String banner = in.readUTF();
					
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.permaban(t, banner);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					} else {
						BanManager.permabanOffline(null, getUUIDFromMojangServer(name), name, banner);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("CustomBan")) {
					String name = in.readUTF();
					long days = in.readLong();
					long hours = in.readLong();
					long minutes = in.readLong();
					String banner = in.readUTF();
					
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.customban(t, days, hours, minutes, banner);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					} else {
						BanManager.custombanOffline(getUUIDFromMojangServer(name), null, name, days, hours, minutes, banner);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + bansuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("Mute")) {
					String name = in.readUTF();
					String reason = in.readUTF();
					String muter = in.readUTF();
					
					Reason r = new Reason(reason, ReasonType.MUTE);
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.mute(t, muter, r);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					} else {
						BanManager.muteOffline(getUUIDFromMojangServer(name), null, name, muter, r);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("PermaMute")) {
					String name = in.readUTF();
					String muter = in.readUTF();
					
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.permamute(t, muter);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					} else {
						BanManager.permamuteOffline(getUUIDFromMojangServer(name), null, name, muter);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("CustomMute")) {
					String name = in.readUTF();
					long days = in.readLong();
					long hours = in.readLong();
					long minutes = in.readLong();
					String muter = in.readUTF();
					
					if(isOnline(name)) {
						ProxiedPlayer t = main.getProxy().getPlayer(name);
						
						BanManager.custommute(t, days, hours, minutes, muter);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					} else {
						BanManager.custommuteOffline(getUUIDFromMojangServer(name), null, name, days, hours, minutes, muter);
						getProxiedPlayer(con).sendMessage(new TextComponent(prefix + mutesuccess.replace("[name]", name)));
					}
				} else if(channel.equalsIgnoreCase("isBanned")) {
					System.out.println("Von Bukkit Empfangen");
					UUID uuid = UUID.fromString(in.readUTF());
					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(stream);
					
					out.writeUTF("isBanned");
					out.writeBoolean(DecidationManager.isBanned(uuid));
					
					ServerInfo server = BungeeCord.getInstance().getPlayer(con.toString()).getServer().getInfo();
					server.sendData("BanSystem", stream.toByteArray());
					System.out.println("An Bukkit gesendet");
				}
			} catch(IOException e) {}
		}
	}
	
	private UUID getUUIDFromMojangServer(String name) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + name.toLowerCase();
		
		try {
			InputStreamReader reader = new InputStreamReader(new URL(url).openStream());
			String trimmedUUID = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			
			return fromTrimmed(trimmedUUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
	    if(trimmedUUID == null) throw new IllegalArgumentException();
	    StringBuilder builder = new StringBuilder(trimmedUUID.trim());
	    try {
	        builder.insert(20, "-");
	        builder.insert(16, "-");
	        builder.insert(12, "-");
	        builder.insert(8, "-");
	    } catch (StringIndexOutOfBoundsException e){
	        throw new IllegalArgumentException();
	    }
	 
	    return UUID.fromString(builder.toString());
	}

	private boolean isOnline(String name) {
		for(ProxiedPlayer p : main.getProxy().getPlayers()) {
			if(p.getName().equalsIgnoreCase(name))return true;
		}
		return false;
	}
	
	private ProxiedPlayer getProxiedPlayer(Connection con) {
		try {
			ProxiedPlayer p = (ProxiedPlayer)con;
			return p;
		} catch(ClassCastException e) {}
		return null;
	}
}