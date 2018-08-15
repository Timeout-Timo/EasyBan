package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.netty.packets.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutBan implements Packet {
	
	public enum BanInfoAction {
		
		BAN("Ban", 0),
		CUSTOM_BAN("CustomBan", 1),
		PERMA_BAN("PermaBan", 2);
		
		private String name;
		private int id;
		
		private BanInfoAction(String name, int id) {
			this.id = id;
			this.name = name;
		}
		
		public int getID() {
			return id;
		}
		
		public String getName() {
			 return name;
		}
		
		public static BanInfoAction getInfoActionByID(int id) {
			for(BanInfoAction e : values())if(e.getID() == id)return e;
			throw new NullPointerException("Enum could not be found");
		}
		
		public static BanInfoAction getInfoActionByName(String name) {
			for(BanInfoAction e : values())if(e.getName().equalsIgnoreCase(name))return e;
			throw new NullPointerException("Enum could not be found");
		}
	}
	
	private BanInfoAction action;
	private long days = 0;
	private long hours = 0;
	private long minutes = 0;
	private String banned = "";
	private String banner = "";
	private String reason = "";
	
	public PacketPlayOutBan() {}
	
	public PacketPlayOutBan(String banned, String banner, long days, long hours, long minutes) {
		this.action = BanInfoAction.CUSTOM_BAN;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.banned = banned;
		this.banner = banner;
	}
	
	public PacketPlayOutBan(String banned, String reason, String banner) {
		this.action = BanInfoAction.BAN;
		this.banned = banned;
		this.banner = banner;
		this.reason = reason;
	}
	
	public PacketPlayOutBan(String banned, String banner) {
		this.action = BanInfoAction.PERMA_BAN;
		this.banned = banned;
		this.banner = banner;
	}

	public BanInfoAction getAction() {
		return action;
	}

	public long getDays() {
		return days;
	}

	public long getHours() {
		return hours;
	}

	public long getMinutes() {
		return minutes;
	}

	public String getBanned() {
		return banned;
	}

	public String getBanner() {
		return banner;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF(action.getName());
		out.writeLong(days);
		out.writeLong(hours);
		out.writeLong(minutes);
		out.writeUTF(banned);
		out.writeUTF(banner);
		out.writeUTF(reason);
		
		byteBuf.writeBytes(out.toByteArray());
	}
}
