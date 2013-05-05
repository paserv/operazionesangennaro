package it.pipe.filters;

import it.pipe.core.Filter;

import java.util.ArrayList;

public class FilterWordFromSentence extends Filter {


	public FilterWordFromSentence(String modName, String conFilePath) {
		super(modName, conFilePath);
	}

	@Override
	public ArrayList<String> getOutput(ArrayList<String> input) {

		ArrayList<String> result = new ArrayList<String>();
		String word =config.get("word");
		
		for (int i = 0; i < input.size(); i++){
			String currToken = input.get(i);

			if (currToken.toLowerCase().contains(" " + word.toLowerCase() + " ")) {
				result.add(currToken);
			}

		}
		System.out.println("\t-> Rimossi " + (input.size() - result.size()) + " items");
		return result;
	}


	

}
