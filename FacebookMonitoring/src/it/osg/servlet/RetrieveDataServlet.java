package it.osg.servlet;

import it.osg.utils.DateUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

	private static String confTable = "pages";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		out.println("id,trasmissione,date,like_count,talking_about_count,timestamp" + "</br>");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		ArrayList<String> conf = getConfPage(confTable);

		Iterator<String> iter = conf.iterator();
		
		int counter = 0;
		while (iter.hasNext()) {
			String currTransmission = iter.next();
			
			Query q = new Query(currTransmission);
			PreparedQuery pq = datastore.prepare(q);
			
			for (Entity result : pq.asIterable()) {
				Date date = (Date) result.getProperty("date");

				String myDate = DateUtils.formatDateAndTime(date);
				
				String like_count = "";
				Object lk = result.getProperty("like_count");
				if (lk instanceof String) {
					like_count = (String) lk;
				} else {
					like_count = String.valueOf((Long) lk);
				}

				String talking_about_count = "";
				Object tk = result.getProperty("talking_about_count");
				if (tk instanceof String) {
					talking_about_count = (String) tk;
				} else {
					talking_about_count = String.valueOf((Long) tk);
				}

				long timestamp = 0L;
				Object tm = result.getProperty("timestamp");
				if (tm instanceof Long) {
					timestamp = (Long) tm;
				} else {
					timestamp = Long.valueOf((String) tm);
				}
				counter++;
				out.println(counter + "," + currTransmission + "," + myDate + "," + like_count + "," + talking_about_count + "," + timestamp + "</br>");
			}
		}
		
		

		
				
	}


	private ArrayList<String> getConfPage(String entityName) {
		ArrayList<String> result = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query(entityName);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity res : pq.asIterable()) {
			String id = res.getKey().getName();
			
			result.add(id);
		}
		return result;

	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}



}
