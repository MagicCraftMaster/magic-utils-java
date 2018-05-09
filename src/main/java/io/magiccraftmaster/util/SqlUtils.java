package io.magiccraftmaster.util;

import org.json.JSONArray;

public final class SqlUtils {
	/**
	 * @param strings the strings to convert
	 * @return a json array string from the given strings
	 */
	public static String toJsonArray(String[] strings) {
		JSONArray array = new JSONArray();
		for (String string : strings) {
			array.put(string);
		}
		return array.toString();
	}

	/**
	 * @param string a json array string
	 * @return an array from the json array string
	 */
	public static String[] fromJsonArray(String string) {
		JSONArray array = new JSONArray(string);
		String[] strings = new String[array.length()];
		for (int i=0, len=array.length(); i<len; i++) if (array.get(i) instanceof String) strings[i] = (String) array.get(i);
		return strings;
	}
}
