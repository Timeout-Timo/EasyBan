package de.timeout.bukkit.ban.filemanager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.utils.BungeeMessager;
import de.timeout.bukkit.ban.utils.BungeeMessager.BungeeMessageHandler;
import de.timeout.utils.BukkitSQLManager;

public class DecidationManager {

	private static boolean file = BanGUI.plugin.isFileSupportEnabled();
	private static boolean bungee = BanGUI.plugin.isBungeeCordEnabled();
		
	public static boolean isBanned(UUID uuid) {
		if(file) {
			if(bungee) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("isBanned");
				out.writeUTF(uuid.toString());
				
				BungeeMessager messager = new BungeeMessager("BanSystem", out.toByteArray(), Bukkit.getServer(), new BungeeMessageHandler() {
					
					@Override
					public void onPluginMessageReceived(String channel, Player player, byte[] message) {
						System.out.println("Empfangen");
						if(channel.equalsIgnoreCase("BanSystem")) {
							ByteArrayDataInput in = ByteStreams.newDataInput(message);
							String subchannel = in.readUTF();
							if(subchannel.equalsIgnoreCase("isBanned")) {

							}
						}
					}
				});
			} else return BukkitFileManager.isBanned(uuid);
		} else return BukkitSQLManager.isBanned(uuid);
	}
}
