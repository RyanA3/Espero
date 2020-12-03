package me.felnstaren.espero.util;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrayUtil {

	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T obj, int pos, T[] arr) {
		T[] new_arr = (T[]) Array.newInstance(obj.getClass(), arr.length + 1);
		for(int i = 0; i < new_arr.length; i++) {
			if(i == pos) new_arr[i] = obj;
			else if(i < pos) new_arr[i] = arr[i];
			else if(i > pos) new_arr[i] = arr[i-1];
		}
		return arr;
	}

	public static <T> T[] append(T obj, T[] arr) {
		return insert(obj, arr.length, arr);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] removeNulls(T[] arr) {
		int arr_length = 0;
		for(int i = 0; i < arr.length; i++) if(arr[i] != null) arr_length++;
		
		T[] new_arr = (T[]) Array.newInstance(arr.getClass(), arr_length);
		for(int i = 0, j = 0; i < arr.length; i++) {
			if(arr[i] == null) continue;
			new_arr[++j] = arr[i];
		}
		
		return new_arr;
	}
	
	
	/**
	 * Gets all indices of a substring in a string
	 * @param string The string you are searching
	 * @param key The string to look for
	 * @return Integer array of indices
	 */
	public static int[] getIndices(String string, String key) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		for(int i = 0; i < string.length(); i++) {
			if(key.length() > string.length() - i) break;
			for(int j = 0; j < key.length(); j++) {
				if(string.charAt(i + j) != key.charAt(j)) break;
				if(j == key.length() - 1) indexes.add(i);
			}
		}
		
		int[] aindexes = new int[indexes.size()];
		for(int i = 0; i < indexes.size(); i++) aindexes[i] = indexes.get(i);
		return aindexes;
	}
	
	
	
	public static String[] stringver(Object[] objs) {
		String[] strs = new String[objs.length];
		for(int i = 0; i < objs.length; i++)
			strs[i] = (String) objs[i];
		return strs;
	}
	
}
