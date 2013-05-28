package it.osg.service.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/resource/")
public class ServiceImpl extends Service {

	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/likes/{transmission}/{from}/{to}")
	public Response getLikesCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {

		return Service.getGraphData("it.osg.datasource.facebook.time.Like", new Object[]{transmission, from, to, "date", "like_count"}, "TIME");

	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/commentcount/{transmission}/{from}/{to}")
	public Response getCommentCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {
		
		return Service.getGraphData("it.osg.datasource.facebook.time.Comment", new Object[]{transmission, from, to}, "TIME");

	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("data/wordfrequency/{transmission}/{from}/{to}/{limit}")
	public Response getWordFrequencyCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to, @PathParam("limit") String limit) {
		
		return Service.getGraphData("it.osg.datasource.facebook.data.WordFrequencyCalculator", new Object[]{transmission, from, to, limit}, "DATA");

	}
	

}
