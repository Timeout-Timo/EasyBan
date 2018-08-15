package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.netty.packets.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInfoOutBanned implements Packet {
	
	private UUID uuid;
	
	public PacketInfoOutBanned() {}
	
	public PacketInfoOutBanned(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF(uuid.toString());
		
		byteBuf.writeBytes(out.toByteArray());
	}

	public UUID getUniqueID() {
		return uuid;
	}
}
