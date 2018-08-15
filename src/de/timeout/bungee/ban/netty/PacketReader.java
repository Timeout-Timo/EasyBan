package de.timeout.bungee.ban.netty;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.netty.packets.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketReader {
	
	private BanSystem main = BanSystem.plugin;
	
	public abstract class PacketInboundHandler extends SimpleChannelInboundHandler<Packet> {
		
		protected Channel channel;
		
		protected Channel getChannel() {
			return channel;
		}
		
		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			channel = ctx.channel();
		}
		
		public void disconnect() {
			channel.pipeline().remove(this);
		}
	}
	
	protected Packet packet;
		
	public PacketReader(Packet packet, PacketInboundHandler listener) {
		this.packet = packet;
		
		main.getNettyServer().getChannel().pipeline().addFirst(listener);
		main.getNettyServer().getChannel().writeAndFlush(packet);
	}
}
