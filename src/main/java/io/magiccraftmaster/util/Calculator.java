package io.magiccraftmaster.util;

@SuppressWarnings("unused")
public final class Calculator {
	/**
	 * Creates a random number within the given range
	 * @param min the minimum number
	 * @param max the maximum number
	 * @param round whether the output should be rounded
	 * @return the generated number
	 */
	public static double randomIn(double min, double max, boolean round) {
		double value = Math.random() * (max - min) + min;
		return round ? Math.round(value) : value;
	}

	/**
	 * Performs a chance test
	 * @param percent an int between 1-100
	 * @return true if the chance was a success
	 */
	public static boolean chance(int percent) {
		return Math.random() <= (double) percent/100;
	}
}
