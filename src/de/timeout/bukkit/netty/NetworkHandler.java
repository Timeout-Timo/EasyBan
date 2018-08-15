package de.timeout.bukkit.netty;

import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.bukkit.netty.packets.PacketPlayInPing;
import de.timeout.bukkit.netty.packets.PacketPlayInTime;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetworkHandler extends SimpleChannelInboundHandler<Packet> {
	
	private static Channel channel;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NetworkHandler.channel = ctx.channel();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if(packet instanceof PacketPlayInTime) 
			System.out.println(((PacketPlayInTime) packet).getTime());
		else if(packet instanceof PacketPlayInPing)
			System.out.println(System.currentTimeMillis() - ((PacketPlayInPing) packet).getTime());
	}
	
	public static void sendPacket(Packet packet) {
		channel.writeAndFlush(packet);
	}

}
