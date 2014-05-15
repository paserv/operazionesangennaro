package test;

import java.util.ArrayList;

import it.pipe.core.PipeBlock;
import it.pipe.core.PipelineEngine;
import it.pipe.filters.RemoveRegex;
import it.pipe.filters.RemoveWordList;
import it.pipe.writers.FrequencyWriter;

public class TestEngineDaCodice {

	public static void main(String[] args) {
		
		ArrayList<String> input = new ArrayList<String>();
		input.add("paolo");
		input.add("paolo");
		input.add("paolo");
		input.add("grande");
		input.add("paolo");
		input.add("paolo");
		input.add("grande");
		input.add("della");
		input.add("paolo");
		
		PipelineEngine eng = new PipelineEngine();
		eng.setInput(input);		
		PipeBlock firstBlock = new RemoveWordList();
		firstBlock.setProperty("vocabularyPath1", "resources/stopwords_it.csv");
		PipeBlock secondBlock = new RemoveRegex();
		secondBlock.setProperty("regex1", "^[0-9]+");
		PipeBlock thirdBlock = new FrequencyWriter();
		thirdBlock.setProperty("path", "C:\\output.hashtag");
		thirdBlock.setProperty("max", "100");
		eng.addBlock(firstBlock);
		eng.addBlock(secondBlock);
		eng.addBlock(thirdBlock);
		
		eng.run();
	}
	
}
