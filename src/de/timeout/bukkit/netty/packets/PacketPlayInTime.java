package de.timeout.bukkit.netty.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayInTime implements Packet {

	private long time;
	
	public PacketPlayInTime() {}
	
	public PacketPlayInTime(long time) {
		this.time = time;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		time = byteBuf.readLong();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeLong(time);
	}

	public long getTime() {
		return time;
	}

}
