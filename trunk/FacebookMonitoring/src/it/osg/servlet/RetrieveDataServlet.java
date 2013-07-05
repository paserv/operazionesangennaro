package it.osg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class RetrieveDataServlet extends HttpServlet {

	//private static String confTable = "pages";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String tabella = req.getParameter("tab");
		String campo = req.getParameter("cam");

		out.println("Tabella = " + tabella + "<br>Campo = " + campo + "<br><br>");

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(tabella);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity res : pq.asIterable()) {
			Object value = res.getProperty(campo);
			if (value instanceof Text) {
				out.println(((Text) value).getValue() + "<br>");
			} else {
				out.println(value.toString() + "<br>");
			}
			
		}


	}


//	private ArrayList<Object> getConfPage(String entityName, String campo) {
//		ArrayList<Object> result = new ArrayList<Object>();
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Query q = new Query(entityName);
//		PreparedQuery pq = datastore.prepare(q);
//
//		for (Entity res : pq.asIterable()) {
//			//String id = res.getKey().getName();
//			Object value = res.getProperty(campo);
//			result.add(value);
//		}
//		return result;
//
//	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}



}
