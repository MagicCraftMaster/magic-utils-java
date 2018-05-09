package io.magiccraftmaster.util;

public final class ArrayUtils {
	/**
	 * Preforms a 'cache drop' to an array
	 * @param array the array to drop
	 */
	public static void cacheDrop(Object[] array) {
		for (int i=array.length-1; i>0; i--) {
			if (array[i] == null) {
				array[i]   = array[i-1];
				array[i-1] = null;
			}
		}
	}
}
