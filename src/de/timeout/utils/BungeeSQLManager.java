package de.timeout.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import de.timeout.utils.Reason.ReasonType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeSQLManager {

	public static boolean isBanned(ProxiedPlayer p) {
		if(isBannedByIP(p.getPendingConnection().getAddress().getHostName()) || isBannedByUUID(p.getUniqueId()))return true;
		return false;	
	}
	
	public static boolean isBanned(String ip, UUID uuid) {
		if(isBannedByIP(ip) || isBannedByUUID(uuid))return true;
		return false;	
	}
	
	public static boolean isMuted(String ip, UUID uuid) {
		if(isMutedByIP(ip) || isMutedByUUID(uuid))return true;
		return false;
	}
	
	public static boolean isBannedByUUID(UUID uuid) {
		if(!uuid.equals(null)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM Bans WHERE UUID = ?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				return rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isBannedByIP(String ip) {
		if(!ip.equals(null)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT IP FROM Bans WHERE IP = ?");
				ps.setString(1, ip);
				ResultSet rs = ps.executeQuery();
				return rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isMutedByIP(String ip) {
		if(!ip.equals(null)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT IP FROM Mutes WHERE IP = ?");
				ps.setString(1, ip);
				ResultSet rs = ps.executeQuery();
				return rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean isMutedByUUID(UUID uuid) {
		if(uuid.equals(null)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT UUID FROM Mutes WHERE UUID = ?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				return rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static long getBanTime(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unban FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Unban");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long getBanTime(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unban FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Unban");
			}
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
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Unmute");
			}
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
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Unmute");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}	
	public static long getFirstLine(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT First FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("First");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getSecondLine(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Second FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Second");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getFirstBan(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT FirstBan FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("FirstBan");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getSecondBan(String name, ReasonType reasonType) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT SecondBan FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, reasonType.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("SecondBan");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}	
	
	public static long getPoints(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Points FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Points");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}	
	
	public static long getThirdBan(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT ThirdBan FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("ThirdBan");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static String getDisplay(String name, ReasonType type) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Display FROM Settings WHERE Name = ? AND Type = ?");
			ps.setString(1, name);
			ps.setString(2, type.getName());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Display");
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static long getPlayerViolences(ProxiedPlayer player) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Violence FROM History WHERE UUID = ?");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Violence");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getBanTime(ProxiedPlayer player) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unban FROM Bans WHERE UUID = ?");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next()) {
				l = rs.getLong("Unban");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getMuteTime(ProxiedPlayer player) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Unmute FROM Mutes WHERE UUID = ?");
			ps.setString(1, player.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			long l = 0L;
			while(rs.next()) {
				l = rs.getLong("Unmute");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static void updateHistory(UUID uuid, long violences) {
		if(HistoryExists(uuid)) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE History SET Violence = ? WHERE UUID = ?");
				ps.setLong(1, violences);
				ps.setString(2, uuid.toString());
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO History VALUES(?, ?)");
				ps.setString(1, uuid.toString());
				ps.setLong(2, violences);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean HistoryExists(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Violence FROM History WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addBan(ProxiedPlayer player, long bantime, String banner, String reason) {
		if(isBannedByUUID(player.getUniqueId())) {
			long oldTime = getBanTime(player);
			if(oldTime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Bans SET UNBAN = ?, IP = ?, Banner = ?, Reason = ? WHERE UUID = ?");
					ps.setLong(1, bantime > 0 ? bantime + oldTime : bantime);
					ps.setString(2, player.getAddress().getAddress().getHostAddress());
					ps.setString(3, banner);
					ps.setString(4, reason);
					ps.setString(5, player.getUniqueId().toString());
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Bans VALUES (?, ?, ?, ?, ?, ?)");
				ps.setString(1, player.getUniqueId().toString());
				ps.setString(2, player.getAddress().getAddress().getHostAddress());
				ps.setString(3, player.getName());
				ps.setString(4, reason);
				ps.setLong(5, bantime > 0 ? bantime + System.currentTimeMillis() : bantime);
				ps.setString(6, banner);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void addMute(ProxiedPlayer player, long mutetime, String muter, String reason) {
		if(isMutedByUUID(player.getUniqueId())) {
			long oldTime = getMuteTime(player);
			if(oldTime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Mutes SET Unmute = ?, IP = ?, Banner = ?, Reason = ? WHERE UUID = ?");
					ps.setLong(1, mutetime > 0 ? mutetime + oldTime : mutetime);
					ps.setString(2, player.getAddress().getAddress().getHostAddress());
					ps.setString(3, muter);
					ps.setString(4, reason);
					ps.setString(5, player.getUniqueId().toString());
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Mutes VALUES(?, ?, ?, ?, ?, ?)");
				ps.setString(1, player.getUniqueId().toString());
				ps.setString(2, player.getAddress().getAddress().getHostAddress());
				ps.setString(3, player.getName());
				ps.setString(4, reason);
				ps.setLong(5, mutetime > 0 ? mutetime + System.currentTimeMillis() : mutetime);
				ps.setString(6, muter);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static long getPlayerViolences(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Violence FROM History WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			long l = 0;
			while(rs.next()) {
				l = rs.getLong("Violence");
			}
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void addBan(UUID uuid, String ip, String name, long bantime, String banner, String reason) {
		if(isBannedByUUID(uuid)) {
			long oldtime = getBanTime(uuid);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Bans SET UNBAN = ?, IP = ?, Banner = ?, Reason = ?, Name = ? WHERE UUID = ?");
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
		} else if(isBannedByIP(ip)) {
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
				ps.setLong(4, bantime > 0 ? System.currentTimeMillis() + bantime : bantime);
				ps.setString(5, reason);
				ps.setString(6, banner);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addMute(UUID uuid, String ip, String name, long mutetime, String muter, String reason) {
		if(isMutedByUUID(uuid)) {
			long oldtime = getMuteTime(uuid);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Mutes SET Unmute = ?, IP = ?, Banner = ?, Reason = ?, Name = ? WHERE UUID = ?");
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
		} else if(isMutedByIP(ip)) {
			long oldtime = getMuteTime(ip);
			if(oldtime > 0) {
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE Mutes SET Unmute = ?, UUID = ?, Banner = ?, Reason = ?, Name = ? WHERE IP = ?");
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
				PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Mutes VALUES(?, ?, ?, ?, ?, ?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, ip);
				ps.setString(3, name);
				ps.setLong(4, mutetime > 0 ? mutetime + System.currentTimeMillis() : mutetime);
				ps.setString(5, reason);
				ps.setString(6, muter);
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getBanReason(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = null;
			while(rs.next()) {
				s = rs.getString("Reason");
			}
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
			String s = null;
			while(rs.next()) {
				s = rs.getString("Reason");
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBanner(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Banner FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Banner");
			}
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
			while(rs.next()) {
				s = rs.getString("Banner");
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void unbanPlayer(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Bans WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void unbanPlayer(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Bans WHERE IP = ?");
			ps.setString(1, ip);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void unmutePlayer(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void unmutePlayer(String ip) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Mutes WHERE IP = ?");
			ps.setString(1, ip);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getMuteReason(UUID uuid) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Reason FROM Mutes WHERE UUID = ?");
			ps.setString(1, uuid.toString());
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

	public static String getMuter(ProxiedPlayer p) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Muter FROM Mutes WHERE UUID = ?");
			ps.setString(1, p.getUniqueId().toString());
			ResultSet rs = ps.executeQuery();
			String s = "";
			while(rs.next()) {
				s = rs.getString("Muter");
			}
			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> getBannedPlayers() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Bans");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) list.add(rs.getString("Name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<String> getMutedPlayers() {
		ArrayList<String> list = new ArrayList<String>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT Name FROM Mutes");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) list.add(rs.getString("Name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
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
}