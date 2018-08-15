package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.netty.packets.Packet;
import io.netty.buffer.ByteBuf;

public class PacketPlayOutMute implements Packet {
	
	public enum MuteInfoAction {
		
		MUTE("Mute", 0),
		CUSTOM_MUTE("CustomMute", 1),
		PERMA_MUTE("PermaMute", 2);
		
		private String name;
		private int id;
		
		private MuteInfoAction(String name, int id) {
			this.id = id;
			this.name = name;
		}
		
		public int getID() {
			return id;
		}
		
		public String getName() {
			 return name;
		}
		
		public static MuteInfoAction getInfoActionByID(int id) {
			for(MuteInfoAction e : values())if(e.getID() == id)return e;
			throw new NullPointerException("Enum could not be found");
		}
		
		public static MuteInfoAction getInfoActionByName(String name) {
			for(MuteInfoAction e : values())if(e.getName().equalsIgnoreCase(name))return e;
			throw new NullPointerException("Enum could not be found");
		}
	}
	
	private MuteInfoAction action;
	private long days;
	private long hours;
	private long minutes;
	private String muted;
	private String muter;
	private String reason;
	
	public PacketPlayOutMute() {}
	
	public PacketPlayOutMute(String muted, String muter, long days, long hours, long minutes) {
		this.action = MuteInfoAction.CUSTOM_MUTE;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.muted = muted;
		this.muter = muter;
	}
	
	public PacketPlayOutMute(String muted, String reason, String muter) {
		this.action = MuteInfoAction.MUTE;
		this.muted = muted;
		this.muter = muter;
		this.reason = reason;
	}
	
	public PacketPlayOutMute(String muted, String muter) {
		this.action = MuteInfoAction.PERMA_MUTE;
		this.muted = muted;
		this.muter = muter;
	}

	public MuteInfoAction getAction() {
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

	public String getMuted() {
		return muted;
	}

	public String getMuter() {
		return muter;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF(action.getName());
		out.writeLong(days);
		out.writeLong(hours);
		out.writeLong(minutes);
		out.writeUTF(muted);
		out.writeUTF(muter);
		out.writeUTF(reason);
		
		byteBuf.writeBytes(out.toByteArray());
	}
}
