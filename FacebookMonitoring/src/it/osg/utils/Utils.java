package it.osg.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utils {

	public static String MD5(String string) throws UnsupportedEncodingException {  
		String signature = null;

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(string.getBytes(),0,string.length());
			signature = new BigInteger(1,md5.digest()).toString(16);
			//System.out.println("Signature: " + signature);
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return signature;
	} 


}
