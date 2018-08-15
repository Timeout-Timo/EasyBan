package de.timeout.bukkit.ban.manager;

import java.lang.reflect.Field;
import java.util.UUID;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.file.FileManager;
import de.timeout.bukkit.ban.netty.PacketReader;
import de.timeout.bukkit.ban.netty.PacketReader.PacketReaderHandler;
import de.timeout.bukkit.ban.netty.packets.PacketInfoInBanned;
import de.timeout.bukkit.ban.netty.packets.PacketInfoOutBanned;
import de.timeout.bukkit.netty.packets.Packet;
import de.timeout.utils.BukkitSQLManager;
import io.netty.channel.ChannelHandlerContext;

public class DecidationManager {
	
	private static BanGUI main = BanGUI.plugin;
	
	public static boolean isBanned(UUID uuid) {
		if(main.isFileSupportEnabled()) {
			if(main.isBungeeCordEnabled()) {
				PacketInfoOutBanned banned = new PacketInfoOutBanned(uuid);
				
				final boolean result = false;
				new PacketReader(banned, new PacketReaderHandler() {
					
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
						if(packet instanceof PacketInfoInBanned) {
							PacketInfoInBanned in = (PacketInfoInBanned) packet;
							UUID uuid = in.getUniqueID();
							if(uuid.toString().equalsIgnoreCase(banned.getUniqueID().toString())) {
								Reflections.changeValue(true, in, "banned", result);
							}
						}
					}
				});
				return result;
			} else return FileManager.isBanned(uuid);
		} else return BukkitSQLManager.isBanned(uuid);
	}
	
	public static class Reflections {
		
		public static void changeValue(Object value, Packet packet, String field, Object placer) {
			try {
				Field f = packet.getClass().getDeclaredField(field);
				f.setAccessible(true);
				f.set(placer, value);
				f.setAccessible(false);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}
	}
}
