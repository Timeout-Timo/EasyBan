package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;
import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.netty.packets.Packet;
import io.netty.buffer.ByteBuf;

public class PacketInfoInBanned implements Packet {

	private UUID uuid;
	private boolean banned;
	
	public PacketInfoInBanned() {}

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		ByteArrayDataInput in = ByteStreams.newDataInput(new byte[byteBuf.readableBytes()]);
		
		uuid = UUID.fromString(in.readUTF());
		banned = in.readBoolean();
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {	}

	public UUID getUniqueID() {
		return uuid;
	}
	
	public boolean isBanned() {
		return banned;
	}
}
