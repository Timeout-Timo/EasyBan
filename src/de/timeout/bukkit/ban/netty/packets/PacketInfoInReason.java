package de.timeout.bukkit.ban.netty.packets;

import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.ban.utils.ItemStackAPI;
import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.utils.Reason.ReasonType;
import io.netty.buffer.ByteBuf;

public class PacketInfoInReason implements Packet {
	
	private String name, display;
	private long firsttime, secondtime, thirdtime, firstline, secondline, points;
	private ReasonType type;
	private ItemStack item;

	@Override
	public void read(ByteBuf byteBuf) throws IOException {
		ByteArrayDataInput in = ByteStreams.newDataInput(byteBuf.array());
		
		name = in.readUTF();
		type = ReasonType.getTypeByName(in.readUTF());
		firsttime = in.readLong();
		secondtime = in.readLong();
		thirdtime = in.readLong();
		firstline = in.readLong();
		secondline = in.readLong();
		display = in.readUTF();
		points = in.readLong();
		item = ItemStackAPI.decodeItemStack(in.readUTF());
	}

	@Override
	public void write(ByteBuf byteBuf) throws IOException {}

}
