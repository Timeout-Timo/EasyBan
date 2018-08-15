package de.timeout.bungee.netty.handlers;

import java.util.List;

import de.timeout.bungee.netty.Server;
import de.timeout.bungee.netty.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
		int id = byteBuf.readInt();
		Class <? extends Packet> c = Server.instance.getInPackets().get(id);
		try {
			Packet packet = c.newInstance();
			packet.read(byteBuf);
		} catch(NullPointerException e) {
			throw new NullPointerException("Couldn't find Packet-ID: " + c.getClass().getSimpleName());
		}
	}

}
