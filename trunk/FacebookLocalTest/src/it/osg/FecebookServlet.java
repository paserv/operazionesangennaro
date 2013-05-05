package it.osg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class FecebookServlet {

	private static String url = "https://graph.facebook.com/166115370094396/feed?access_token=156346967866710%7CgnswdSXw_ObP0RaWj5qqgK_HtCk&limit=100";
	
	public static void main(String[] args) {
		int counter = 1;
		System.out.println("Step 0");
		for(;;) {
			BufferedWriter out = null;
			try {
				FileWriter fstream = new FileWriter("c:/temp/out.csv", true); //true tells to append data.
			    out = new BufferedWriter(fstream);
			    String jsonString = FacebookSourceGenerator.retrieveJson(url);
				ArrayList<Hashtable<String, String>> analisi = FacebookAnalyser.analyse(jsonString);
				
				Iterator<Hashtable<String, String>> iter = analisi.iterator();
				while(iter.hasNext()){
					Hashtable<String, String> currRow = iter.next();
					Enumeration<String> enumer = currRow.keys();
					while (enumer.hasMoreElements()){
						String currKey = enumer.nextElement();
						String currValue = currRow.get(currKey);
						//System.out.println(currKey);
						out.write(currValue + ",");
					}
					out.write("\n");			
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					System.out.println("ENDED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(300000);
				System.out.println("Step " + counter++);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
		

}
