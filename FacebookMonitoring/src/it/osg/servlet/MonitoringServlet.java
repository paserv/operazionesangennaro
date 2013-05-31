package it.osg.servlet;

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
public class MonitoringServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Hashtable<String, String> conf = getConfPage("pages");

		Enumeration<String> en = conf.keys();
		while (en.hasMoreElements()) {
			String entityName = en.nextElement();
			String currUrl = conf.get(entityName);

			String jsonString = JSONObjectUtil.retrieveJson(currUrl);
			ArrayList<Hashtable<String, Object>> analisi = FacebookDataPicker
					.likeTalkAnalysis(jsonString);

			Iterator<Hashtable<String, Object>> iter = analisi.iterator();
			while (iter.hasNext()) {
				Entity currEntity = new Entity(entityName);
				Hashtable<String, Object> currRow = iter.next();
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

	private Hashtable<String, String> getConfPage(String entityName) {
		Hashtable<String, String> result = new Hashtable<String, String>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query(entityName);
		PreparedQuery pq = datastore.prepare(q);

		for (Entity res : pq.asIterable()) {
			String id = res.getKey().getName();
			String url = (String) res.getProperty("url");

			result.put(id, url);
		}
		return result;

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		doGet(req, resp);
	}

}
