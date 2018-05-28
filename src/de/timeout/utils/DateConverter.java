package de.timeout.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

	public static long getTimeMillis(long day, long hours, long minutes) {
		return (day*24*60*60*1000) + (hours*60*60*1000) + (minutes*60*1000);
	}
	
	public static String getDate(long millis) {
		if(millis > 0) {
			Date date = new Date(millis);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
			String s = format.format(date);
			return s;
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
