package de.timeout.bukkit.netty.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

public interface Packet {

	public void read(ByteBuf byteBuf) throws IOException;
	
	public void write(ByteBuf byteBuf) throws IOException;
}
