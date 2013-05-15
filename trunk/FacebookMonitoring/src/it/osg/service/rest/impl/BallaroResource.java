package it.osg.service.rest.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.google.appengine.api.datastore.Query.SortDirection;


@Path("/resource/")
public class BallaroResource {

	private static String ballaro_tablename = "ballaro";
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/ballaro")
	public ArrayList<BallaroData> getBallaroData() {
		ArrayList<BallaroData> result = new ArrayList<BallaroData>();

		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(ballaro_tablename).addSort("timestamp", SortDirection.ASCENDING);;

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity ent : pq.asIterable()) {
			
			Date date = new Date();
			
			Object dt = ent.getProperty("date");
			if (dt instanceof Date) {
				date = (Date) dt;
			} else {
				Calendar cal= Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				cal.add(Calendar.HOUR_OF_DAY, 2);
				SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date cestDate= cal.getTime();
			}
			
			long likeCount = 0L;
			
			Object lk = ent.getProperty("like_count");
			if (lk instanceof Long) {
				likeCount = (Long) lk;
			} else {
				likeCount = Long.valueOf((String) lk);
			}
			
			long talkingAboutCount = 0L;
			Object tk = ent.getProperty("talking_about_count");
			if (tk instanceof Long) {
				talkingAboutCount = (Long) tk;
			} else {
				talkingAboutCount = Long.valueOf((String) tk);
			}
					
			
			long timestamp = 0L;
			Object tm = ent.getProperty("timestamp");
			if (tm instanceof Long) {
				timestamp = (Long) tm;
			} else {
				timestamp = Long.valueOf((String) tm);
			}
			
			Key key = ent.getKey();
			
			BallaroData curr = new BallaroData();
			curr.setDate(date);
			curr.setId(key.getName());
			curr.setDatastoreId(key);
			curr.setLikeCount(likeCount);
			curr.setTalkingAboutCount(talkingAboutCount);
			curr.setTimestamp(timestamp);
			result.add(curr);
			System.out.println(curr.toString());


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
