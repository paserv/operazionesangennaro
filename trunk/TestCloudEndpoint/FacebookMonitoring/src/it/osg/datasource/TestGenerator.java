package it.osg.datasource;

import it.osg.service.model.Graph;

import java.util.ArrayList;

public class TestGenerator extends GraphSourceGenerator {

	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {
		ArrayList<Graph> result = new ArrayList<Graph>();
		
		for (int i = 0; i < 10; i++){
			Graph curr = new Graph(String.valueOf(i), Long.valueOf(String.valueOf(i)));
			result.add(curr);
		}
		
		return result;
	}

}
