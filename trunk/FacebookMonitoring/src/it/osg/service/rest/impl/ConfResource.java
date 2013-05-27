package it.osg.service.rest.impl;

import java.util.ArrayList;

import it.osg.service.model.Conf;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


@Path("/config/")
public class ConfResource {

	private static String conf_tablename = "pages";
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/conf")
	public ArrayList<Conf> getConf() {
		ArrayList<Conf> result = new ArrayList<Conf>();

		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(conf_tablename);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity ent : pq.asIterable()) {
			String url = (String) ent.getProperty("url");

			Key key = ent.getKey();
			Conf conf = new Conf();
			conf.setUrl(url);
			conf.setDatastoreId(key);
			conf.setId(key.getName());
			result.add(conf);
			System.out.println(conf.toString());


		}

		return result;
	}



}
