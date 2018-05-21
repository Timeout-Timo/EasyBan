package de.timeout.bukkit.ban.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.inventory.ItemStack;

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
			ps.setString(10, BukkitSQLManager.encodeItemStack(title));
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
