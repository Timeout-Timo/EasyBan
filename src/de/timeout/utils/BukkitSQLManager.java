package de.timeout.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.Reason.ReasonType;

public class BukkitSQLManager {

	public static ArrayList<BukkitReason> getBanReasons() {
		ArrayList<BukkitReason> list = new ArrayList<BukkitReason>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Settings WHERE Type = ?");
			ps.setString(1, "Ban");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				BukkitReason r = new BukkitReason(rs.getString("Name"), ReasonType.BAN, rs.getLong("First"), rs.getLong("Second"), rs.getLong("Points"),
						rs.getLong("FirstBan"), rs.getLong("SecondBan"), rs.getLong("ThirdBan"), rs.getString("Display"), decodeItemStack(rs.getString("Title")));
				list.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<BukkitReason> getMuteReasons() {
		ArrayList<BukkitReason> list = new ArrayList<BukkitReason>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Settings WHERE Type = ?");
			ps.setString(1, "Mute");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				BukkitReason r = new BukkitReason(rs.getString("Name"), ReasonType.BAN, rs.getLong("First"), rs.getLong("Second"), rs.getLong("Points"),
						rs.getLong("FirstBan"), rs.getLong("SecondBan"), rs.getLong("ThirdBan"), rs.getString("Display"), decodeItemStack(rs.getString("Title")));
				list.add(r);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static String getNameByItemStack(ItemStack title, String type) {
		String encoded = encodeItemStack(title);
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Settings WHERE Title = ? AND Type = ?");
			ps.setString(1, encoded);
			ps.setString(2, type);
			ResultSet rs = ps.executeQuery();
			String name = null;
			while(rs.next()) {
				name = rs.getString("Name");
			}
			return name;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isBanned(String name) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT NAME FROM Bans WHERE Name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ItemStack getTitle(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Title FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Title");
			}
			return decodeItemStack(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getBanReasonName(String playerName) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Bans WHERE Name = ?");
			ps.setString(1, playerName);
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Reason");
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMuteReasonName(String playerName) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Mutes WHERE Name = ?");
			ps.setString(1, playerName);
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next())s = rs.getString("Reason");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isMuted(String name) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Mutes WHERE Name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void unmute(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateHistory(UUID uuid, long addedPoints) {
		if(hasHistory(uuid)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE History SET Violence = ? WHERE UUID = ?");
				ps.setLong(1, getViolencePoints(uuid) + addedPoints);
				ps.setString(2, uuid.toString());
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static long getViolencePoints(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Violence FROM History WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) l = rs.getLong("Violence");
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return 0;
	}
	
	public static boolean hasHistory(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM History WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void unban(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static String encodeItemStack(ItemStack item) {
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
	
	@SuppressWarnings("resource")
	public static ItemStack decodeItemStack(String base64) {
		try {
			ByteArrayInputStream str = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
			BukkitObjectInputStream data = new BukkitObjectInputStream(str);
			ItemStack item = (ItemStack) data.readObject();
			return item;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
