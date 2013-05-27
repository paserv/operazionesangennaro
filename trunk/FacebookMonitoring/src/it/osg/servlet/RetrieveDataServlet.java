package it.osg.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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

		//aggiustare questo pezzotto
		Scanner scan = new Scanner(new File("facebook_page.conf"));
		while (scan.hasNext()) {
			String currRowConf = scan.next();
			String[] split = currRowConf.split(",");
			String entityName = split[0];

			Query q = new Query(entityName);
			PreparedQuery pq = datastore.prepare(q);

			for (Entity result : pq.asIterable()) {
				Date date = (Date) result.getProperty("date");

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

				out.println(entityName + "," + date + "," + like_count + "," + talking_about_count + "," + timestamp + "</br>");

				//System.out.println(entityName + "," + date + "," + like_count + "," + talking_about_count + "," + timestamp);
			}

		}






	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}



}
