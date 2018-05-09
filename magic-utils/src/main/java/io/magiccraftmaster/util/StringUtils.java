package io.magiccraftmaster.util;

import org.jetbrains.annotations.Contract;

import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings(value={"unused","WeakerAccess"})
public final class StringUtils {
	/**
	 * Clip operations for {@link #clip(String[], ClipType, int)}
	 */
	public enum ClipType {
		/**
		 * Removes all items to the left of the array (first indexes)
		 */
		LEFT,
		/**
		 * Removes all items to the right of the array (last indexes)
		 */
		RIGHT,
		/**
		 * Removes all items to the left and right of the array (first and last indexes)
		 */
		LEFT_RIGHT
	}

	/**
	 * Date format operations for {@link #formatDate(OffsetDateTime, DateFormatType, boolean, boolean)}
	 */
	@Deprecated
	public enum DateFormatType {
		/**
		 * Shows the date and time
		 */
		DATE_TIME,
		/**
		 * Shows the date
		 */
		DATE,
		/**
		 * Shows the time
		 */
		TIME
	}

	// ====================================================================================================

	/**
	 * Converts a string array to a string
	 * @param strings the array of strings to combine
	 * @param separator the character(s) to separate with
	 * @return a new string from the array
	 */
	@Contract(pure = true)
	public static String toString(String[] strings, String separator) {
		if (strings == null || strings.length == 0) return "";
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : strings) stringBuilder.append(string).append(separator);
		return stringBuilder.substring(0, stringBuilder.length() - separator.length());
	}

	/**
	 * Returns a string of the item in the format 'one', 'two' and 'three'
	 * @param strings the strings to combine
	 * @return a string of the given array
	 */
	@Contract(pure = true)
	public static String toListString(String[] strings) {
		StringBuilder stringBuilder = new StringBuilder();
		if (strings == null || strings.length == 0) return "";
		if (strings.length == 1) return "'" + strings[0] + "'";
		for (int i=0, len=strings.length; i<len; i++) {
			if (i < len-2)
				stringBuilder.append("'").append(strings[i]).append("', ");
			else if (i < len-1)
				stringBuilder.append("'").append(strings[i]).append("' ");
			else
				stringBuilder.append("and '").append(strings[i]).append("'");
		}
		return stringBuilder.toString();
	}

	/**
	 * Ensures that a string starts with an uppercase letter<br>
	 *     Eg: <code>"all lower"</code> becomes <code>"All lower"</code>
	 * @param string the string to adjust
	 * @return the fixed string
	 */
	@Contract(pure = true)
	public static String fixCase(String string) {
		if (string == null || string.length() == 0) throw new NullPointerException();
		if (string.length() == 1) return string.toUpperCase();
		return string.substring(0,1).toUpperCase() + string.substring(1);
	}

	/**
	 * Removes ends of an array as specified
	 * @param array the array to "clip"
	 * @param clipType a mode
	 * @param distance how many elements to remove from the end(s)
	 * @return the clipped array
	 */
	@Contract(pure = true)
	public static String[] clip(String[] array, ClipType clipType, int distance) {
		String[] array2 = null;
		switch (clipType) {
			case LEFT:
				array2 = new String[array.length - distance];
				System.arraycopy(array, distance, array2, 0,array.length - distance);
				break;

			case RIGHT:
				array2 = new String[array.length - distance];
				System.arraycopy(array, 0, array, 0,array.length - distance);
				break;

			case LEFT_RIGHT:
				array2 = new String[array.length - distance * 2];
				System.arraycopy(array, distance, array, 0,array.length - distance * 2);
				break;
		}
		return array2;
	}

	/**
	 * Creates a text box around elements of the given array
	 * @param lines an array of strings for the box content
	 * @return a box string
	 */
	@Contract(pure = true)
	public static String box(String[] lines) {
		StringBuilder out = new StringBuilder();
		List<String> newLines = new ArrayList<>();

		for (String line : lines) {
			String[] splitLine = line.replace("\t", "   ").split("\n");
			Collections.addAll(newLines, splitLine);
		}

		int length = 0;
		for (String line : newLines) if (line.length() > length) length = line.length();

		out.append("+");
		for (int i=0; i<length; i++) out.append("-");
		out.append("+\n");

		for (String line : newLines) {
			out.append("|");
			out.append(line);
			for (int i=line.length(); i<length; i++) out.append(" ");
			out.append("|\n");
		}

		out.append("+");
		for (int i=0; i<length; i++) out.append("-");
		out.append("+");

		return out.toString();
	}

	/**
	 * Formats a number to be human readable
	 * @param d the number as a double
	 * @return the formatted number
	 */
	@Contract(pure = true)
	public static String formatNumber(double d) {
		return new DecimalFormat("#,###.##").format(d);
	}


	// = OffsetDateTime formatters = Deprecated ========================================================

	/**
	 * Formats an {@link OffsetDateTime}
	 * @param offsetDateTime the date/time to format
	 * @param formatType the format type
	 * @param military if 24-hour time is to be used
	 * @param clean if number-only rep should be used (AM/PM is shown if <b>military</b> if false)
	 * @return a formatted string
	 * @throws NullPointerException if offsetDateTime or formatType are <code>null</code>
	 */
	@Deprecated
	@Contract(pure = true)
	public static String formatDateTime(OffsetDateTime offsetDateTime, DateFormatType formatType, boolean military, boolean clean) {
		if (offsetDateTime == null || formatType == null) throw new NullPointerException("Neither 'offsetDateTime' nor 'formatType' may be null");
		switch (formatType) {
			case DATE_TIME:
				return formatDate(offsetDateTime.toLocalDate(), clean) + " " + formatTime(offsetDateTime.toLocalTime(), military);

			case DATE:
				return formatDate(offsetDateTime.toLocalDate(), clean);

			case TIME:
				return formatTime(offsetDateTime.toLocalTime(), military);
		}

		throw new NullPointerException();
	}

	/**
	 * Formats an {@link OffsetDateTime}
	 * @param offsetDateTime the date/time to format
	 * @param formatType the format type
	 * @param military if 24-hour time is to be used
	 * @param clean if number-only rep should be used (AM/PM is shown if <b>military</b> if false)
	 * @return a formatted string
	 * @throws NullPointerException if offsetDateTime or formatType are <code>null</code>
	 */
	@Deprecated
	@Contract(pure = true)
	public static String formatDate(OffsetDateTime offsetDateTime, DateFormatType formatType, boolean military, boolean clean) {
		return formatDateTime(offsetDateTime, formatType, military, clean);
	}


	// = OffsetDateTime formatters =====================================================================

	/**
	 * Formats a date time
	 * @param offsetDateTime the date time to format
	 * @param clean if the date should be clean
	 * @param military if the time should be twenty-four hour
	 * @return a human-readable date time
	 * @see OffsetDateTime
	 */
	@Contract(pure = true)
	public static String formatDateTime(OffsetDateTime offsetDateTime, boolean clean, boolean military) {
		return formatDate(offsetDateTime.toLocalDate(), clean) + " " + formatTime(offsetDateTime.toOffsetTime(), military);
	}

	/**
	 * Formats a time
	 * @param offsetTime the time to format
	 * @param military if the time should be twenty-four hour
	 * @return a human-readable time
	 * @see OffsetTime
	 */
	@Contract(pure = true)
	public static String formatTime(OffsetTime offsetTime, boolean military) {
		return formatTime(offsetTime.toLocalTime().plusSeconds(offsetTime.getOffset().getTotalSeconds()), military);
	}


	// = LocalDateTime  formatters =====================================================================

	/**
	 * Formats a date time
	 * @param localDateTime the date time to format
	 * @param clean if the date should be clean
	 * @param military if the time should be twenty-four hour
	 * @return a human-readable date time
	 * @see LocalDateTime
	 */
	@Contract(pure = true)
	public static String formatDateTime(LocalDateTime localDateTime, boolean clean, boolean military) {
		return formatDate(localDateTime.toLocalDate(), clean) + " " + formatTime(localDateTime.toLocalTime(), military);
	}

	/**
	 * Formats a date
	 * @param localDate the date to format
	 * @param clean if the date should be clean
	 * @return a human-readable date
	 * @see LocalDate
	 */
	@Contract(pure = true)
	public static String formatDate(LocalDate localDate, boolean clean) {
		return localDate.format(DateTimeFormatter.ofPattern(clean ? "yyyy/MM/dd" : "MMM dd, yyyy"));
	}

	/**
	 * Formats a time
	 * @param localTime the time to format
	 * @param military if the time should be twenty-four hour
	 * @return a human-readable time
	 * @see LocalTime
	 */
	@Contract(pure = true)
	public static String formatTime(LocalTime localTime, boolean military) {
		return localTime.format(DateTimeFormatter.ofPattern(military ? "HH:mm" : "hh:mm a"));
	}
}
