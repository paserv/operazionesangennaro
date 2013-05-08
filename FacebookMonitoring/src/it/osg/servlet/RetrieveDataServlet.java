package it.osg.servlet;

import it.osg.analyser.FacebookAnalyser;
import it.osg.datasource.FacebookSourceGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class RetrieveDataServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Scanner scan = new Scanner(new File("facebook_page.conf"));
		while (scan.hasNext()) {
			String currRowConf = scan.next();
			String[] split = currRowConf.split(",");
			String entityName = split[0];
			
			Query q = new Query(entityName);
			PreparedQuery pq = datastore.prepare(q);
			
			for (Entity result : pq.asIterable()) {
				  Date date = (Date) result.getProperty("date");
				  String like_count = (String) result.getProperty("like_count");
				  String talking_about_count = (String) result.getProperty("talking_about_count");
				  long timestamp = (Long) result.getProperty("timestamp");

				  out.println(entityName + "," + date + "," + like_count + "," + talking_about_count + "," + timestamp + "</br>");

				  //System.out.println(entityName + "," + date + "," + like_count + "," + talking_about_count + "," + timestamp);
				}
			
		}
		
		
        
		
		
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	

}
