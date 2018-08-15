package de.timeout.bungee.netty.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayOutTime implements Packet {

	private long time;
	
	public PacketPlayOutTime() {}
	
	public PacketPlayOutTime(long time) {
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
