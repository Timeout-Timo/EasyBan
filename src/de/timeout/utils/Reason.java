package de.timeout.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Reason {
	
	public enum Stage{FIRST, SECOND, THIRD;}

	protected String name;
	protected ReasonType type;
	protected long firsttime;
	protected long secondtime;
	protected long thirdtime;
	protected long firstToSecond;
	protected long secondToThird;
	protected long points;
	protected String display;
	
	public Reason(String name, ReasonType type, long firstLine, long secondLine, long points, long time1, long time2, long time3, String display) {
		this.name = name;
		this.type = type;
		this.firstToSecond = firstLine;
		this.secondToThird = secondLine;
		this.points = points;
		this.display = display;
		this.firsttime = time1;
		this.secondtime = time2;
		this.thirdtime = time3;
	}
	
	public Reason(String name, ReasonType type) {
		this.name = name;
		this.type = type;
		this.firstToSecond = BungeeSQLManager.getFirstLine(name, type);
		this.secondToThird = BungeeSQLManager.getSecondLine(name, type);
		this.firsttime = BungeeSQLManager.getFirstBan(name, type);
		this.secondtime = BungeeSQLManager.getSecondBan(name, type);
		this.thirdtime = BungeeSQLManager.getThirdBan(name, type);
		this.points = BungeeSQLManager.getPoints(name, type);
		this.display = BungeeSQLManager.getDisplay(name, type);
	}
	
	public String getName() {
		return name;
	}
	
	public ReasonType getType() {
		return type;
	}
	
	public Stage getValidStage(long violences) {
		if(violences >= secondToThird)return Stage.THIRD;
		else if(violences  > firstToSecond)return Stage.SECOND;
		return Stage.FIRST;
	}

	public long getViolencePoints() {
		return points;
	}

	public String getDisplayName() {
		return display;
	}
	
	public enum ReasonType {
		
		BAN("Ban"), MUTE("Mute");
		
		private String name;
		
		private ReasonType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static ReasonType getTypeByName(String name) {
			for(ReasonType type : values()) {
				if(type.getName().equalsIgnoreCase(name))return type;
			}
			throw new IllegalArgumentException("Enum could not be found");
		}
	}

	public long getFirsttime() {
		return firsttime;
	}

	public long getSecondtime() {
		return secondtime;
	}

	public long getThirdtime() {
		return thirdtime;
	}

	public long getFirstToSecond() {
		return firstToSecond;
	}

	public long getSecondToThird() {
		return secondToThird;
	}

	public long getPoints() {
		return points;
	}

	public String getDisplay() {
		return display;
	}
	
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
			ps.setString(10, null);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
