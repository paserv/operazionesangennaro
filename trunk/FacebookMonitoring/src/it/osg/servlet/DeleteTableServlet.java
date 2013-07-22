package it.osg.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public abstract class DeleteTableServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected abstract String getTabella();
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		q = new Query(getTabella());
		pq = datastore.prepare(q);
		for (Entity res : pq.asIterable()) {
			datastore.delete(res.getKey());
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	
}
