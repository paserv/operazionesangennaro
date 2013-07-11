package it.osg.servlet.monitoring;

import it.osg.datapicker.FacebookDataPicker;
import it.osg.utils.JSONObjectUtil;

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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public abstract class MonitoringServlet extends HttpServlet {
	
	//private String confTable;
	private static String graphAPIUrl = "https://graph.facebook.com/";

	public abstract String getConfTable();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//TODO Li deve mettere in coda
		
		
		
		
		
		
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		ArrayList<String> conf = getConfPage(getConfTable());

		Iterator<String> iter = conf.iterator();
		while (iter.hasNext()) {
			String idPage = iter.next();

			String jsonString = JSONObjectUtil.retrieveJson(graphAPIUrl + idPage);
			ArrayList<Hashtable<String, Object>> analisi = FacebookDataPicker.likeTalkAnalysis(jsonString);

			Iterator<Hashtable<String, Object>> iterAna = analisi.iterator();
			while (iterAna.hasNext()) {
				Entity currEntity = new Entity(idPage);
				Hashtable<String, Object> currRow = iterAna.next();
				Enumeration<String> enumer = currRow.keys();
				while (enumer.hasMoreElements()) {
					String currKey = enumer.nextElement();
					Object currValue = currRow.get(currKey);
					currEntity.setProperty(currKey, currValue);
					System.out.println(currKey + " " + currValue);
				}
				datastore.put(currEntity);

			}
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
