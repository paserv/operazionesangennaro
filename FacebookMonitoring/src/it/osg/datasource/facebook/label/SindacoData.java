package it.osg.datasource.facebook.label;

import facebook4j.Post;
import it.osg.datasource.GraphSourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class SindacoData extends GraphSourceGenerator {

	
	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();

		//Dati di input
		String sindaco = (String) objects[0];
		Date f = null;
		Date t = null;
		try {
			if (objects[1] != null && objects[2] != null){
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}

			//Dati di Output
			//Media giornaliera dei nuovi Post pubblicati sulla fan page
			ArrayList<Post> posts = FacebookUtils.getAllPosts(sindaco, f, t, new String[]{"id", "created_time"});
			double numTotalePost = posts.size();
			double numGiorni = DateUtils.giorniTraDueDate(f, t);
			Graph currGraph = new Graph("mediapost", numTotalePost/numGiorni);
			result.add(currGraph);
			

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;


	}






}
