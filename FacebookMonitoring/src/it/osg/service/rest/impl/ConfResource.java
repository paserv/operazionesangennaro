package it.osg.service.rest.impl;

import java.util.ArrayList;

import it.osg.service.converter.ConfConverter;
import it.osg.service.model.Conf;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public ArrayList<ConfConverter> getConf() {
		ArrayList<ConfConverter> result = new ArrayList<ConfConverter>();

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
			result.add(new ConfConverter(conf));
			System.out.println(conf.toString());


		}

		return result;
	}

//	@POST
//	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
//	@Path("/polldomanda")
//	public Response putDomanda(@FormParam("id") int id, @FormParam("descrizione") String descr, @FormParam("costs") boolean costs) {
//		Entity currEntity = new Entity("");
//		currEntity.setProperty("id", id);
//		currEntity.setProperty("descrizione", descr);
//		currEntity.setProperty("costs", costs);
//
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		datastore.put(currEntity);
//		return Response.status(201).entity(descr).build();
//
//	}

}
