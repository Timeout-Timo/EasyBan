package de.timeout.utils;

import java.lang.reflect.Field;

public class Reflections {

	public static Field getField(Class<?> clazz, String fieldname) throws NoSuchFieldException {
		try {
			Field field = clazz.getDeclaredField(fieldname);
			return field;
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		throw new NoSuchFieldException("Field could not be found");
	}
	
	public static void setField(Object object, String name, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(object, value);
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void setField(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
			field.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
