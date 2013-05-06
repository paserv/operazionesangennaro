package it.osg.servlet;

import it.osg.analyser.FacebookAnalyser;
import it.osg.datasource.FacebookSourceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class MonitoringBallaroServlet extends HttpServlet {

	private static String url = "https://graph.facebook.com/Ballaro.Rai";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		String jsonString = FacebookSourceGenerator.retrieveJson(url);
		ArrayList<Hashtable<String, Object>> analisi = FacebookAnalyser.likeTalkAnalysis(jsonString);
		
		Iterator<Hashtable<String, Object>> iter = analisi.iterator();
		while(iter.hasNext()){
			Entity currEntity = new Entity("ballaro");
			Hashtable<String, Object> currRow = iter.next();
			Enumeration<String> enumer = currRow.keys();
			while (enumer.hasMoreElements()){
				String currKey = enumer.nextElement();
				Object currValue = currRow.get(currKey);
				currEntity.setProperty(currKey, currValue);
				System.out.println(currKey + " " + currValue);
			}
			datastore.put(currEntity);
						
		}
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	

}
