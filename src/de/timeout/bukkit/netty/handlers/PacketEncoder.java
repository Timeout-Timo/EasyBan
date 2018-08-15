package de.timeout.bukkit.netty.handlers;

import de.timeout.bukkit.netty.Client;
import de.timeout.bukkit.netty.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		int id = Client.instance.getOutPackets().indexOf(packet.getClass());
		if(id >= 0) {
			out.writeInt(id);
			packet.write(out);
		} else throw new NullPointerException("Couldn't find Packet-ID: " + packet.getClass().getSimpleName());
	}

}
