package it.osg.datasource.facebook.data;

import it.osg.datasource.SourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.pipe.core.PipeBlock;
import it.pipe.core.PipelineEngine;
import it.pipe.filters.RemoveRegex;
import it.pipe.filters.RemoveWordList;
import it.pipe.transformers.DummyTransformer;
import it.pipe.transformers.FrequencyTransformer;
import it.pipe.transformers.Tokenizer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class WordFrequencyCalculator extends SourceGenerator {

	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();

		//GET INPUT PARAMETERS
		String transmissionName = (String) objects[0];
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
			Tokenizer block1 = new Tokenizer("block1", null);
			blocks.add(block1);
			RemoveWordList block2 = new RemoveWordList("block2", null);
			block2.addConfiguration("wordListPath", "stopwords_it.csv");
			blocks.add(block2);
			RemoveRegex block3 = new RemoveRegex("block3", null);
			block3.addConfiguration("regex1", "regex1,^[0-9]+");
			blocks.add(block3);
			FrequencyTransformer block4 = new FrequencyTransformer("block4", null);
			blocks.add(block4);

			//TODO getFacebookPost
			ArrayList<String> input = new ArrayList<String>();
			input.add("paolo servillo paolo antonio roberto gaviscon gaviscon gaviscon");
			//prendo il risultato della pipe
			ArrayList<String> pipeResult = eng.run(blocks, input);

			//Costruisco il grafo a partire dal risultato della pipe
			for(int i = 0; i < pipeResult.size(); i++) {
				String currOccurrence = pipeResult.get(i);
				String[] splitted = currOccurrence.split(",");
				Graph currGraph = new Graph(splitted[0], Long.valueOf(splitted[1]));
				result.add(currGraph);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

}
