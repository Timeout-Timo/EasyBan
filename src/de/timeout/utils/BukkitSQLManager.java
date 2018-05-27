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

import org.bukkit.entity.Player;
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
	
	public static boolean isBanned(String ip, UUID uuid) {
		return isBanned(uuid) || isIPBanned(ip);
	}
	
	public static boolean isBanned(String name) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Bans WHERE Name = ?");
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
	
	public static String getBanReasonName(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next())s = rs.getString("Reason");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
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
	
	public static boolean isMuted(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isIPMuted(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT IP FROM Mutes WHERE IP = ?");
			ps.setString(1, ip);
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
	
	public static void unmute(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Mutes WHERE IP = ?");
			ps.setString(1, ip);
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
	
	public static void unban(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
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
	
	public static void removeReason(String name) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Settings WHERE Name = ?");
			ps.setString(1, name);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean reasonExists(String name) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Settings WHERE Name = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isIPBanned(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT IP FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isBanned(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ReasonType getType(String reasonname) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Type FROM Settings WHERE Name = ?");
			ps.setString(1, reasonname);
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Type");
			}
			return ReasonType.getTypeByName(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new NullPointerException("Unknown ReasonTypeName");
	}
	
	public static long getBanTime(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unban FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next())l = rs.getLong("Unban");
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long getBanTime(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unban FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next())l = rs.getLong("Unban");
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long getMuteTime(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unmute FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next())l = rs.getLong("Unban");
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long getMuteTime(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unmute FROM Mutes WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next())l = rs.getLong("Unban");
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void addBan(Player banned, long bantime, String banner, String reason) {
		addBan(banned.getUniqueId(), banned.getAddress().getAddress().getHostAddress(), banned.getName(), bantime, banner, reason);
	}
	
	public static void addBan(UUID uuid, String ip, String name, long bantime, String banner, String reason) {
		if(isBanned(uuid)) {
			long oldtime = getBanTime(uuid);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Bans SET Unban = ?, IP = ?, Banner = ?, Reason = ?, Name = ? WHERE UUID = ?");
					ps.setLong(1, bantime > 0 ? bantime + oldtime : bantime);
					ps.setString(2, ip);
					ps.setString(3, banner);
					ps.setString(4, reason);
					ps.setString(5, name);
					ps.setString(6, uuid.toString());
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if(isBanned(ip)) {
			long oldtime = getBanTime(ip);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Bans SET Unban = ?, UUID = ?, Banner = ?, Reason = ?, Name = ? WHERE IP = ?");
					ps.setLong(1, bantime > 0 ? bantime + oldtime : bantime);
					ps.setString(2, uuid.toString());
					ps.setString(3, banner);
					ps.setString(4, reason);
					ps.setString(5, name);
					ps.setString(6, ip);
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Bans VALUES (?, ?, ?, ?, ?, ?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, ip);
				ps.setString(3, name);
				ps.setString(4, reason);
				ps.setLong(5, bantime > 0 ? System.currentTimeMillis() + bantime : bantime);
				ps.setString(6, banner);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addMute(UUID uuid, String ip, String name, long mutetime, String muter, String reason) {
		if(isMuted(uuid)) {
			long oldtime = getMuteTime(uuid);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Mutes SET Unmute = ?, IP = ?, Muter = ?, Reason = ?, Name = ? WHERE UUID = ?");
					ps.setLong(1, mutetime > 0 ? mutetime + oldtime : mutetime);
					ps.setString(2, ip);
					ps.setString(3, muter);
					ps.setString(4, reason);
					ps.setString(5, name);
					ps.setString(6, uuid.toString());
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if(isMuted(ip)) {
			long oldtime = getMuteTime(ip);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Mutes SET Unmute = ?, UUID = ?, Muter = ?, Reason = ?, Name = ? WHERE IP = ?");
					ps.setLong(1, mutetime > 0 ? mutetime + oldtime : mutetime);
					ps.setString(2, uuid.toString());
					ps.setString(3, muter);
					ps.setString(4, reason);
					ps.setString(5, name);
					ps.setString(6, ip);
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Mutes VALUES (?, ?, ?, ?, ?, ?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, ip);
				ps.setString(3, name);
				ps.setString(4, reason);
				ps.setLong(5, mutetime > 0 ? System.currentTimeMillis() + mutetime : mutetime);
				ps.setString(6, muter);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getBanner(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Banner FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next())s = rs.getString("Banner");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBanReason(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) s = rs.getString("Reason");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBanner(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Banner FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next())s = rs.getString("Banner");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void updateIPAddress(String ip, String name, UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Bans SET IP = ?, Name = ? WHERE UUID = ?");
			ps.setString(1, ip);
			ps.setString(2, name);
			ps.setString(3, uuid.toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isMuted(String ip, UUID uuid) {
		return isMuted(uuid) || isIPMuted(ip);
	}

	public static String getMuteReason(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) s = rs.getString("Reason");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMuter(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Muter FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next())s = rs.getString("Muter");
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
