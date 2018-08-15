package de.timeout.bungee.netty;

import java.util.Arrays;
import java.util.List;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.bungee.netty.packets.Packet;
import de.timeout.bungee.netty.packets.PacketPlayInExit;
import de.timeout.bungee.netty.packets.PacketPlayInPing;
import de.timeout.bungee.netty.packets.PacketPlayInTime;
import de.timeout.bungee.netty.packets.PacketPlayOutPing;
import de.timeout.bungee.netty.packets.PacketPlayOutTime;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

	private BanSystem main = BanSystem.plugin;
	private int port = main.getConfig().getInt("file.server.port");
	
	public static final boolean EPOLL = Epoll.isAvailable();
	
	public List<Class<? extends Packet>> outPackets = Arrays.asList(PacketPlayOutPing.class, PacketPlayOutTime.class);
	public List<Class <? extends Packet>> inPackets = Arrays.asList(PacketPlayInPing.class, PacketPlayInTime.class, PacketPlayInExit.class);
	
	public static Server instance;
	
	private Channel channel;

	public Server(ChannelHandler... handlers) throws Exception {
		instance = this;
		EventLoopGroup group = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		try {
			channel = new ServerBootstrap().group(group).channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) throws Exception {
					for(ChannelHandler handler : handlers)channel.pipeline().addLast(handler);
				}
				
			}).bind(port).sync().channel();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Server();
	}

	public List<Class<? extends Packet>> getOutPackets() {
		return outPackets;
	}

	public List<Class<? extends Packet>> getInPackets() {
		return inPackets;
	}
	
	public Channel getChannel() {
		return channel;
	}
}
