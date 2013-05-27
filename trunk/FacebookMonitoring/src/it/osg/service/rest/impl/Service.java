package it.osg.service.rest.impl;

import java.util.ArrayList;
import java.util.List;

import it.osg.datasource.SourceGenerator;
import it.osg.service.model.Graph;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;



public abstract class Service {

	 public enum graphType {DATA,TIME};
	

	public static Response getGraphData(String className, Object[] objects, String gt) {
		
		ArrayList<Graph> result = SourceGenerator.getData(className, objects);
		
		final GenericEntity<List<Graph>> entity = new GenericEntity<List<Graph>>(result) { };
		return Response.ok().entity(entity).build();

	} 



}



