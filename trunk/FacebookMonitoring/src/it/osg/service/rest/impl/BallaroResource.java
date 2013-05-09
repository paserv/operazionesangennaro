package it.osg.service.rest.impl;

import java.util.ArrayList;
import java.util.Date;

import it.osg.service.converter.BallaroDataConverter;
import it.osg.service.model.BallaroData;
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


@Path("/resource/")
public class BallaroResource {

	private static String ballaro_tablename = "ballaro";
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/ballaro")
	public ArrayList<BallaroDataConverter> getConf() {
		ArrayList<BallaroDataConverter> result = new ArrayList<BallaroDataConverter>();

		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(ballaro_tablename);

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity ent : pq.asIterable()) {
			Date date = (Date) ent.getProperty("date");
			String likeCount = (String) ent.getProperty("like_count");
			String talkingAboutCount = (String) ent.getProperty("talking_about_count");
			long timestamp = (Long) ent.getProperty("timestamp");
			Key key = ent.getKey();
			
			BallaroData curr = new BallaroData();
			curr.setDate(date);
			curr.setId(key.getName());
			curr.setDatastoreId(key);
			curr.setLikeCount(likeCount);
			curr.setTalkingAboutCount(talkingAboutCount);
			curr.setTimestamp(timestamp);
			result.add(new BallaroDataConverter(curr));
			System.out.println(curr.toString());


		}

		return result;
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/polldomanda")
	public Response putDomanda(@FormParam("id") int id, @FormParam("descrizione") String descr, @FormParam("costs") boolean costs) {
		Entity currEntity = new Entity("");
		currEntity.setProperty("id", id);
		currEntity.setProperty("descrizione", descr);
		currEntity.setProperty("costs", costs);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(currEntity);
		return Response.status(201).entity(descr).build();

	}

}
