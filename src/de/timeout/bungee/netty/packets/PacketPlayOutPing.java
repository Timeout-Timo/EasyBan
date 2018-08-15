package de.timeout.bungee.netty.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public class PacketPlayOutPing implements Packet {

	private long time;
	
	public PacketPlayOutPing() {}
	
	public PacketPlayOutPing(long time) {
		this.time = time;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		this.time = byteBuf.readLong();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		byteBuf.writeLong(this.time);
	}

	public long getTime() {
		return time;
	}
}
