package de.timeout.bungee.netty;

import de.timeout.bungee.netty.packets.Packet;
import de.timeout.bungee.netty.packets.PacketPlayInExit;
import de.timeout.bungee.netty.packets.PacketPlayInPing;
import de.timeout.bungee.netty.packets.PacketPlayInTime;
import de.timeout.bungee.netty.packets.PacketPlayOutPing;
import de.timeout.bungee.netty.packets.PacketPlayOutTime;
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
		if(packet instanceof PacketPlayInPing) 
			NetworkHandler.channel.writeAndFlush(new PacketPlayOutPing(((PacketPlayInPing)packet).getTime()), channel.voidPromise());
		else if(packet instanceof PacketPlayInExit) 
			NetworkHandler.channel.close().syncUninterruptibly();
		else if(packet instanceof PacketPlayInTime) 
			NetworkHandler.channel.writeAndFlush(new PacketPlayOutTime());
	}

	public static void sendPacket(Packet packet) {
		channel.writeAndFlush(packet, channel.voidPromise());
	}
}
