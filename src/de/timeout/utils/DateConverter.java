package de.timeout.utils;

import java.util.Calendar;

public class DateConverter {

	public static long getTimeMillis(long day, long hours, long minutes) {
		return (day*24*60*60*1000) + (hours*60*60*1000) + (minutes*60*1000);
	}
	
	public static String getDate(long millis) {
		if(millis > 0) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(millis);
			
			int years = c.get(Calendar.YEAR);
			int months = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			return "§c" + day + "." + months + "." + years + " - " + hour + ":" + minute;
		} else return "§cPERMANENT";
	}
	
	public static int[] getDateTime(long millis) {
		int oneDay = 1000*60*60*24;
		int oneHour = 1000*60*60;
		int oneMinute = 1000*60;
	
		int days = (int) (millis / oneDay);
		millis = millis - (oneDay * days);
		int hours = (int) (millis / oneHour);
		millis = millis - (oneHour * hours);
		int minutes = (int) (millis / oneMinute);
		
		return new int[] {days, hours, minutes};
	}
}
