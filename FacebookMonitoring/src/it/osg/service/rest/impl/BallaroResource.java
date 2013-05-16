package it.osg.service.rest.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import it.osg.service.model.TransmissionData;
import it.osg.utils.DataStoreAO;
import it.osg.utils.DateUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/resource/")
public class BallaroResource {

	private static String ballaro_tablename = "ballaro";
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/ballaro")
	public ArrayList<TransmissionData> getBallaroData() {
		
		ArrayList<TransmissionData> result = new ArrayList<TransmissionData>();
		
		try {
			Date from = DateUtils.parseDateAndTime("15-05-2013 10:30:00");
			Date to = DateUtils.parseDateAndTime("15-05-2013 11:20:00");
			result = DataStoreAO.getTransmissionData("ballaro", from, to);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;

	}

	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@Path("/ballarotimeinterval")
	public ArrayList<TransmissionData> getBallaroDataFilter(@FormParam("from") String from, @FormParam("to") String to) {
		ArrayList<TransmissionData> result = new ArrayList<TransmissionData>();
		
		try {
			Date f = DateUtils.parseDateAndTime(from);
			Date t = DateUtils.parseDateAndTime(to);
			result = DataStoreAO.getTransmissionData("ballaro", f, t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;

	}

}
