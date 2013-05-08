package it.osg.datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class FacebookSourceGenerator {

	//private static String url = "https://graph.facebook.com/166115370094396/feed?access_token=156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk&since=1365408000&until=1410163200&fields=message,comments&limit=10000&locale=it_IT";
	//private static String url = "https://graph.facebook.com/166115370094396/feed?access_token=156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk";
	
	public static String retrieveJson(String url) {
		InputStream is = null;
		String jsonString = "";
		try {
			is = new URL(url).openStream();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				jsonString = readAll(rd);
				//System.out.println(jsonString);
			} finally {
				is.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return jsonString;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}



	public static void main(String[] args) {
		FacebookSourceGenerator.retrieveJson("https://graph.facebook.com/166115370094396/feed?access_token=156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk");
	}

}
