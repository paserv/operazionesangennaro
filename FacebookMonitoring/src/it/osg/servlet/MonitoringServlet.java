package it.osg.servlet;

import it.osg.analyser.FacebookAnalyser;
import it.osg.datasource.FacebookSourceGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class MonitoringServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Scanner scan = new Scanner(new File("facebook_page.conf"));
		while (scan.hasNext()) {
			String currRowConf = scan.next();
			String[] split = currRowConf.split(",");
			String entityName = split[0];
			String currUrl = split[1];
			
			
			String jsonString = FacebookSourceGenerator.retrieveJson(currUrl);
			ArrayList<Hashtable<String, String>> analisi = FacebookAnalyser.likeTalkAnalysis(jsonString);
			
			Iterator<Hashtable<String, String>> iter = analisi.iterator();
			while(iter.hasNext()){
				Entity currEntity = new Entity(entityName);
				Hashtable<String, String> currRow = iter.next();
				Enumeration<String> enumer = currRow.keys();
				while (enumer.hasMoreElements()){
					String currKey = enumer.nextElement();
					String currValue = currRow.get(currKey);
					currEntity.setProperty(currKey, currValue);
					System.out.println(currKey + " " + currValue);
				}
				datastore.put(currEntity);
							
			}
		}
		
		
		
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	

}
