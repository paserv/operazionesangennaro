package it.osg.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayUtils {

	public static ArrayList<String> removeDuplicate (ArrayList<String> list) {
		ArrayList<String> result = new ArrayList<String>();
		
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			String curr = iter.next();
			if (!result.contains(curr)) {
				result.add(curr);
			}
		}
		return result;
	}
	
}
