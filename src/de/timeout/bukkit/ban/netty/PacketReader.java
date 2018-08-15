package de.timeout.bukkit.ban.netty;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.netty.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketReader {
	
	private BanGUI main = BanGUI.plugin;
	
	public static abstract class PacketReaderHandler extends SimpleChannelInboundHandler<Packet> {
		
		protected Channel channel;
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			channel = ctx.channel();
		}
		
		public Channel getChannel() {
			return channel;
		}
		
		public void disconnect() {
			channel.pipeline().remove(this);
		}
		
	}

	private PacketReaderHandler handler;
	protected Packet packet;
	
	public PacketReader(Packet packet, PacketReaderHandler handler) {
		this.packet = packet;
		this.handler = handler;
		
		main.getNettyClient().getMainChannel().pipeline().addFirst(handler);
		main.getNettyClient().sendPacket(packet);
	}

	public PacketReaderHandler getHandler() {
		return handler;
	}

	public Packet getPacket() {
		return packet;
	}
}
