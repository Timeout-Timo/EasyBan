package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.utils.Reason.ReasonType;
import io.netty.buffer.ByteBuf;

public class PacketInfoOutReason implements Packet {
	
	private String name;
	private ReasonType type;
	
	public PacketInfoOutReason() {}
	
	public PacketInfoOutReason(String name, ReasonType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF(name);
		out.writeUTF(type.getName());
		
		byteBuf.writeBytes(out.toByteArray());
	}

	public String getName() {
		return name;
	}

	public ReasonType getType() {
		return type;
	}
}
