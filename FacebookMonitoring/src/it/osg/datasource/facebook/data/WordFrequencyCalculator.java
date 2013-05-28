package it.osg.datasource.facebook.data;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import it.osg.datasource.SourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.OrderComparator;
import it.pipe.core.PipeBlock;
import it.pipe.core.PipelineEngine;
import it.pipe.filters.RemoveRegex;
import it.pipe.filters.RemoveWordList;
import it.pipe.transformers.DummyTransformer;
import it.pipe.transformers.FrequencyTransformer;
import it.pipe.transformers.Tokenizer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class WordFrequencyCalculator extends SourceGenerator {

	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();

		//GET INPUT PARAMETERS
		String transmissionName = (String) objects[0];
		int limit = Integer.valueOf((String) objects[3]);
		Date f = null;
		Date t = null;
		try {
			if (objects[1] != null && objects[2] != null) {
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}
			

			//Istanzio e configuro il pipeline engine
			PipelineEngine eng = new PipelineEngine();
			ArrayList<PipeBlock> blocks = new ArrayList<PipeBlock>();
			Tokenizer block1 = new Tokenizer("Tokenizer", null);
			blocks.add(block1);
			RemoveWordList block2 = new RemoveWordList("RemoveWordList", null);
			block2.addConfiguration("wordListPath", "stopwords_it.csv");
			blocks.add(block2);
			RemoveRegex block3 = new RemoveRegex("RemoveRegex", null);
			block3.addConfiguration("regex1", "^[0-9]+");
			block3.addConfiguration("regex2", "^\\d+$");
			blocks.add(block3);
			FrequencyTransformer block4 = new FrequencyTransformer("FrequencyTransformer", null);
			blocks.add(block4);

			//TODO getFacebookPost
			ArrayList<String> input = getFacebookCommentsAndPost(transmissionName, f, t, limit);
			//prendo il risultato della pipe
			ArrayList<String> pipeResult = eng.run(blocks, input);

			//Costruisco il grafo a partire dal risultato della pipe
			for(int i = 0; i < pipeResult.size(); i++) {
				String currOccurrence = pipeResult.get(i);
				String[] splitted = currOccurrence.split(",");
				Graph currGraph = new Graph(splitted[0], Long.valueOf(splitted[1]));
				result.add(currGraph);
			}
			
			Collections.sort(result,new OrderComparator());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (result.size() > limit) {
			ArrayList<Graph> subList = new ArrayList<Graph>();
			for (int i = 0; i < limit; i++) {
				Graph currGraph = result.get(i);
				subList.add(currGraph);
			}
			return subList;
		}
		
		return result;
		
	}

	private ArrayList<String> getFacebookCommentsAndPost(String transmissionName, Date f, Date t, int limit) {
		
		ArrayList<String> result = new ArrayList<String>();
		
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
		//facebook.setOAuthPermissions(commaSeparetedPermissions);
		facebook.setOAuthAccessToken(new AccessToken("156346967866710|gnswdSXw_ObP0RaWj5qqgK_HtCk", null));
		
		try {
			ResponseList<Post> facResults = facebook.getFeed(transmissionName, new Reading().since(f).until(t).limit(limit));
			Iterator<Post> iter = facResults.iterator();
			while (iter.hasNext()){
				Post curr = iter.next();
				String currText = curr.getMessage();
				if (currText != null) {
					result.add(currText);
				}
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return result;
	}


}
