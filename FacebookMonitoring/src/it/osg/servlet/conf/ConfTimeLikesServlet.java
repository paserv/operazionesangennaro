package it.osg.servlet.conf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class ConfTimeLikesServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		
		String transmission = req.getParameter("transmission");
				
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Filter transFilter = new FilterPredicate("ID", FilterOperator.EQUAL, transmission);
		Query q = new Query("pages").setFilter(transFilter);
		
		PreparedQuery pq = datastore.prepare(q);
		
		if (pq.countEntities() != 0) {
			resp.sendRedirect("../monitor/conf/conf_time_likes.html");
		} else {
			Calendar cal= Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.add(Calendar.HOUR_OF_DAY, 2);
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date cestDate= cal.getTime();
			Entity currEntity = new Entity("pages", transmission);
			currEntity.setProperty("startingdate", cestDate);
			
			datastore.put(currEntity);
			
			resp.sendRedirect("../index.html");
		}
		
		
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	

}
