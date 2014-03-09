package it.cloudpanel.service.rest.impl;

import java.util.ArrayList;

import it.cloudpanel.service.converter.DomandaConverter;
import it.cloudpanel.service.model.Domanda;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;


@Path("/resource/")
public class DomandeResource {

	private static String source_graph_tablename = "source_graph_tablename";
	private static String domande_tablename = "source_domanda";
	private static String poll_domanda = "poll_domanda";

	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/domande")
	public ArrayList<Domanda> getDomande() {
		ArrayList<Domanda> result = new ArrayList<Domanda>();

		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(domande_tablename);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity ent : pq.asIterable()) {
			String domanda = (String) ent.getProperty("domanda");

			Key key = ent.getKey();
			Domanda dom = new Domanda();
			dom.setDomanda(domanda);
			dom.setDatastoreId(key);
			dom.setId(key.getName());
			result.add(dom);
			System.out.println(dom.toString());


		}

		return result;
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/polldomanda")
	public Response putDomanda(@FormParam("id") int id, @FormParam("descrizione") String descr, @FormParam("costs") boolean costs) {
		Entity currEntity = new Entity(poll_domanda);
		currEntity.setProperty("id", id);
		currEntity.setProperty("descrizione", descr);
		currEntity.setProperty("costs", costs);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(currEntity);
		return Response.status(201).entity(descr).build();

	}

}
