package de.timeout.bukkit.ban.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.netty.packets.PacketPlayOutAddReason;
import de.timeout.utils.BukkitSQLManager;
import de.timeout.utils.MySQL;
import de.timeout.utils.Reason;

public class BukkitReason extends Reason {
	
	private ItemStack title;
	
	public BukkitReason(String name, ReasonType type, long first, long second, long points, long time1, long time2, long time3, String display, ItemStack title) {
		super(name, type, first, second, points, time1, time2, time3, display);
		this.title = title;
	}
	
	public BukkitReason(String name, ReasonType type) {
		super(name, type);
		this.title = BukkitSQLManager.getTitle(name, type);
	}
	
	public ItemStack getTitle() {
		return title;
	}
	
	
	@Override
	public void saveToMySQL() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Settings VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, name);
			ps.setLong(2, firsttime);
			ps.setLong(3, secondtime);
			ps.setLong(4, thirdtime);
			ps.setLong(5, firstToSecond);
			ps.setLong(6, secondToThird);
			ps.setLong(7, points);
			ps.setString(8, type.getName());
			ps.setString(9, display);
			ps.setString(10, ItemStackAPI.encodeItemStack(title));
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getEncodedTitle() {
		return encodeItemStack(title);
	}
	
	public void sendToBungeeCord() {
		PacketPlayOutAddReason reason = new PacketPlayOutAddReason(this);
		BanGUI.plugin.getNettyClient().getMainChannel().writeAndFlush(reason);
	}
	
	//Für BukkitReasons
	@Override
	public void saveToFile() {
		
	}
	
	@SuppressWarnings("resource")
	private String encodeItemStack(ItemStack item) {
		try {
			ByteArrayOutputStream str = new ByteArrayOutputStream();
			BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);
			data.writeObject(item);
			return Base64.getEncoder().encodeToString(str.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
