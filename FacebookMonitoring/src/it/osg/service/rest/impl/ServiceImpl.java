package it.osg.service.rest.impl;

import it.osg.service.model.IdName;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;



@Path("/resource/")
public class ServiceImpl extends Service {

	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("conf/monitoredentities/{table}")
	public Response getMonitoredEntities(@PathParam("table") String table) {

		ArrayList<IdName> result = new ArrayList<IdName>();
		DatastoreService DS = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		q = new Query(table);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			IdName curr = new IdName();
			curr.setIdentificativo(ent.getKey().getName());
			curr.setNome((String) ent.getProperty("nome"));
			result.add(curr);
		}
		final GenericEntity<List<IdName>> entity = new GenericEntity<List<IdName>>(result) { };
		return Response.ok().entity(entity).build();
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/likes/{transmission}/{from}/{to}")
	public Response getLikesCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {

		return Service.getGraphData("it.osg.datasource.facebook.time.PickedData", new Object[]{transmission, from, to, "date", "like_count"}, "TIME");

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/dlikes/{transmission}/{from}/{to}")
	public Response getDLikesCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {

		return Service.getGraphData("it.osg.datasource.facebook.time.DPickedData", new Object[]{transmission, from, to, "date", "like_count"}, "TIME");

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/talkingabout/{transmission}/{from}/{to}")
	public Response getTalkingAboutCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {

		return Service.getGraphData("it.osg.datasource.facebook.time.PickedData", new Object[]{transmission, from, to, "date", "talking_about_count"}, "TIME");

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/dtalkingabout/{transmission}/{from}/{to}")
	public Response getDTalkingAboutCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {

		return Service.getGraphData("it.osg.datasource.facebook.time.DPickedData", new Object[]{transmission, from, to, "date", "talking_about_count"}, "TIME");

	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/post/{transmission}/{from}/{to}")
	public Response getCommentCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {
		
		return Service.getGraphData("it.osg.datasource.facebook.time.PostCount", new Object[]{transmission, from, to}, "TIME");

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("label/wordfrequency/{transmission}/{from}/{to}/{limit}")
	public Response getWordFrequencyCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to, @PathParam("limit") String limit) {
		
		return Service.getGraphData("it.osg.datasource.facebook.label.WordFrequencyCalculator", new Object[]{transmission, from, to, limit}, "DATA");

	}
	

}
