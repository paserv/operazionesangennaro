package it.osg.servlet.monitoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public abstract class MonitoringServlet extends HttpServlet {
	

	public abstract String getConfTable();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		Queue queue = QueueFactory.getQueue("monitoring");
		
		ArrayList<String> conf = getConfPage(getConfTable());

		Iterator<String> iter = conf.iterator();
		while (iter.hasNext()) {
			String idPage = iter.next();
			queue.add(TaskOptions.Builder.withUrl("/facebook/monitor").param("idPage", idPage));			
		}

	}

	private ArrayList<String> getConfPage(String entityName) {
		ArrayList<String> result = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(entityName);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity res : pq.asIterable()) {
			String idPage = res.getKey().getName();
			
			result.add(idPage);
		}
		return result;

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		doGet(req, resp);
	}

}
