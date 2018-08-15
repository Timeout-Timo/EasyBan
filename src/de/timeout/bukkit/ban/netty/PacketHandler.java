package de.timeout.bukkit.ban.netty;

import de.timeout.bukkit.netty.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

//Default Packet Handler
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		
	}

}
