package it.osg.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Utils {

	public static String MD5(String md5, String encoding) throws UnsupportedEncodingException {  
		try {        
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			String s = new String(md5.getBytes(), encoding);
			byte[] array = md.digest(s.getBytes());       
			StringBuffer sb = new StringBuffer();       
			for (int i = 0; i < array.length; ++i) { 
				String hex=Integer.toHexString((array[i] & 0xFF)+0x100).substring(1);
				sb.append(hex);
			}        
			return sb.toString();     }
		catch (java.security.NoSuchAlgorithmException e) {   
			throw new IllegalArgumentException("Attenzione non esiste l'algoritmo di cifratura selezionato");
		}   
	} 
	
	public static double getUniqueAuthors(String auth) {
		String[] splitted = auth.split(",");
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < splitted.length; i++) {
			list.add(splitted[i]);
		}
		return ArrayUtils.removeDuplicate(list).size();
	}
	
}
