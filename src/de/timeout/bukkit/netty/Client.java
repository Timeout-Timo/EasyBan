package de.timeout.bukkit.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.netty.packets.PacketPlayOutBan;
import de.timeout.bukkit.ban.netty.packets.PacketPlayOutMute;
import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.bukkit.netty.packets.PacketPlayInPing;
import de.timeout.bukkit.netty.packets.PacketPlayInTime;
import de.timeout.bukkit.netty.packets.PacketPlayOutExit;
import de.timeout.bukkit.netty.packets.PacketPlayOutPing;
import de.timeout.bukkit.netty.packets.PacketPlayOutTime;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	private BanGUI main = BanGUI.plugin;
	private String host = main.getConfig().getString("file.client.host");
	private int port = main.getConfig().getInt("file.client.port");
	
	public static final boolean EPOLL = Epoll.isAvailable();
	
	public List<Class <? extends Packet>> outPackets = Arrays.asList(PacketPlayOutPing.class, PacketPlayOutTime.class,
			PacketPlayOutExit.class, PacketPlayOutBan.class, PacketPlayOutMute.class);
	public List<Class <? extends Packet>> inPackets = Arrays.asList(PacketPlayInPing.class, PacketPlayInTime.class);
	
	public static Client instance;
	private Channel channel;

	public Client(ChannelHandler... handlers) throws Exception {
		instance = this;
		EventLoopGroup group = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		
		try {
			channel = new Bootstrap().group(group).channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
			.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) throws Exception {
					for(ChannelHandler handler : handlers) {
						channel.pipeline().addLast(handler);
					}
				}
				
			}).connect(host, port).sync().channel();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while((line = reader.readLine()) != null) {
				if(line == "") continue;
				Packet packet = null;
				if(line.startsWith("ping")) {
					packet = new PacketPlayOutPing(System.currentTimeMillis());
				} else if(line.startsWith("time")) {
					packet = new PacketPlayOutTime();
				} else if(line.startsWith("exit")) {
					channel.writeAndFlush(new PacketPlayOutExit(), channel.voidPromise());
					channel.closeFuture().syncUninterruptibly();
					break;
				}
				channel.writeAndFlush(packet);
			}
		} finally {
			group.shutdownGracefully();
		}
	}

	public List<Class<? extends Packet>> getOutPackets() {
		return outPackets;
	}

	public List<Class<? extends Packet>> getInPackets() {
		return inPackets;
	}
	
	public Channel getMainChannel() {
		return channel;
	}
	
	public void sendPacket(Packet packet) {
		channel.writeAndFlush(packet, channel.voidPromise());
	}
}
