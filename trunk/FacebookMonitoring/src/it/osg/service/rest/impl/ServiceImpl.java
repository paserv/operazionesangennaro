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

		return Service.getGraphData("it.osg.datasource.FacebookLikeTime", new Object[]{transmission, from, to, "date", "like_count"}, "DATA");

	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("time/commentcount/{transmission}/{from}/{to}")
	public Response getCommentCount(@PathParam("transmission") String transmission, @PathParam("from") String from, @PathParam("to") String to) {
		
		return Service.getGraphData("it.osg.datasource.FacebookCommentTime", new Object[]{transmission, from, to}, "DATA");

	}
	

}
